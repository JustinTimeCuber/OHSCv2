public class TakingScreen extends GameplayScreen {
    static final TakingScreen INSTANCE = new TakingScreen();

    private TakingScreen() {
    }

    void handleTakenChange(Player p, boolean pos, OhHellScoreboardV2 sc) {
        if(pos) {
            if(p.taken < sc.tricks[sc.trick_index] || sc.trick_mode == 0) {
                p.taken++;
            } else {
                sc.displayError("Cannot take more tricks than were dealt");
            }
        } else if(p.taken > 0) {
            p.taken--;
        } else {
            sc.displayError("Tricks taken must be greater than zero");
        }
    }

    void handleFinishRound(OhHellScoreboardV2 sc) {
        int total_taken = 0;
        for(Player p : Player.players) {
            total_taken += p.taken;
        }
        if(sc.trick_mode == 0 || total_taken == sc.tricks[sc.trick_index] || sc.enterRightClick()) {
            sc.hands_played++;
            Logger.write("--------------------------------");
            Logger.write("Hand #" + sc.hands_played + " - Tricks: " + sc.tricks[sc.trick_index]);
            for(int i = 0; i < Player.count(); i++) {
                Player p = Player.get(i);
                int old = p.score;
                p.total_bid += p.bid;
                p.total_taken += p.taken;
                if(p.taken < p.bid) {
                    if(Config.overbid_set) {
                        p.times_set++;
                        p.score += Config.points_set * (Config.set_penalty_scales ? p.bid - p.taken : 1);
                        if(!Config.set_prevents_trick_points) {
                            p.score += Config.points_per_trick * p.taken;
                        }
                    } else {
                        p.score += Config.points_per_trick * p.taken;
                    }
                } else if(p.taken > p.bid) {
                    if(Config.underbid_set) {
                        p.times_set++;
                        p.score += Config.points_set * (Config.set_penalty_scales ? p.taken - p.bid : 1);
                        if(!Config.set_prevents_trick_points) {
                            p.score += Config.points_per_trick * p.taken;
                        }
                    } else {
                        p.score += Config.points_per_trick * p.taken;
                    }
                } else {
                    p.bonuses++;
                    p.score += Config.points_bonus + Config.points_per_trick * p.taken;
                }
                if(p.score < 0 && !Config.negative_scores_allowed) {
                    p.score = 0;
                }
                p.hands_played++;
                Logger.write(p.getName(i) + " bid " + p.bid + " tricks and took " + p.taken + ". " + old + " --> " + p.score);
                p.bid = 0;
                p.has_bid = false;
                p.taken = 0;
            }
            if(sc.trick_mode != 0) {
                sc.trick_index++;
                if(sc.trick_index >= sc.tricks.length) {
                    ScreenManager.setScreen(GameOverScreen.INSTANCE);
                    sc.saveRecord();
                    sc.trick_index--;
                    return;
                }
            }
            sc.trump_suit = 0;
            ScreenManager.setScreen(BiddingScreen.INSTANCE);
        } else {
            sc.displayError("Tricks taken must equal tricks dealt - override with enter + right click");
        }
    }

    @Override
    public void draw(OhHellScoreboardV2 sc) {
        super.draw(sc);
        sc.textAlign(sc.CENTER, sc.CENTER);
        for(int i = 0; i < Player.count(); i++) {
            Player p = Player.get(i);
            sc.fill(p.display_color);
            sc.textSize(game_tiles[0].w() * 0.04f);
            sc.text("Taken", game_tiles[i].mx() - game_tiles[i].w() * 0.125f, game_tiles[i].my() - game_tiles[0].w() * 0.167f);
            sc.textSize(game_tiles[0].w() * 0.1f);
            sc.text(p.taken, game_tiles[i].mx() - game_tiles[i].w() * 0.125f, game_tiles[i].my() - game_tiles[0].w() * 0.1f);
        }

        sc.drawButton(edit_bids_button, "Edit Bids", 0.02f, true, true);
        sc.drawButton(proceed_button, "Proceed", 0.02f, sc.trick_mode == 0 || total_taken == sc.tricks[sc.trick_index], true);

        if(sc.trick_mode != 0) {
            if(total_bid < sc.tricks[sc.trick_index]) {
                sc.fill(Theme.theme.underbid_color);
            } else {
                sc.fill(Theme.theme.overbid_color);
            }
        }
        sc.textSize(sc.width * 0.05f);
        sc.text(total_bid, sc.width * 0.8f, sc.height * 0.93f);
    }

    @Override
    public void mousePressed(OhHellScoreboardV2 sc) {
        for(int i = 0; i < Player.count(); i++) {
            if(game_tiles[i].mouseInTile()) {
                handleTakenChange(Player.get(i), sc.mouseButton == sc.LEFT, sc);
                return;
            }
        }
        if(proceed_button.mouseInTile()) {
            handleFinishRound(sc);
        } else {
            super.mousePressed(sc);
        }
    }

    @Override
    public void keyTyped(OhHellScoreboardV2 sc) {
        char key = sc.key;
        super.keyTyped(sc);
        if(key == sc.ENTER) {
            handleFinishRound(sc);
        }
        int i = Math.abs(sc.getKeyValue(key));
        if(i != 0 && i - 1 < Player.count()) {
            i--;
            Player p = Player.get(i);
            handleTakenChange(p, sc.getKeyValue(key) > 0, sc);
        }
    }
}

public class BiddingScreen extends GameplayScreen {
    static final BiddingScreen INSTANCE = new BiddingScreen();

    private BiddingScreen() {
    }

    void handleBidChange(Player p, boolean pos, OhHellScoreboardV2 sc) {
        if(pos) {
            if(p.bid <= sc.tricks[sc.trick_index] || sc.trick_mode == 0) {
                if(!p.has_bid) {
                    p.has_bid = true;
                }
                p.bid++;
            } else {
                sc.displayError("Maximum bid for this hand is " + (sc.tricks[sc.trick_index] + 1));
            }
        } else if(p.bid > 0) {
            p.bid--;
        } else {
            p.has_bid = !p.has_bid;
        }
    }

    void handleFinishBidding(OhHellScoreboardV2 sc) {
        int total_bid = 0;
        boolean all_players_bid = true;
        for(Player p : Player.players) {
            total_bid += p.bid;
            all_players_bid &= p.has_bid;
        }
        if(all_players_bid) {
            if(sc.trick_mode == 0 || total_bid != sc.tricks[sc.trick_index] || sc.enterRightClick()) {
                ScreenManager.setScreen(TakingScreen.INSTANCE);
            } else {
                sc.displayError("Tricks bid can't equal tricks dealt - override with enter + right click");
            }
        } else {
            sc.displayError("Not all players have a bid entered");
        }
    }

    @Override
    public void draw(OhHellScoreboardV2 sc) {
        super.draw(sc);
        sc.drawButton(change_bids_button, "Change Bids", 0.02f, false, true);
        sc.drawButton(proceed_button, "Proceed", 0.02f, sc.trick_mode == 0 || total_bid != sc.tricks[sc.trick_index], true);
        sc.fill(Theme.theme.text_color);
        sc.textSize(sc.width * 0.05f);
        sc.text(total_bid, sc.width * 0.8f, sc.height * 0.93f);
    }

    @Override
    public void mousePressed(OhHellScoreboardV2 sc) {
        for(int i = 0; i < Player.count(); i++) {
            if(game_tiles[i].mouseInTile()) {
                handleBidChange(Player.get(i), sc.mouseButton == sc.LEFT, sc);
                return;
            }
        }
        if(proceed_button.mouseInTile()) {
            handleFinishBidding(sc);
        } else {
            super.mousePressed(sc);
        }
    }
}
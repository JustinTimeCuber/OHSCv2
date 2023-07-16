public class TakingScreen extends GameplayScreen {
    static void draw(OhHellScoreboardV2 sc) {
        GameplayScreen.draw(sc);
        sc.textAlign(sc.CENTER, sc.CENTER);
        for(int i = 0; i < Player.count(); i++) {
            Player p = Player.players.get(i);
            total_bid += p.bid;
            sc.fill(p.display_color);
            sc.textSize(sc.game_tiles[0].w() * 0.04f);
            sc.text("Taken", sc.game_tiles[i].mx() - sc.game_tiles[i].w() * 0.125f, sc.game_tiles[i].my() - sc.game_tiles[0].w() * 0.167f);
            sc.textSize(sc.game_tiles[0].w() * 0.1f);
            sc.text(p.taken, sc.game_tiles[i].mx() - sc.game_tiles[i].w() * 0.125f, sc.game_tiles[i].my() - sc.game_tiles[0].w() * 0.1f);
        }

        sc.drawButton(sc.change_bids_button, "Change Bids", 0.02f, true, true);
        sc.drawButton(sc.proceed_button, "Proceed", 0.02f, sc.trick_mode == 0 || total_taken == sc.tricks[sc.trick_index], true);

        if(sc.trick_mode != 0) {
            if(total_bid < sc.tricks[sc.trick_index]) {
                sc.fill(Theme.theme.underbid_color);
            } else {
                sc.fill(Theme.theme.overbid_color);
            }
        }
    }
}

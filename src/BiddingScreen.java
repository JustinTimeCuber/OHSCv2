public class BiddingScreen extends GameplayScreen {
    static void draw(OhHellScoreboardV2 sc) {
        GameplayScreen.draw(sc);
        sc.drawButton(sc.change_bids_button, "Change Bids", 0.02f, false, true);
        sc.drawButton(sc.proceed_button, "Proceed", 0.02f, sc.trick_mode == 0 || total_bid != sc.tricks[sc.trick_index], true);
    }
}
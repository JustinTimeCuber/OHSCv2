import processing.core.PImage;

public class GameplayScreen {

    static int total_bid;
    static int total_taken;

    static void draw(OhHellScoreboardV2 sc) {
        sc.textAlign(sc.CENTER, sc.CENTER);
        total_bid = 0;
        total_taken = 0;
        for(int i = 0; i < sc.players.size(); i++) {
            Player p = sc.players.get(i);
            total_bid += p.bid;
            total_taken += p.taken;
            sc.fill(Theme.theme.background_color);
            sc.rect(sc.game_tiles[i].x(), sc.game_tiles[i].y(), sc.game_tiles[i].w(), sc.game_tiles[i].h());
            sc.fill(p.display_color);
            sc.textSize(sc.game_tiles[0].w() * 0.1f);
            sc.text(p.name.equals("") ? ("Player " + (i + 1)) : p.name, sc.game_tiles[i].cx(), sc.game_tiles[i].y() + sc.game_tiles[i].h() * 0.167f);
            sc.textSize(sc.game_tiles[i].h() * 0.5f);
            sc.text(p.score, sc.game_tiles[i].cx(), sc.game_tiles[i].my() - sc.game_tiles[i].h() * 0.43f);
            sc.textSize(sc.game_tiles[0].w() * 0.04f);
            sc.text("Bid", sc.game_tiles[i].x() + sc.game_tiles[i].w() * 0.125f, sc.game_tiles[i].my() - sc.game_tiles[0].w() * 0.167f);
            if(p.has_bid) {
                sc.textSize(sc.game_tiles[0].w() * 0.1f);
                sc.text(p.bid, sc.game_tiles[i].x() + sc.game_tiles[i].w() * 0.125f, sc.game_tiles[i].my() - sc.game_tiles[0].w() * 0.1f);
            }
            if(sc.current_screen == Screen.TAKING) {
                sc.textSize(sc.game_tiles[0].w() * 0.04f);
                sc.text("Taken", sc.game_tiles[i].mx() - sc.game_tiles[i].w() * 0.125f, sc.game_tiles[i].my() - sc.game_tiles[0].w() * 0.167f);
                sc.textSize(sc.game_tiles[0].w() * 0.1f);
                sc.text(p.taken, sc.game_tiles[i].mx() - sc.game_tiles[i].w() * 0.125f, sc.game_tiles[i].my() - sc.game_tiles[0].w() * 0.1f);
            }
        }
        sc.drawButton(sc.setup_button, "Setup", 0.02f, true, true);
        sc.drawButton(sc.end_game_button, "End Game", 0.02f, true, true);
        sc.textSize(sc.width * 0.01f);
        sc.fill(Theme.theme.text_color);
        sc.text("Deal", sc.width * 0.72f, sc.height * 0.87f);
        sc.text("Bid", sc.width * 0.8f, sc.height * 0.87f);
        sc.text("Taken", sc.width * 0.88f, sc.height * 0.87f);
        sc.text("Trump", sc.width * 0.96f, sc.height * 0.87f);
        sc.textSize(sc.width * 0.05f);
        sc.text(sc.trick_mode == 0 ? "--" : String.valueOf(sc.tricks[sc.trick_index]), sc.width * 0.72f, sc.height * 0.93f);
        if(sc.current_screen == Screen.TAKING) {
            if(sc.trick_mode != 0) {
                if(total_bid < sc.tricks[sc.trick_index]) {
                    sc.fill(Theme.theme.underbid_color);
                } else {
                    sc.fill(Theme.theme.overbid_color);
                }
            }
        }
        sc.text(total_bid, sc.width * 0.8f, sc.height * 0.93f);
        sc.fill(Theme.theme.text_color);
        sc.text(total_taken, sc.width * 0.88f, sc.height * 0.93f);
        PImage trump_icon = sc.trumpIcon();
        if(trump_icon != null) {
            sc.image(trump_icon, sc.trump_suit_bounding_box.x() + sc.trump_suit_bounding_box.w() * 0.25f, sc.trump_suit_bounding_box.y() + sc.trump_suit_bounding_box.h() * 0.25f, sc.trump_suit_bounding_box.w() * 0.5f, sc.trump_suit_bounding_box.h() * 0.5f);
        }
    }
}

import processing.core.PImage;

public abstract class GameplayScreen implements Screen {

    int total_bid;
    int total_taken;
    Tile setup_button, edit_bids_button, proceed_button, statistics_button, end_game_button, trump_suit_bounding_box;
    Tile confirm_end_game_button, cancel_end_game_button;
    static Tile[] game_tiles;

    void handleChangeBids(OhHellScoreboardV2 sc) {
        if(ScreenManager.currentScreen() instanceof BiddingScreen) {
            sc.displayError("Already changing bids");
        } else {
            ScreenManager.setScreen(BiddingScreen.INSTANCE);
        }
    }

    void handleEndGame(OhHellScoreboardV2 sc) {
        if(sc.enterRightClick() || Window.current == Window.CONFIRM_END_GAME) {
            ScreenManager.setScreen(GameOverScreen.INSTANCE);
            Window.current = Window.NONE;
            sc.saveRecord();
        } else {
            Window.current = Window.CONFIRM_END_GAME;
        }
    }

    void handleTrumpButton(OhHellScoreboardV2 sc) {
        sc.trump_suit = sc.mouseButton == sc.LEFT ? Suit.getNext(sc.trump_suit) : Suit.getPrevious(sc.trump_suit);
    }

    @Override
    public void draw(OhHellScoreboardV2 sc) {
        sc.textAlign(sc.CENTER, sc.CENTER);
        total_bid = 0;
        total_taken = 0;
        for(int i = 0; i < Player.count(); i++) {
            Player p = Player.get(i);
            total_bid += p.bid;
            total_taken += p.taken;
            sc.fill(Theme.theme.background_color);
            sc.rect(game_tiles[i].x(), game_tiles[i].y(), game_tiles[i].w(), game_tiles[i].h());
            sc.fill(p.display_color);
            sc.textSize(game_tiles[0].w() * 0.1f);
            sc.text(p.getName(i), game_tiles[i].cx(), game_tiles[i].y() + game_tiles[i].h() * 0.167f);
            sc.textSize(game_tiles[i].h() * 0.5f);
            sc.text(p.score, game_tiles[i].cx(), game_tiles[i].my() - game_tiles[i].h() * 0.43f);
            sc.textSize(game_tiles[0].w() * 0.04f);
            sc.text("Bid", game_tiles[i].x() + game_tiles[i].w() * 0.125f, game_tiles[i].my() - game_tiles[0].w() * 0.167f);
            if(p.has_bid) {
                sc.textSize(game_tiles[0].w() * 0.1f);
                sc.text(p.bid, game_tiles[i].x() + game_tiles[i].w() * 0.125f, game_tiles[i].my() - game_tiles[0].w() * 0.1f);
            }
        }
        sc.drawButton(setup_button, "Setup", 0.02f, true, true);
        sc.drawButton(statistics_button, "Statistics", 0.02f, true, true);
        sc.drawButton(end_game_button, "End Game", 0.02f, true, true);
        if(sc.trick_mode == 0) {
            sc.fill(Theme.theme.grayed_text_color);
            sc.noStroke();
            sc.rect(sc.width * 0.7f, sc.height * 0.92f, sc.width * 0.04f, sc.height * 0.02f);
        } else {
            sc.fill(Theme.theme.text_color);
            sc.textSize(sc.width * 0.05f);
            sc.text(String.valueOf(sc.tricks[sc.trick_index]), sc.width * 0.72f, sc.height * 0.93f);
        }
        sc.textSize(sc.width * 0.01f);
        sc.text("Deal", sc.width * 0.72f, sc.height * 0.87f);
        sc.fill(Theme.theme.text_color);
        sc.text("Bid", sc.width * 0.8f, sc.height * 0.87f);
        sc.text("Taken", sc.width * 0.88f, sc.height * 0.87f);
        sc.text("Trump", sc.width * 0.96f, sc.height * 0.87f);
        sc.textSize(sc.width * 0.05f);
        sc.text(total_taken, sc.width * 0.88f, sc.height * 0.93f);
        if(sc.trump_suit != null) {
            PImage trump_icon = sc.trump_suit.image;
            if (trump_icon != null) {
                sc.image(trump_icon, trump_suit_bounding_box.x() + trump_suit_bounding_box.w() * 0.25f, trump_suit_bounding_box.y() + trump_suit_bounding_box.h() * 0.25f, trump_suit_bounding_box.w() * 0.5f, trump_suit_bounding_box.h() * 0.5f);
            }
        }
    }

    public void drawEndGameConfirmationBox(OhHellScoreboardV2 sc) {
        if(Window.current == Window.CONFIRM_END_GAME) {
            sc.fill(Theme.theme.popup_background_color, 230);
            sc.stroke(Theme.theme.line_color);
            sc.rect(Window.small_tile.x(), Window.small_tile.y(), Window.small_tile.w(), Window.small_tile.h());
            sc.drawButton(cancel_end_game_button, "Cancel", 0.02f, true, true);
            sc.drawButton(confirm_end_game_button, "Confirm", 0.02f, true, true);
            sc.fill(Theme.theme.text_color);
            sc.textSize(sc.width * 0.05f);
            sc.text("Confirm end game?", Window.small_tile.cx(), Window.small_tile.y(0.2));
            sc.textSize(sc.width * 0.02f);
            sc.text("This action cannot be undone.", Window.small_tile.cx(), Window.small_tile.y(0.4));
            if(sc.trick_mode != 0) {
                int remaining_hands = sc.tricks.length - sc.trick_index;
                if(remaining_hands > 0) {
                    sc.text("There are currently " + remaining_hands + (remaining_hands > 1 ? " hands" : " hand") + " remaining.", Window.small_tile.cx(), Window.small_tile.y(0.5));
                }
            }
        }
    }

    @Override
    public void mousePressed(OhHellScoreboardV2 sc) {
        if(Window.current == Window.CONFIRM_END_GAME) {
            if(cancel_end_game_button.mouseInTile()) {
                Window.current = Window.NONE;
            } else if(confirm_end_game_button.mouseInTile()) {
                handleEndGame(sc);
            }
        } else {
            if(setup_button.mouseInTile()) {
                ScreenManager.pushScreen(SetupScreen.INSTANCE);
            } else if(edit_bids_button.mouseInTile()) {
                handleChangeBids(sc);
            } else if(statistics_button.mouseInTile()) {
                ScreenManager.pushScreen(StatisticsScreen.INSTANCE);
            } else if(end_game_button.mouseInTile()) {
                handleEndGame(sc);
            } else if(trump_suit_bounding_box.mouseInTile()) {
                handleTrumpButton(sc);
            }
        }
    }

    @Override
    public void keyTyped(OhHellScoreboardV2 sc) {
        sc.setTrumpFromKey(sc.key);
    }

    @Override
    public void init(OhHellScoreboardV2 sc) {
        setup_button = new Tile(0.02, 0.875, 0.14, 0.958);
        edit_bids_button = new Tile(0.15, 0.875, 0.27, 0.958);
        proceed_button = new Tile(0.28, 0.875, 0.40, 0.958);
        statistics_button = new Tile(0.41, 0.875, 0.53, 0.958);
        end_game_button = new Tile(0.54, 0.875, 0.66, 0.958);
        cancel_end_game_button = Tile.fromCoordinates(Window.small_tile.x(0.15), Window.small_tile.y(0.65), Window.small_tile.x(0.35), Window.small_tile.y(0.89));
        confirm_end_game_button = Tile.fromCoordinates(Window.small_tile.x(0.65), Window.small_tile.y(0.65), Window.small_tile.x(0.85), Window.small_tile.y(0.89));
        trump_suit_bounding_box = new Tile(0.92, 1 - sc.aspect_ratio * 0.08, 1, 1);
    }
}

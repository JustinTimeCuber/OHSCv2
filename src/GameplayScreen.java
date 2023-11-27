import processing.core.PImage;

public abstract class GameplayScreen implements Screen {

    int total_bid;
    int total_taken;
    Tile setup_button, edit_bids_button, proceed_button, statistics_button, end_game_button, trump_suit_bounding_box;
    static Tile[] game_tiles;

    void handleChangeBids(OhHellScoreboardV2 sc) {
        if(ScreenManager.currentScreen() instanceof BiddingScreen) {
            sc.displayError("Already changing bids");
        } else {
            ScreenManager.setScreen(BiddingScreen.INSTANCE);
        }
    }

    void handleEndGame(OhHellScoreboardV2 sc) {
        if(sc.enterRightClick()) {
            ScreenManager.setScreen(GameOverScreen.INSTANCE);
            sc.saveRecord();
        } else {
            sc.displayError("End game? Confirm with enter + right click");
        }
    }

    void handleTrumpButton(OhHellScoreboardV2 sc) {
        sc.trump_suit += sc.mouseButton == sc.LEFT ? 1 : -1;
        int max = Config.extra_trump_suits ? 6 : 4;
        if(sc.trump_suit < 0) {
            sc.trump_suit = max;
        }
        if(sc.trump_suit > max) {
            sc.trump_suit = 0;
        }
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
            if(ScreenManager.currentScreen() instanceof TakingScreen) {
                sc.textSize(game_tiles[0].w() * 0.04f);
                sc.text("Taken", game_tiles[i].mx() - game_tiles[i].w() * 0.125f, game_tiles[i].my() - game_tiles[0].w() * 0.167f);
                sc.textSize(game_tiles[0].w() * 0.1f);
                sc.text(p.taken, game_tiles[i].mx() - game_tiles[i].w() * 0.125f, game_tiles[i].my() - game_tiles[0].w() * 0.1f);
            }
        }
        sc.drawButton(setup_button, "Setup", 0.02f, true, true);
        sc.drawButton(statistics_button, "Statistics", 0.02f, true, true);
        sc.drawButton(end_game_button, "End Game", 0.02f, true, true);
        sc.textSize(sc.width * 0.01f);
        sc.fill(Theme.theme.text_color);
        sc.text("Deal", sc.width * 0.72f, sc.height * 0.87f);
        sc.text("Bid", sc.width * 0.8f, sc.height * 0.87f);
        sc.text("Taken", sc.width * 0.88f, sc.height * 0.87f);
        sc.text("Trump", sc.width * 0.96f, sc.height * 0.87f);
        sc.textSize(sc.width * 0.05f);
        sc.text(sc.trick_mode == 0 ? "--" : String.valueOf(sc.tricks[sc.trick_index]), sc.width * 0.72f, sc.height * 0.93f);
        sc.text(total_taken, sc.width * 0.88f, sc.height * 0.93f);
        PImage trump_icon = sc.trumpIcon();
        if(trump_icon != null) {
            sc.image(trump_icon, trump_suit_bounding_box.x() + trump_suit_bounding_box.w() * 0.25f, trump_suit_bounding_box.y() + trump_suit_bounding_box.h() * 0.25f, trump_suit_bounding_box.w() * 0.5f, trump_suit_bounding_box.h() * 0.5f);
        }
    }

    @Override
    public void mousePressed(OhHellScoreboardV2 sc) {
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
        trump_suit_bounding_box = new Tile(0.92, 1 - sc.aspect_ratio * 0.08, 1, 1);
    }
}

public class GameOverScreen implements Screen {
    static final GameOverScreen INSTANCE = new GameOverScreen();
    Tile restart_button, statistics_button;

    private GameOverScreen() {
    }

    @Override
    public void draw(OhHellScoreboardV2 sc) {
        sc.textAlign(sc.CENTER, sc.CENTER);
        for(int i = 0; i < Player.count(); i++) {
            Player p = Player.get(i);
            sc.fill(Theme.theme.background_color);
            GameplayScreen.game_tiles[i].rect(sc);
            sc.fill(p.display_color);
            sc.textSize(GameplayScreen.game_tiles[0].w() * 0.1f);
            sc.text(p.getName(i), GameplayScreen.game_tiles[i].cx(), GameplayScreen.game_tiles[i].y() + GameplayScreen.game_tiles[i].h() * 0.167f);
            sc.textSize(GameplayScreen.game_tiles[i].h() * 0.5f);
            sc.text(p.score, GameplayScreen.game_tiles[i].cx(), GameplayScreen.game_tiles[i].my() - GameplayScreen.game_tiles[i].h() * 0.43f);
        }
        sc.drawButton(statistics_button, "Statistics", 0.02f, true, true);
        sc.drawButton(restart_button, "Restart", 0.02f, true, true);
        sc.textSize(sc.width * 0.05f);
        sc.fill(Theme.theme.text_color);
        sc.text("Game Over", sc.width * 0.5f, sc.height * 0.92f);
    }

    @Override
    public void mousePressed(OhHellScoreboardV2 sc) {
        if(restart_button.mouseInTile()) {
            sc.setInitialValues();
        } else if(statistics_button.mouseInTile()) {
            ScreenManager.pushScreen(StatisticsScreen.INSTANCE);
        }
    }

    @Override
    public void init(OhHellScoreboardV2 sc) {
        statistics_button = new Tile(0.08, 0.875, 0.28, 0.958);
        restart_button = new Tile(0.72, 0.875, 0.92, 0.958);
    }
}

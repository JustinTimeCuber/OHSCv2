public class GameOverScreen implements Screen {
    static final GameOverScreen INSTANCE = new GameOverScreen();
    private GameOverScreen() {}
    public void draw(OhHellScoreboardV2 sc) {
        sc.textAlign(sc.CENTER, sc.CENTER);
        for(int i = 0; i < Player.count(); i++) {
            Player p = Player.get(i);
            sc.fill(Theme.theme.background_color);
            sc.rect(sc.game_tiles[i].x(), sc.game_tiles[i].y(), sc.game_tiles[i].w(), sc.game_tiles[i].h());
            sc.fill(p.display_color);
            sc.textSize(sc.game_tiles[0].w() * 0.1f);
            sc.text(p.name.equals("") ? ("Player " + (i + 1)) : p.name, sc.game_tiles[i].cx(), sc.game_tiles[i].y() + sc.game_tiles[i].h() * 0.167f);
            sc.textSize(sc.game_tiles[i].h() * 0.5f);
            sc.text(p.score, sc.game_tiles[i].cx(), sc.game_tiles[i].my() - sc.game_tiles[i].h() * 0.43f);
        }
        sc.drawButton(sc.statistics_button, "Statistics", 0.02f, true, true);
        sc.drawButton(sc.restart_button, "Restart", 0.02f, true, true);
        sc.textSize(sc.width * 0.05f);
        sc.fill(Theme.theme.text_color);
        sc.text("Game Over", sc.width * 0.5f, sc.height * 0.92f);
    }
}

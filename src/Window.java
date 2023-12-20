public enum Window {
    NONE,
    TRICKS,
    THEMES,
    SCORE_EDITOR,
    SETTINGS,
    CONFIRM_END_GAME;

    static Tile tile;
    static Tile small_tile;
    static Tile close_button;
    static Tile back_button;
    static Window current;

    static void init(OhHellScoreboardV2 sc) {
        tile = new Tile(0.167, 0.167, 0.833, 0.833);
        small_tile = new Tile(0.167, 0.333, 0.833, 0.667);
        close_button = Tile.fromCoordinates(tile.x(0.93), tile.y() + tile.w()*0.02f, tile.x(0.98), tile.y() + tile.w()*0.07f);
        back_button = Tile.fromCoordinates(tile.x(0.02), tile.y() + tile.w()*0.02f, tile.x(0.07), tile.y() + tile.w()*0.07f);
        current = NONE;
    }
}

public enum Window {
    NONE,
    TRICKS,
    THEMES;

    static Tile tile;
    static Tile close_button;
    static Window current;

    static void init(OhHellScoreboardV2 sc) {
        tile = new Tile(0.167, 0.167, 0.833, 0.833);
        close_button = new Tile(0.783, 0.167 * (1 + sc.aspect_ratio * 0.1), 0.817, 0.167 * (1 + sc.aspect_ratio * 0.3));
        current = NONE;
    }
}

public enum Screen {
    SETUP_TO_BIDDING,
    SETUP_TO_TAKING,
    BIDDING,
    TAKING,
    GAME_OVER,
    STATISTICS;

    boolean isSetup() {
        return this == SETUP_TO_BIDDING || this == SETUP_TO_TAKING;
    }

    public boolean usesGameTiles() {
        return this == BIDDING || this == TAKING || this == GAME_OVER;
    }
}

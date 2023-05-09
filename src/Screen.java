public enum Screen {
    SETUP_TO_BIDDING,
    SETUP_TO_TAKING,
    BIDDING,
    TAKING,
    GAME_OVER;
    boolean isSetup() {
        return this == SETUP_TO_BIDDING || this == SETUP_TO_TAKING;
    }
}

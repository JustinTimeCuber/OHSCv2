public interface Screen {
    void draw(OhHellScoreboardV2 sc);

    default void mousePressed(OhHellScoreboardV2 sc) {}

    default void init(OhHellScoreboardV2 sc) {}

    default void onLoad(OhHellScoreboardV2 sc) {}
}

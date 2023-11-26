public class StatisticsScreen implements Screen {
    static final StatisticsScreen INSTANCE = new StatisticsScreen();

    private StatisticsScreen() {
    }

    Tile statistics_header;
    Tile[] statistics_tiles;
    Tile open_save_button, return_button;
    PlayerSortMode sort_mode;
    boolean sort_reverse;
    int[] sorted_player_indices;
    final float[] vertical_lines = new float[]{0.25f, 0.4f, 0.55f, 0.7f, 0.85f};

    void highlightStatsHeader(PlayerSortMode mode, OhHellScoreboardV2 sc) {
        if(sort_mode == mode) {
            if(sort_reverse) {
                sc.fill(Theme.theme.grayed_text_color);
            } else {
                sc.fill(Theme.theme.highlight_text_color);
            }
        } else {
            sc.fill(Theme.theme.text_color);
        }
    }

    void handleHeaderClick(OhHellScoreboardV2 sc) {
        //TODO: Make this less scuffed
        PlayerSortMode mode = PlayerSortMode.NONE;
        if(sc.mouseX > statistics_header.x(vertical_lines[0]) && sc.mouseX < statistics_header.x(vertical_lines[1])) {
            mode = PlayerSortMode.SCORE;
        } else if(sc.mouseX > statistics_header.x(vertical_lines[1]) && sc.mouseX < statistics_header.x(vertical_lines[2])) {
            mode = PlayerSortMode.TAKEN;
        } else if(sc.mouseX > statistics_header.x(vertical_lines[2]) && sc.mouseX < statistics_header.x(vertical_lines[3])) {
            mode = PlayerSortMode.BID;
        } else if(sc.mouseX > statistics_header.x(vertical_lines[3]) && sc.mouseX < statistics_header.x(vertical_lines[4])) {
            mode = PlayerSortMode.BONUS;
        } else if(sc.mouseX > statistics_header.x(vertical_lines[4])) {
            mode = PlayerSortMode.SET;
        }
        if(sort_mode == mode && sort_mode != PlayerSortMode.NONE) {
            sort_reverse = !sort_reverse;
        } else {
            sort_reverse = false;
        }
        sortPlayers(mode, sort_reverse);
    }

    void sortPlayers(PlayerSortMode mode, boolean reverse) {
        sort_mode = mode;
        sort_reverse = reverse;
        sorted_player_indices = new int[Player.count()];
        for(int i = 0; i < sorted_player_indices.length; i++) {
            sorted_player_indices[i] = reverse ? sorted_player_indices.length - i - 1 : i;
        }
        if(mode == PlayerSortMode.NONE || sorted_player_indices.length < 2) {
            return;
        }
        for(int i = 0; i < sorted_player_indices.length - 1; i++) {
            for(int j = i + 1; j < sorted_player_indices.length; j++) {
                Player p1 = Player.get(sorted_player_indices[i]);
                Player p2 = Player.get(sorted_player_indices[j]);
                boolean shouldSwap = false;
                switch(mode) {
                    case SCORE -> shouldSwap = p1.score < p2.score;
                    case TAKEN -> shouldSwap = p1.total_taken < p2.total_taken;
                    case BID -> shouldSwap = p1.total_bid < p2.total_bid;
                    case BONUS -> shouldSwap = p1.bonuses < p2.bonuses;
                    case SET -> shouldSwap = p1.times_set < p2.times_set;
                }
                if(reverse) shouldSwap = !shouldSwap;
                if(shouldSwap) {
                    int temp = sorted_player_indices[i];
                    sorted_player_indices[i] = sorted_player_indices[j];
                    sorted_player_indices[j] = temp;
                }
            }
        }
    }

    void sortPlayers() {
        sortPlayers(sort_mode, sort_reverse);
    }

    @Override
    public void draw(OhHellScoreboardV2 sc) {
        sc.drawButton(open_save_button, "Open Save", 0.02f, true, true);
        sc.drawButton(return_button, "Return", 0.02f, true, true);
        sc.stroke(Theme.theme.line_color);
        sc.fill(Theme.theme.background_color);
        sc.rect(statistics_header.x(), statistics_header.y(), statistics_header.w(), statistics_header.h());
        for(float x : vertical_lines) {
            sc.line(statistics_header.x(x), statistics_header.y(), statistics_header.x(x), statistics_tiles[Player.count() - 1].my());
        }
        float[] text_positions = new float[]{0.01f, 0.325f, 0.475f, 0.625f, 0.775f, 0.925f};
        sc.fill(Theme.theme.text_color);
        sc.textSize(statistics_tiles[0].h() * 0.7f);
        sc.textAlign(sc.LEFT, sc.CENTER);
        sc.text("Name", statistics_header.x(text_positions[0]), statistics_header.cy());
        sc.textAlign(sc.CENTER, sc.CENTER);
        highlightStatsHeader(PlayerSortMode.SCORE, sc);
        sc.text("Score", statistics_header.x(text_positions[1]), statistics_header.cy());
        highlightStatsHeader(PlayerSortMode.TAKEN, sc);
        sc.text("Taken", statistics_header.x(text_positions[2]), statistics_header.cy());
        highlightStatsHeader(PlayerSortMode.BID, sc);
        sc.text("Bid", statistics_header.x(text_positions[3]), statistics_header.cy());
        highlightStatsHeader(PlayerSortMode.BONUS, sc);
        sc.text("Bonus", statistics_header.x(text_positions[4]), statistics_header.cy());
        highlightStatsHeader(PlayerSortMode.SET, sc);
        sc.text("Set", statistics_header.x(text_positions[5]), statistics_header.cy());
        for(int i = 0; i < Player.count(); i++) {
            Player p = Player.get(sorted_player_indices[i]);
            sc.noFill();
            sc.rect(statistics_tiles[i].x(), statistics_tiles[i].y(), statistics_tiles[i].w(), statistics_tiles[i].h());
            sc.fill(p.display_color);
            sc.textAlign(sc.LEFT, sc.CENTER);
            sc.text(p.getName(sorted_player_indices[i]), statistics_tiles[i].x(text_positions[0]), statistics_tiles[i].cy());
            sc.textAlign(sc.CENTER, sc.CENTER);
            sc.text(p.score, statistics_tiles[i].x(text_positions[1]), statistics_tiles[i].cy());
            sc.text(p.total_taken, statistics_tiles[i].x(text_positions[2]), statistics_tiles[i].cy());
            sc.text(p.total_bid, statistics_tiles[i].x(text_positions[3]), statistics_tiles[i].cy());
            sc.text(p.bonuses, statistics_tiles[i].x(text_positions[4]), statistics_tiles[i].cy());
            sc.text(p.times_set, statistics_tiles[i].x(text_positions[5]), statistics_tiles[i].cy());
        }
    }

    @Override
    public void mousePressed(OhHellScoreboardV2 sc) {
        if(return_button.mouseInTile()) {
            ScreenManager.popScreen();
        } else if(open_save_button.mouseInTile()) {
            sc.openLatestSave();
        } else if(statistics_header.mouseInTile()) {
            handleHeaderClick(sc);
        }
    }

    @Override
    public void init(OhHellScoreboardV2 sc) {
        sort_mode = PlayerSortMode.NONE;
        sort_reverse = false;
        open_save_button = new Tile(0.08, 0.875, 0.28, 0.958);
        return_button = new Tile(0.72, 0.875, 0.92, 0.958);
    }

    @Override
    public void onLoad(OhHellScoreboardV2 sc) {
        if(sort_mode == PlayerSortMode.NONE && !sort_reverse) {
            sortPlayers(PlayerSortMode.SCORE, false);
        } else {
            sortPlayers();
        }
    }
}

public class Tile {
    static OhHellScoreboardV2 sc;
    float x1;
    float y1;
    float x2;
    float y2;

    Tile(double x1, double y1, double x2, double y2) {
        this.x1 = (float) x1;
        this.y1 = (float) y1;
        this.x2 = (float) x2;
        this.y2 = (float) y2;
    }

    static Tile fromCoordinates(float x1, float y1, float x2, float y2) {
        return new Tile(x1 / sc.width, y1 / sc.height, x2 / sc.width, y2 / sc.height);
    }

    boolean mouseInTile() {
        return sc.mouseX > x1 * sc.width && sc.mouseX < x2 * sc.width && sc.mouseY > y1 * sc.height && sc.mouseY < y2 * sc.height;
    }

    /** The x coordinate of the left side of the tile. */
    float x() {
        return sc.width * x1;
    }

    /** The y coordinate of the top of the tile. */
    float y() {
        return sc.height * y1;
    }

    /** The x coordinate at a position within the tile, where 0 is left and 1 is right. */
    float x(double pos) {
        return (float) (sc.width * (x1 + pos * (x2 - x1)));
    }

    /** The y coordinate at a position within the tile, where 0 is top and 1 is bottom. */
    float y(double pos) {
        return (float) (sc.height * (y1 + pos * (y2 - y1)));
    }

    /** The width of the tile. */
    float w() {
        return sc.width * (x2 - x1);
    }

    /** The height of the tile. */
    float h() {
        return sc.height * (y2 - y1);
    }

    /** The x coordinate of the center of the tile. */
    float cx() {
        return x(0.5);
    }

    /** The y coordinate of the center of the tile. */
    float cy() {
        return y(0.5);
    }

    /** The x coordinate of the right side of the tile. */
    float mx() {
        return sc.width * x2;
    }

    /** The y coordinate of the bottom of the tile. */
    float my() {
        return sc.height * y2;
    }

    static void setPlayerCountBasedTiles() {
        int playerCount = sc.players.size();
        if(playerCount == 2) {
            sc.game_tiles = new Tile[]{
                    new Tile(0, 0, 0.5, 0.833),
                    new Tile(0.5, 0, 1, 0.833)
            };
        }
        if(playerCount == 3) {
            sc.game_tiles = new Tile[]{
                    new Tile(0, 0, 0.333, 0.833),
                    new Tile(0.333, 0, 0.667, 0.833),
                    new Tile(0.667, 0, 1, 0.833)
            };
        }
        if(playerCount == 4) {
            sc.game_tiles = new Tile[]{
                    new Tile(0, 0, 0.5, 0.417),
                    new Tile(0.5, 0, 1, 0.417),
                    new Tile(0, 0.417, 0.5, 0.833),
                    new Tile(0.5, 0.417, 1, 0.833)
            };
        }
        if(playerCount == 5) {
            sc.game_tiles = new Tile[]{
                    new Tile(0, 0, 0.333, 0.417),
                    new Tile(0.333, 0, 0.667, 0.417),
                    new Tile(0.667, 0, 1, 0.417),
                    new Tile(0, 0.417, 0.5, 0.833),
                    new Tile(0.5, 0.417, 1, 0.833)
            };
        }
        if(playerCount == 6) {
            sc.game_tiles = new Tile[]{
                    new Tile(0, 0, 0.333, 0.417),
                    new Tile(0.333, 0, 0.667, 0.417),
                    new Tile(0.667, 0, 1, 0.417),
                    new Tile(0, 0.417, 0.333, 0.833),
                    new Tile(0.333, 0.417, 0.667, 0.833),
                    new Tile(0.667, 0.417, 1, 0.833)
            };
        }
        if(playerCount == 7) {
            sc.game_tiles = new Tile[]{
                    new Tile(0, 0, 0.25, 0.417),
                    new Tile(0.25, 0, 0.5, 0.417),
                    new Tile(0.5, 0, 0.75, 0.417),
                    new Tile(0.75, 0, 1, 0.417),
                    new Tile(0, 0.417, 0.333, 0.833),
                    new Tile(0.333, 0.417, 0.667, 0.833),
                    new Tile(0.667, 0.417, 1, 0.833)
            };
        }
        if(playerCount == 8) {
            sc.game_tiles = new Tile[]{
                    new Tile(0, 0, 0.25, 0.417),
                    new Tile(0.25, 0, 0.5, 0.417),
                    new Tile(0.5, 0, 0.75, 0.417),
                    new Tile(0.75, 0, 1, 0.417),
                    new Tile(0, 0.417, 0.25, 0.833),
                    new Tile(0.25, 0.417, 0.5, 0.833),
                    new Tile(0.5, 0.417, 0.75, 0.833),
                    new Tile(0.75, 0.417, 1, 0.833)
            };
        }
        if(playerCount == 9) {
            sc.game_tiles = new Tile[]{
                    new Tile(0, 0, 0.333, 0.278),
                    new Tile(0.333, 0, 0.667, 0.278),
                    new Tile(0.667, 0, 1, 0.278),
                    new Tile(0, 0.278, 0.333, 0.556),
                    new Tile(0.333, 0.278, 0.667, 0.556),
                    new Tile(0.667, 0.278, 1, 0.556),
                    new Tile(0, 0.556, 0.333, 0.833),
                    new Tile(0.333, 0.556, 0.667, 0.833),
                    new Tile(0.667, 0.556, 1, 0.833)
            };
        }
        if(playerCount == 10) {
            sc.game_tiles = new Tile[]{
                    new Tile(0, 0, 0.25, 0.278),
                    new Tile(0.25, 0, 0.5, 0.278),
                    new Tile(0.5, 0, 0.75, 0.278),
                    new Tile(0.75, 0, 1, 0.278),
                    new Tile(0, 0.278, 0.333, 0.556),
                    new Tile(0.333, 0.278, 0.667, 0.556),
                    new Tile(0.667, 0.278, 1, 0.556),
                    new Tile(0, 0.556, 0.333, 0.833),
                    new Tile(0.333, 0.556, 0.667, 0.833),
                    new Tile(0.667, 0.556, 1, 0.833)
            };
        }
        double statsTileHeight = playerCount <= 7 ? 0.1 : 0.833/(playerCount + 1);
        sc.statistics_tiles = new Tile[playerCount];
        sc.statistics_header = new Tile(0, 0, 1, statsTileHeight);
        for(int i = 0; i < playerCount; i++) {
            sc.statistics_tiles[i] = new Tile(0, (i+1)*statsTileHeight, 1, (i+2)*statsTileHeight);
        }
    }
}


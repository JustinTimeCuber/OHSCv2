public class Tile {
  static OhHellScoreboardV2 sc;
  float x1;
  float y1;
  float x2;
  float y2;
  Tile(double x1, double y1, double x2, double y2) {
    this.x1 = (float)x1;
    this.y1 = (float)y1;
    this.x2 = (float)x2;
    this.y2 = (float)y2;
  }
  boolean mouseInTile() {
    return sc.mouseX > x1*sc.width && sc.mouseX < x2*sc.width && sc.mouseY > y1*sc.height && sc.mouseY < y2*sc.height;
  }
  float x() {
    return sc.width*x1;
  }
  float y() {
    return sc.height*y1;
  }
  float w() {
    return sc.width*(x2 - x1);
  }
  float h() {
    return sc.height*(y2 - y1);
  }
  float cx() {
    return 0.5f*sc.width*(x2 + x1);
  }
  float cy() {
    return 0.5f*sc.height*(y2 + y1);
  }
  float mx() {
    return sc.width*x2;
  }
  float my() {
    return sc.height*y2;
  }
  static void setGameTiles() {
    if(sc.players.size() == 2) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 0.5, 0.833),
              new Tile(0.5, 0, 1, 0.833)
      };
    }
    if(sc.players.size() == 3) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 0.333, 0.833),
              new Tile(0.333, 0, 0.667, 0.833),
              new Tile(0.667, 0, 1, 0.833)
      };
    }
    if(sc.players.size() == 4) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 0.5, 0.417),
              new Tile(0.5, 0, 1, 0.417),
              new Tile(0, 0.417, 0.5, 0.833),
              new Tile(0.5, 0.417, 1, 0.833)
      };
    }
    if(sc.players.size() == 5) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 0.333, 0.417),
              new Tile(0.333, 0, 0.667, 0.417),
              new Tile(0.667, 0, 1, 0.417),
              new Tile(0, 0.417, 0.5, 0.833),
              new Tile(0.5, 0.417, 1, 0.833)
      };
    }
    if(sc.players.size() == 6) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 0.333, 0.417),
              new Tile(0.333, 0, 0.667, 0.417),
              new Tile(0.667, 0, 1, 0.417),
              new Tile(0, 0.417, 0.333, 0.833),
              new Tile(0.333, 0.417, 0.667, 0.833),
              new Tile(0.667, 0.417, 1, 0.833)
      };
    }
    if(sc.players.size() == 7) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 0.25, 0.417),
              new Tile(0.25, 0, 0.5, 0.417),
              new Tile(0.5, 0, 0.75, 0.417),
              new Tile(0.75, 0, 1, 0.417),
              new Tile(0, 0.417, 0.333, 0.833),
              new Tile(0.333, 0.417, 0.667, 0.833),
              new Tile(0.667, 0.417, 1, 0.833)
      };
    }
    if(sc.players.size() == 8) {
      sc.game_tiles = new Tile[] {
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
    if(sc.players.size() == 9) {
      sc.game_tiles = new Tile[] {
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
    if(sc.players.size() == 10) {
      sc.game_tiles = new Tile[] {
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
  }
}


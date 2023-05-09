import processing.core.PApplet;

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
              new Tile(0, 0, 1./2, 5./6),
              new Tile(1./2, 0, 1, 5./6)
      };
    }
    if(sc.players.size() == 3) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 1./3, 5./6),
              new Tile(1./3, 0, 2./3, 5./6),
              new Tile(2./3, 0, 1, 5./6)
      };
    }
    if(sc.players.size() == 4) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 1./2, 5./12),
              new Tile(1./2, 0, 1, 5./12),
              new Tile(0, 5./12, 1./2, 5./6),
              new Tile(1./2, 5./12, 1, 5./6)
      };
    }
    if(sc.players.size() == 5) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 1./3, 5./12),
              new Tile(1./3, 0, 2./3, 5./12),
              new Tile(2./3, 0, 1, 5./12),
              new Tile(0, 5./12, 1./2, 5./6),
              new Tile(1./2, 5./12, 1, 5./6)
      };
    }
    if(sc.players.size() == 6) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 1./3, 5./12),
              new Tile(1./3, 0, 2./3, 5./12),
              new Tile(2./3, 0, 1, 5./12),
              new Tile(0, 5./12, 1./3, 5./6),
              new Tile(1./3, 5./12, 2./3, 5./6),
              new Tile(2./3, 5./12, 1, 5./6)
      };
    }
    if(sc.players.size() == 7) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 1./4, 5./12),
              new Tile(1./4, 0, 1./2, 5./12),
              new Tile(1./2, 0, 3./4, 5./12),
              new Tile(3./4, 0, 1, 5./12),
              new Tile(0, 5./12, 1./3, 5./6),
              new Tile(1./3, 5./12, 2./3, 5./6),
              new Tile(2./3, 5./12, 1, 5./6)
      };
    }
    if(sc.players.size() == 8) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 1./4, 5./12),
              new Tile(1./4, 0, 1./2, 5./12),
              new Tile(1./2, 0, 3./4, 5./12),
              new Tile(3./4, 0, 1, 5./12),
              new Tile(0, 5./12, 1./4, 5./6),
              new Tile(1./4, 5./12, 1./2, 5./6),
              new Tile(1./2, 5./12, 3./4, 5./6),
              new Tile(3./4, 5./12, 1, 5./6)
      };
    }
    if(sc.players.size() == 9) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 1./3, 5./18),
              new Tile(1./3, 0, 2./3, 5./18),
              new Tile(2./3, 0, 1, 5./18),
              new Tile(0, 5./18, 1./3, 5./9),
              new Tile(1./3, 5./18, 2./3, 5./9),
              new Tile(2./3, 5./18, 1, 5./9),
              new Tile(0, 5./9, 1./3, 5./6),
              new Tile(1./3, 5./9, 2./3, 5./6),
              new Tile(2./3, 5./9, 1, 5./6)
      };
    }
    if(sc.players.size() == 10) {
      sc.game_tiles = new Tile[] {
              new Tile(0, 0, 1./4, 5./18),
              new Tile(1./4, 0, 1./2, 5./18),
              new Tile(1./2, 0, 3./4, 5./18),
              new Tile(3./4, 0, 1, 5./18),
              new Tile(0, 5./18, 1./3, 5./9),
              new Tile(1./3, 5./18, 2./3, 5./9),
              new Tile(2./3, 5./18, 1, 5./9),
              new Tile(0, 5./9, 1./3, 5./6),
              new Tile(1./3, 5./9, 2./3, 5./6),
              new Tile(2./3, 5./9, 1, 5./6)
      };
    }
  }
}


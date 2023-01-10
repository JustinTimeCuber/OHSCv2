class Tile {
  float x1;
  float y1;
  float x2;
  float y2;
  Tile(float x1, float y1, float x2, float y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }
  boolean mouseInTile() {
    return mouseX > x1*width && mouseX < x2*width && mouseY > y1*height && mouseY < y2*height;
  }
  float x() {
    return width*x1;
  }
  float y() {
    return height*y1;
  }
  float w() {
    return width*(x2 - x1);
  }
  float h() {
    return height*(y2 - y1);
  }
  float cx() {
    return 0.5*width*(x2 + x1);
  }
  float cy() {
    return 0.5*height*(y2 + y1);
  }
  float mx() {
    return width*x2;
  }
  float my() {
    return height*y2;
  }
}
void setGameTiles() {
  if(players.size() == 2) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./2, 5./6),
      new Tile(1./2, 0, 1, 5./6)
    };
  }
  if(players.size() == 3) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./3, 5./6),
      new Tile(1./3, 0, 2./3, 5./6),
      new Tile(2./3, 0, 1, 5./6)
    };
  }
  if(players.size() == 4) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./2, 5./12),
      new Tile(1./2, 0, 1, 5./12),
      new Tile(0, 5./12, 1./2, 5./6),
      new Tile(1./2, 5./12, 1, 5./6)
    };
  }
  if(players.size() == 5) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./3, 5./12),
      new Tile(1./3, 0, 2./3, 5./12),
      new Tile(2./3, 0, 1, 5./12),
      new Tile(0, 5./12, 1./2, 5./6),
      new Tile(1./2, 5./12, 1, 5./6)
    };
  }
  if(players.size() == 6) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./3, 5./12),
      new Tile(1./3, 0, 2./3, 5./12),
      new Tile(2./3, 0, 1, 5./12),
      new Tile(0, 5./12, 1./3, 5./6),
      new Tile(1./3, 5./12, 2./3, 5./6),
      new Tile(2./3, 5./12, 1, 5./6)
    };
  }
  if(players.size() == 7) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./4, 5./12),
      new Tile(1./4, 0, 1./2, 5./12),
      new Tile(1./2, 0, 3./4, 5./12),
      new Tile(3./4, 0, 1, 5./12),
      new Tile(0, 5./12, 1./3, 5./6),
      new Tile(1./3, 5./12, 2./3, 5./6),
      new Tile(2./3, 5./12, 1, 5./6)
    };
  }
  if(players.size() == 8) {
    game_tiles = new Tile[] {
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
  if(players.size() == 9) {
    game_tiles = new Tile[] {
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
  if(players.size() == 10) {
    game_tiles = new Tile[] {
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

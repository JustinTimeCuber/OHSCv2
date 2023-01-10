class Theme {
  color[] player_colors;
  color background_color;
  color line_color;
  Theme(color[] pc, color bc, color lc) {
    player_colors = pc;
    background_color = bc;
    line_color = lc;
  }
  color getPlayerColor(int index) {
    if(index >= player_colors.length) {
      return color(255);
    } else {
      return player_colors[index];
    }
  }
}

Theme main_theme = new Theme(
  new color[] {
    color(255, 0, 0),
    color(0, 255, 0),
    color(127, 127, 255),
    color(255, 255, 0),
    color(0, 255, 255),
    color(255, 0, 191),
    color(255, 255, 255),
    color(255, 127, 0),
    color(127, 127, 127),
    color(191, 0, 255)
  }, color(0), color(255));

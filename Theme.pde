class Theme {
  color[] player_colors = new color[MAX_PLAYERS];
  color background_color = 0;
  color line_color = 0;
  Theme(color bc, color lc, color[] pc) {
    player_colors = pc;
    background_color = bc;
    line_color = lc;
  }
  Theme(String filename) {
    try {
      String[] lines = loadStrings(filename);
      if(lines.length < MAX_PLAYERS + 2) {
        System.err.println("Could not load theme \"" + filename + "\": expected " + (MAX_PLAYERS + 2) + " lines, only found " + lines.length);
      } else {
        background_color = colorFromLine(lines[0]);
        line_color = colorFromLine(lines[1]);
        for(int i = 0; i < MAX_PLAYERS; i++) {
          player_colors[i] = colorFromLine(lines[i + 2]);
        }
      }
    } catch(Exception e) {
      System.err.println("Exception loading theme \"" + filename + "\": " + e.toString());
    }
  }
  color colorFromLine(String line) {
    String[] parts = line.split(" ");
    try {
      return color(parseInt(parts[1]), parseInt(parts[2]), parseInt(parts[3]));
    } catch(Exception e) {
      System.err.println("Exception parsing theme line \"" + line + "\": " + e.toString());
      return color(255);
    }
  }
  color getPlayerColor(int index) {
    if(index >= player_colors.length) {
      return color(255);
    } else {
      return player_colors[index];
    }
  }
}
Theme theme_default;
void loadThemes() {
  theme_default = new Theme("themes" + FILE_SEPARATOR + "default.ohsctheme");
}

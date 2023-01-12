class Theme {
  color[] player_colors = new color[MAX_PLAYERS];
  color background_color = 0;
  color line_color = 0;
  String file = "";
  String name = "Unnamed";
  Theme(color bc, color lc, color[] pc, String f, String n) {
    player_colors = pc;
    background_color = bc;
    line_color = lc;
    file = f;
    name = n;
  }
  Theme(String directory, String filename, String n) {
    try {
      name = n;
      String[] lines = loadStrings(directory + FILE_SEPARATOR + filename);
      if(lines.length < MAX_PLAYERS + 2) {
        System.err.println("Could not load theme \"" + filename + "\": expected " + (MAX_PLAYERS + 2) + " lines, only found " + lines.length);
      } else {
        background_color = colorFromLine(lines[0]);
        line_color = colorFromLine(lines[1]);
        for(int i = 0; i < MAX_PLAYERS; i++) {
          player_colors[i] = colorFromLine(lines[i + 2]);
        }
      }
      file = filename;
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
ArrayList<Theme> themes;
int theme_index = 0;
Theme theme;
void loadThemes() {
  themes = new ArrayList<Theme>();
  try {
    String[] themes_list = loadStrings("themes" + FILE_SEPARATOR + "themes.txt");
    for(int i = 0; i < themes_list.length; i++) {
      String file = themes_list[i].split(":")[0];
      String name = themes_list[i].split(":")[1];
      themes.add(new Theme("themes", file, name));
    }
  } catch(Exception e) {
    System.err.println("Could not read themes.txt: " + e.toString());
  }
  theme = themes.get(theme_index);
}

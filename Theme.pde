class Theme {
  color[] player_colors = new color[MAX_PLAYERS];
  color background_color = 0;
  color popup_background_color = 0;
  color line_color = 0;
  color text_color = 0;
  color grayed_text_color = 0;
  color error_text_color = 0;
  color button_click_color = 0;
  color button_hover_color = 0;
  color underbid_color = 0;
  color overbid_color = 0;
  String file = "";
  String name = "Unnamed";
  Theme(color bc, color pbc, color lc, color tc, color gtc, color etc, color bcc, color bhc, color ubc, color obc, color[] pc, String f, String n) {
    background_color = bc;
    popup_background_color = pbc;
    line_color = lc;
    text_color = tc;
    grayed_text_color = gtc;
    error_text_color = etc;
    button_click_color = bcc;
    button_hover_color = bhc;
    underbid_color = ubc;
    overbid_color = obc;
    player_colors = pc;
    file = f;
    name = n;
  }
  // TODO: Make this use label parsing instead of hardcoded line indices
  Theme(String directory, String filename, String n) {
    try {
      name = n;
      String[] lines = loadStrings(directory + FILE_SEPARATOR + filename);
      final int DELTA = 10;
      if(lines.length < MAX_PLAYERS + DELTA) {
        System.err.println("Could not load theme \"" + filename + "\": expected " + (MAX_PLAYERS + DELTA) + " lines, only found " + lines.length);
      } else {
        background_color = colorFromLine(lines[0]);
        popup_background_color = colorFromLine(lines[1]);
        line_color = colorFromLine(lines[2]);
        text_color = colorFromLine(lines[3]);
        grayed_text_color = colorFromLine(lines[4]);
        error_text_color = colorFromLine(lines[5]);
        button_click_color = colorFromLine(lines[6]);
        button_hover_color = colorFromLine(lines[7]);
        underbid_color = colorFromLine(lines[8]);
        overbid_color = colorFromLine(lines[9]);
        for(int i = 0; i < MAX_PLAYERS; i++) {
          player_colors[i] = colorFromLine(lines[i + DELTA]);
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
String theme_file;
Theme theme;
void loadThemes() {
  if(themes == null) {
  themes = new ArrayList<Theme>();
    try {
      String[] themes_list = loadStrings("themes" + FILE_SEPARATOR + "themes.txt");
      for(int i = 0; i < themes_list.length; i++) {
        String file = themes_list[i].split(":")[0];
        String name = themes_list[i].split(":")[1];
        themes.add(new Theme("themes", file, name));
        if(file.equals(theme_file)) {
          theme_index = i;
        }
      }
    } catch(Exception e) {
      System.err.println("Could not read themes.txt: " + e.toString());
    }
  } else {
    for(int i = 0; i < themes.size(); i++) {
      Theme t = themes.get(i);
      if(t.file.equals(theme_file)) {
        theme_index = i;
      }
    }
  }
  theme = themes.get(theme_index);
  println(theme_index);
}

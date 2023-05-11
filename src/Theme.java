import java.util.ArrayList;

class Theme {
  static OhHellScoreboardV2 sc;
  final int[] player_colors = new int[sc.MAX_PLAYERS];
  int background_color = 0;
  int popup_background_color = 0;
  int line_color = 0;
  int text_color = 0;
  int grayed_text_color = 0;
  int error_text_color = 0;
  int button_click_color = 0;
  int button_hover_color = 0;
  int underbid_color = 0;
  int overbid_color = 0;
  String file = "";
  String name = "Unnamed";
  Theme(String directory, String filename, String n) {
    try {
      name = n;
      String[] lines = sc.loadStrings(directory + sc.FILE_SEPARATOR + filename);
      final int DELTA = 10;
      if(lines.length < sc.MAX_PLAYERS + DELTA) {
        System.err.println("Could not load theme \"" + filename + "\": expected " + (sc.MAX_PLAYERS + DELTA) + " lines, only found " + lines.length);
      } else {
        for (String line : lines) {
          String label = line.split(" ")[0];
          if (label.equals("background")) background_color = colorFromLine(line);
          else if (label.equals("popupbackground")) popup_background_color = colorFromLine(line);
          else if (label.equals("line")) line_color = colorFromLine(line);
          else if (label.equals("text")) text_color = colorFromLine(line);
          else if (label.equals("grayedtext")) grayed_text_color = colorFromLine(line);
          else if (label.equals("errortext")) error_text_color = colorFromLine(line);
          else if (label.equals("buttonclick")) button_click_color = colorFromLine(line);
          else if (label.equals("buttonhover")) button_hover_color = colorFromLine(line);
          else if (label.equals("underbid")) underbid_color = colorFromLine(line);
          else if (label.equals("overbid")) overbid_color = colorFromLine(line);
          else if (label.charAt(0) == 'p') {
            int player = Integer.parseInt(label.substring(1));
            player--;
            player_colors[player] = colorFromLine(line);
          } else {
            System.err.println("Unknown label in theme " + filename + ": " + label);
          }
        }
      }
      file = filename;
    } catch(Exception e) {
      System.err.println("Exception loading theme \"" + filename + "\": " + e);
    }
  }
  int colorFromLine(String line) {
    String[] parts = line.split(" ");
    try {
      return sc.color(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
    } catch(Exception e) {
      System.err.println("Exception parsing theme line \"" + line + "\": " + e);
      return sc.color(255);
    }
  }
  int getPlayerColor(int index) {
    if(index >= player_colors.length) {
      return sc.color(255);
    } else {
      return player_colors[index];
    }
  }
  static ArrayList<Theme> themes;
  static int theme_index = 0;
  static String theme_file;
  static Theme theme;
  static void loadThemes() {
    if(themes == null) {
      themes = new ArrayList<>();
      try {
        String[] themes_list = sc.loadStrings("themes" + sc.FILE_SEPARATOR + "themes.txt");
        for(int i = 0; i < themes_list.length; i++) {
          String file = themes_list[i].split(":")[0];
          String name = themes_list[i].split(":")[1];
          themes.add(new Theme("themes", file, name));
          if(file.equals(theme_file)) {
            theme_index = i;
          }
        }
      } catch(Exception e) {
        System.err.println("Could not read themes.txt: " + e);
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
  }
  static void setTheme(int index) {
    theme_index = index;
    if(theme_index >= themes.size()) {
      theme_index = 0;
    }
    theme = themes.get(theme_index);
    theme_file = theme.file;
    sc.updatePlayers(false);
  }
}


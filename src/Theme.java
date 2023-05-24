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
  int highlight_text_color = 0;
  int button_click_color = 0;
  int button_hover_color = 0;
  int underbid_color = 0;
  int overbid_color = 0;
  String directory = "";
  String file = "";
  String name = "Unnamed";
  Theme(String directory, String filename, String n) {
    try {
      name = n;
      String[] lines = sc.loadStrings(directory + filename);
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
          else if (label.equals("highlighttext")) highlight_text_color = colorFromLine(line);
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
      this.directory = directory;
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
  static String theme_directory;
  static Theme theme;
  static void loadThemes() {
    if(themes == null) {
      themes = new ArrayList<>();
      try {
        String[] themes_list = sc.loadStrings(sc.DATA_PATH + "themes" + sc.FILE_SEPARATOR + "themes.txt");
        if(themes_list == null) {
          themes_list = sc.loadStrings("themes/themes.txt");
        }
        for(int i = 0; i < themes_list.length; i++) {
          String file = themes_list[i].split(":")[0];
          String name = themes_list[i].split(":")[1];
          String dir = sc.DATA_PATH + "themes" + sc.FILE_SEPARATOR;
          if(sc.loadStrings(dir + file) == null) {
            dir = "themes/";
          }
          themes.add(new Theme(dir, file, name));
          if(file.equals(theme_file) && theme_directory.equals(dir)) {
            theme_index = i;
          }
        }
      } catch(Exception e) {
        System.err.println("Could not read themes.txt: " + e);
      }
      String[] themes_list_ext = null;
      try {
        themes_list_ext = sc.loadStrings(sc.DATA_PATH + "themes" + sc.FILE_SEPARATOR + "external_themes.txt");
      } catch(Exception e) {
        System.err.println("Could not read external_themes.txt: " + e);
      }
      if(themes_list_ext == null) {
        themes_list_ext = new String[]{"custom.ohsctheme:Custom Theme"};
        sc.saveStrings(sc.DATA_PATH + "themes" + sc.FILE_SEPARATOR + "external_themes.txt", themes_list_ext);
        String[] custom = sc.loadStrings("themes/custom/custom.ohsctheme");
        sc.saveStrings(sc.DATA_PATH + "themes" + sc.FILE_SEPARATOR + "custom.ohsctheme", custom);
      }
      int offset = themes.size();
      for(int i = 0; i < themes_list_ext.length; i++) {
        String file = themes_list_ext[i].split(":")[0];
        String name = themes_list_ext[i].split(":")[1];
        themes.add(new Theme(sc.DATA_PATH + "themes" + sc.FILE_SEPARATOR, file, name));
        if(file.equals(theme_file) && theme_directory.equals(sc.DATA_PATH + "themes" + sc.FILE_SEPARATOR)) {
          theme_index = i + offset;
        }
      }
    } else {
      for(int i = 0; i < themes.size(); i++) {
        Theme t = themes.get(i);
        if(t.file.equals(theme_file) && t.directory.equals(theme_directory)) {
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
    theme_directory = theme.directory;
    sc.updatePlayers(false);
  }
}


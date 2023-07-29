import java.util.ArrayList;

public class StateIO {

    static void saveState(String filename, OhHellScoreboardV2 sc) {
        if(sc.debug) {
            System.out.println("Saving state...");
        }
        ArrayList<String> state = new ArrayList<>();
        state.add("selected_player:" + sc.selected_player);
        state.add("editing_name:" + sc.editing_name);
        state.add("current_window:" + sc.current_window);
        state.add("current_screen:" + sc.current_screen);
        state.add("suits:" + sc.suits);
        state.add("cards_per_suit:" + sc.cards_per_suit);
        state.add("trick_mode:" + sc.trick_mode);
        state.add("trick_index:" + sc.trick_index);
        state.add("trump_suit:" + sc.trump_suit);
        state.add("hands_played:" + sc.hands_played);
        state.add("theme_file:" + Theme.theme.file);
        state.add("theme_directory:" + Theme.theme.directory);
        state.add("last_save:" + sc.last_save);
        state.add("sort_mode:" + StatisticsScreen.INSTANCE.sort_mode);
        state.add("sort_reverse:" + StatisticsScreen.INSTANCE.sort_reverse);
        for(Player p : Player.players) {
            state.add("player:" + p.toString());
        }
        sc.saveStrings(filename + ".ohsc", state.toArray(new String[]{}));
        Logger.save(filename + ".log", sc);
    }

    static void loadState(String filename, OhHellScoreboardV2 sc) {
        String[] state = sc.loadStrings(filename + ".ohsc");
        Logger.read(filename + ".log", sc);
        Player.players = new ArrayList<>();
        for(String s : state) {
            String label = s.split(":")[0];
            String value = s.substring(label.length() + 1);
            switch(label) {
                case "selected_player" -> sc.selected_player = Integer.parseInt(value);
                case "editing_name" -> sc.editing_name = Boolean.parseBoolean(value);
                case "current_window" -> sc.current_window = Window.valueOf(value);
                case "current_screen" -> sc.current_screen = Screen.valueOf(value);
                case "suits" -> sc.suits = Integer.parseInt(value);
                case "cards_per_suit" -> sc.cards_per_suit = Integer.parseInt(value);
                case "trick_mode" -> sc.trick_mode = Integer.parseInt(value);
                case "trick_index" -> sc.trick_index = Integer.parseInt(value);
                case "trump_suit" -> sc.trump_suit = Integer.parseInt(value);
                case "hands_played" -> sc.hands_played = Integer.parseInt(value);
                case "theme_file" -> Theme.theme_file = value;
                case "theme_directory" -> Theme.theme_directory = value;
                case "last_save" -> sc.last_save = value;
                case "sort_mode" -> StatisticsScreen.INSTANCE.sort_mode = PlayerSortMode.valueOf(value);
                case "sort_reverse" -> StatisticsScreen.INSTANCE.sort_reverse = Boolean.parseBoolean(value);
                case "player" -> new Player().parse(value);
                default -> System.err.println("Unrecognized label: " + label);
            }
        }
        Config.loadConfig(sc);
        Theme.loadThemes();
        sc.updatePlayers(false);
    }
}
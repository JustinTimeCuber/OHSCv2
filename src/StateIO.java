import java.util.ArrayList;

public class StateIO {
    static OhHellScoreboardV2 sc;

    static void saveState(String filename) {
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
        for(Player p : sc.players) {
            state.add("player:" + p.toString());
        }
        sc.saveStrings(filename + ".ohsc", state.toArray(new String[]{}));
        Logger.save(filename + ".log");
    }

    static void loadState(String filename) {
        String[] state = sc.loadStrings(filename + ".ohsc");
        Logger.read(filename + ".log");
        sc.players = new ArrayList<>();
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
                case "player" -> sc.players.add(new Player().parse(value));
                default -> System.err.println("Unrecognized label: " + label);
            }
        }
        Theme.loadThemes();
        sc.updatePlayers(false);
    }
}
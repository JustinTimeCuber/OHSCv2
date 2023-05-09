import java.util.ArrayList;

public class StateIO {
    static OhHellScoreboardV2 sc;
    static void saveState(String filename) {
        ArrayList<String> state = new ArrayList<String>();
        state.add("selected_player:" + sc.selected_player);
        state.add("error_message:" + sc.error_message);
        state.add("error_frames:" + sc.error_frames);
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
        for (Player p : sc.players) {
            state.add("player:" + p.toString());
        }
        sc.saveStrings(filename + ".ohsc", state.toArray(new String[state.size()]));
        Logger.save(filename + ".log");
    }

    static void loadState(String filename) {
        String[] state = sc.loadStrings(filename + ".ohsc");
        Logger.read(filename + ".log");
        sc.players = new ArrayList<Player>();
        for (int i = 0; i < state.length; i++) {
            String label = state[i].split(":")[0];
            String value = state[i].substring(label.length() + 1);
            if (label.equals("selected_player")) sc.selected_player = sc.parseInt(value);
            else if (label.equals("error_message")) sc.error_message = value;
            else if (label.equals("error_frames")) sc.error_frames = sc.parseInt(value);
            else if (label.equals("editing_name")) sc.editing_name = sc.parseBoolean(value);
            else if (label.equals("current_window")) sc.current_window = Window.valueOf(value);
            else if (label.equals("current_screen")) sc.current_screen = Screen.valueOf(value);
            else if (label.equals("suits")) sc.suits = sc.parseInt(value);
            else if (label.equals("cards_per_suit")) sc.cards_per_suit = sc.parseInt(value);
            else if (label.equals("trick_mode")) sc.trick_mode = sc.parseInt(value);
            else if (label.equals("trick_index")) sc.trick_index = sc.parseInt(value);
            else if (label.equals("trump_suit")) sc.trump_suit = sc.parseInt(value);
            else if (label.equals("hands_played")) sc.hands_played = sc.parseInt(value);
            else if (label.equals("theme_file")) Theme.theme_file = value;
            else if (label.equals("player")) sc.players.add(new Player("").parse(value));
            else System.err.println("Unrecognized label: " + label);
        }
        Theme.loadThemes();
        sc.updatePlayers(false);
    }
}
import java.util.ArrayList;
import java.util.Arrays;

public class StateIO {

    static void saveState(String filename, OhHellScoreboardV2 sc) {
        if(sc.debug) {
            System.out.println("Saving state...");
        }
        ArrayList<String> state = new ArrayList<>();
        state.add("selected_player:" + SetupScreen.INSTANCE.selected_player);
        state.add("editing_name:" + SetupScreen.INSTANCE.editing_name);
        state.add("current_window:" + Window.current);
        state.add("current_screen_stack:" + ScreenManager.stateToString());
        state.add("suits:" + sc.suits);
        state.add("cards_per_suit:" + sc.cards_per_suit);
        state.add("trick_mode:" + sc.trick_mode);
        if(sc.trick_mode == 6) {
            StringBuilder custom = new StringBuilder();
            for(int i = 0; i < sc.tricks.length; i++) {
                custom.append(sc.tricks[i]);
                custom.append(",");
            }
            state.add("custom_tricks:" + custom);
        }
        state.add("trick_index:" + sc.trick_index);
        state.add("trump_suit:" + (sc.trump_suit == null ? "" : sc.trump_suit.name));
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
                case "selected_player" -> SetupScreen.INSTANCE.selected_player = Integer.parseInt(value);
                case "editing_name" -> SetupScreen.INSTANCE.editing_name = Boolean.parseBoolean(value);
                case "current_window" -> Window.current = Window.valueOf(value);
                case "current_screen_stack" -> ScreenManager.setStateFromString(value);
                case "suits" -> sc.suits = Integer.parseInt(value);
                case "cards_per_suit" -> sc.cards_per_suit = Integer.parseInt(value);
                case "trick_mode" -> sc.trick_mode = Integer.parseInt(value);
                case "custom_tricks" -> {
                    if(sc.trick_mode == 6) {
                        ArrayList<Integer> sequence = new ArrayList<>();
                        for(String str : value.split(",")) {
                            sequence.add(Integer.parseInt(str));
                        }
                        sc.tricks = sequence.stream().mapToInt(Integer::intValue).toArray();
                    }
                }
                case "trick_index" -> sc.trick_index = Integer.parseInt(value);
                case "trump_suit" -> sc.trump_suit = Suit.getByName(value);
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
        if(sc.trick_mode == 6 && sc.tricks.length == 0) {
            sc.trick_mode = 0;
        }
        Config.loadConfig(sc);
        Theme.loadThemes();
        sc.updatePlayers(false);
    }
}
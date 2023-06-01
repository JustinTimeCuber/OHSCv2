public class Config {
    static OhHellScoreboardV2 sc;
    static int points_per_trick = 1;
    static int points_bonus = 10;
    static int points_set = -10;
    static boolean underbid_set = true;
    static boolean overbid_set = false;
    static boolean set_penalty_scales = false;
    static boolean set_prevents_trick_points = true;
    static boolean negative_scores_allowed = true;
    static boolean extra_trump_suits = false;

    static void loadConfig() {
        try {
            String[] config = sc.loadStrings(sc.DATA_PATH + "config.txt");
            if(config == null) {
                config = sc.loadStrings("config.txt");
                sc.saveStrings(sc.DATA_PATH + "config.txt", config);
            }
            for(String s : config) {
                if(s.isBlank() || s.trim().startsWith("#")) {
                    continue;
                }
                String label = s.split(":")[0];
                String value = s.substring(label.length() + 1).trim();
                switch(label) {
                    case "points_per_trick" -> points_per_trick = Integer.parseInt(value);
                    case "points_bonus" -> points_bonus = Integer.parseInt(value);
                    case "points_set" -> points_set = Integer.parseInt(value);
                    case "underbid_set" -> underbid_set = Boolean.parseBoolean(value);
                    case "overbid_set" -> overbid_set = Boolean.parseBoolean(value);
                    case "set_penalty_scales" -> set_penalty_scales = Boolean.parseBoolean(value);
                    case "set_prevents_trick_points" -> set_prevents_trick_points = Boolean.parseBoolean(value);
                    case "negative_scores_allowed" -> negative_scores_allowed = Boolean.parseBoolean(value);
                    case "extra_trump_suits" -> extra_trump_suits = Boolean.parseBoolean(value);
                    default -> System.err.println("Unrecognized label: " + label);
                }
            }
        } catch(Exception e) {
            System.err.println("Could not read config.txt: " + e);
        }
    }
}

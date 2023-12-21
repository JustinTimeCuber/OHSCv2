public class Config {
    static int points_per_trick = 1;
    static int points_bonus = 10;
    static int points_set = -10;
    static boolean underbid_set = true;
    static boolean overbid_set = false;
    static boolean set_penalty_scales = false;
    static boolean set_prevents_trick_points = true;
    static boolean negative_scores_allowed = true;
    static UpdateMode update_mode = UpdateMode.STABLE;

    static void loadConfig(OhHellScoreboardV2 sc) {
        try {
            String[] config = sc.loadStrings(sc.DATA_PATH + "config.txt");
            if(config == null) {
                config = sc.loadStrings("default_config.txt");
                sc.saveStrings(sc.DATA_PATH + "config.txt", config);
            }
            boolean[] found = new boolean[9];
            for(String s : config) {
                if(s.isBlank() || s.trim().startsWith("#")) {
                    continue;
                }
                String label = s.split(":")[0];
                String value = s.substring(label.length() + 1).trim();
                switch(label) {
                    case "points_per_trick" -> {
                        points_per_trick = Integer.parseInt(value);
                        found[0] = true;
                    }
                    case "points_bonus" -> {
                        points_bonus = Integer.parseInt(value);
                        found[1] = true;
                    }
                    case "points_set" -> {
                        points_set = Integer.parseInt(value);
                        found[2] = true;
                    }
                    case "underbid_set" -> {
                        underbid_set = Boolean.parseBoolean(value);
                        found[3] = true;
                    }
                    case "overbid_set" -> {
                        overbid_set = Boolean.parseBoolean(value);
                        found[4] = true;
                    }
                    case "set_penalty_scales" -> {
                        set_penalty_scales = Boolean.parseBoolean(value);
                        found[5] = true;
                    }
                    case "set_prevents_trick_points" -> {
                        set_prevents_trick_points = Boolean.parseBoolean(value);
                        found[6] = true;
                    }
                    case "negative_scores_allowed" -> {
                        negative_scores_allowed = Boolean.parseBoolean(value);
                        found[7] = true;
                    }
                    case "update_mode" -> {
                        update_mode = UpdateMode.valueOf(value);
                        found[8] = true;
                    }
                    default -> {
                        System.err.println("Unrecognized label: " + label);
                        System.err.println("Attempting to fix config.txt...");
                        fixFile(sc);
                    }
                }
            }
            for(boolean b : found) {
                if(!b) {
                    fixFile(sc);
                }
            }
        } catch(Exception e) {
            System.err.println("Could not read config.txt: " + e);
            fixFile(sc);
        }
    }
    static void fixFile(OhHellScoreboardV2 sc) {
        String[] config = sc.loadStrings("default_config.txt");
        for(int i = 0; i < config.length; i++) {
            String s = config[i];
            if(s.isBlank() || s.trim().startsWith("#")) {
                continue;
            }
            String label = s.split(":")[0];
            switch(label) {
                case "points_per_trick" -> config[i] = label + ": " + points_per_trick;
                case "points_bonus" -> config[i] = label + ": " + points_bonus;
                case "points_set" -> config[i] = label + ": " + points_set;
                case "underbid_set" -> config[i] = label + ": " + underbid_set;
                case "overbid_set" -> config[i] = label + ": " + overbid_set;
                case "set_penalty_scales" -> config[i] = label + ": " + set_penalty_scales;
                case "set_prevents_trick_points" -> config[i] = label + ": " + set_prevents_trick_points;
                case "negative_scores_allowed" -> config[i] = label + ": " + negative_scores_allowed;
                case "update_mode" -> config[i] = label + ": " + update_mode;
            }
        }
        sc.saveStrings(sc.DATA_PATH + "config.txt", config);
    }
}

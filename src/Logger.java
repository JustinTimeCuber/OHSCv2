public class Logger {
    static String[] log = new String[0];

    static void write(String out) {
        String[] new_log = new String[log.length + 1];
        System.arraycopy(log, 0, new_log, 0, log.length);
        new_log[log.length] = out;
        log = new_log;
        System.out.println("[LOGGER] " + out);
    }

    static void read(String file, OhHellScoreboardV2 sc) {
        try {
            log = sc.loadStrings(file);
        } catch(Exception e) {
            log = new String[0];
            sc.saveStrings(file, log);
            System.err.println("File not found: " + file + " - creating new blank file.");
        }
    }

    static void save(String file, OhHellScoreboardV2 sc) {
        sc.saveStrings(file, log);
    }

    static void reset() {
        log = new String[0];
    }

    static String[] append(String[] in) {
        String[] out = new String[log.length + in.length];
        System.arraycopy(in, 0, out, 0, in.length);
        System.arraycopy(log, 0, out, in.length, log.length);
        return out;
    }
}

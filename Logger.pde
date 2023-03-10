static class Logger {
  static PApplet context;
  static String[] log = new String[0];
  static void write(String out) {
    String[] new_log = new String[log.length + 1];
    for(int i = 0; i < log.length; i++) {
      new_log[i] = log[i];
    }
    new_log[log.length] = out;
    log = new_log;
    println("[LOGGER] " + out);
  }
  static void read(String file) {
    try {
      log = context.loadStrings(file);
    } catch(Exception e) {
      log = new String[0];
      context.saveStrings(file, log);
      System.err.println("File not found: " + file + " - creating new blank file.");
    }
  }
  static void save(String file) {
    context.saveStrings(file, log);
  }
  static void reset() {
    log = new String[0];
  }
  static String[] append(String[] in) {
    String[] out = new String[log.length + in.length];
    for(int i = 0; i < in.length; i++) {
      out[i] = in[i];
    }
    for(int i = 0; i < log.length; i++) {
      out[in.length + i] = log[i];
    }
    return out;
  }
}

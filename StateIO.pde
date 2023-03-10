void saveState(String filename) {
  ArrayList<String> state = new ArrayList<String>();
  state.add("selected_player:" + selected_player);
  state.add("error_message:" + error_message);
  state.add("error_frames:" + error_frames);
  state.add("editing_name:" + editing_name);
  state.add("bidding:" + bidding);
  state.add("game_over:" + game_over);
  state.add("suits:" + suits);
  state.add("cards_per_suit:" + cards_per_suit);
  state.add("trick_mode:" + trick_mode);
  state.add("trick_index:" + trick_index);
  state.add("trump_suit:" + trump_suit);
  state.add("custom_tricks_window:" + custom_tricks_window);
  state.add("setup:" + setup);
  state.add("hands_played:" + hands_played);
  state.add("theme_file:" + theme.file);
  for(Player p : players) {
    state.add("player:" + p.toString());
  }
  saveStrings(filename + ".ohsc", state.toArray(new String[state.size()]));
  Logger.save(filename + ".log");
}
void loadState(String filename) {
  String[] state = loadStrings(filename + ".ohsc");
  Logger.read(filename + ".log");
  players = new ArrayList<Player>();
  for(int i = 0; i < state.length; i++) {
    String label = state[i].split(":")[0];
    String value = state[i].substring(label.length() + 1);
    if(label.equals("selected_player")) selected_player = parseInt(value);
    else if(label.equals("error_message")) error_message = value;
    else if(label.equals("error_frames")) error_frames = parseInt(value);
    else if(label.equals("editing_name")) editing_name = parseBoolean(value);
    else if(label.equals("bidding")) bidding = parseBoolean(value);
    else if(label.equals("game_over")) game_over = parseBoolean(value);
    else if(label.equals("suits")) suits = parseInt(value);
    else if(label.equals("cards_per_suit")) cards_per_suit = parseInt(value);
    else if(label.equals("trick_mode")) trick_mode = parseInt(value);
    else if(label.equals("trick_index")) trick_index = parseInt(value);
    else if(label.equals("trump_suit")) trump_suit = parseInt(value);
    else if(label.equals("custom_tricks_window")) custom_tricks_window = parseBoolean(value);
    else if(label.equals("setup")) setup = parseBoolean(value);
    else if(label.equals("hands_played")) hands_played = parseInt(value);
    else if(label.equals("theme_file")) theme_file = value;
    else if(label.equals("player")) players.add(new Player("").parse(value));
    else System.err.println("Unrecognized label: " + label);
  }
  loadThemes();
  updatePlayers(false);
}

class Tile {
  float x1;
  float y1;
  float x2;
  float y2;
  Tile(float x1, float y1, float x2, float y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }
  boolean mouseInTile() {
    return mouseX > x1*width && mouseX < x2*width && mouseY > y1*height && mouseY < y2*height;
  }
  float x() {
    return width*x1;
  }
  float y() {
    return height*y1;
  }
  float w() {
    return width*(x2 - x1);
  }
  float h() {
    return height*(y2 - y1);
  }
  float cx() {
    return 0.5*width*(x2 + x1);
  }
  float cy() {
    return 0.5*height*(y2 + y1);
  }
  float mx() {
    return width*x2;
  }
  float my() {
    return height*y2;
  }
}
class Player {
  int score = 0;
  int bid = 0;
  boolean has_bid = false;
  int taken = 0;
  int total_bid = 0;
  int total_taken = 0;
  int bonuses = 0;
  int times_set = 0;
  int hands_played = 0;
  color display_color = color(255);
  Tile tile = new Tile(0, 0, 0, 0);
  String name;
  Player(String n) {
    name = n;
  }
  Player setColor(color c) {
    display_color = c;
    return this;
  }
  Player setTile(Tile t) {
    tile = t;
    return this;
  }
  String toString() {
    return score + "," + (has_bid ? bid : "X") + "," + taken + "," + total_bid + "," + total_taken + "," + bonuses + "," + times_set + "," + hands_played + "," + name;
  }
  Player parse(String in) {
    String[] inputs = in.split(",");
    score = parseInt(inputs[0]);
    if(inputs[1].equals("X")) {
      bid = 0;
      has_bid = false;
    } else {
      bid = parseInt(inputs[1]);
      has_bid = true;
    }
    taken = parseInt(inputs[2]);
    total_bid = parseInt(inputs[3]);
    total_taken = parseInt(inputs[4]);
    bonuses = parseInt(inputs[5]);
    times_set = parseInt(inputs[6]);
    hands_played = parseInt(inputs[7]);
    if(inputs.length > 8) {
      name = inputs[8];
    }
    return this;
  }
}
class Theme {
  color[] player_colors;
  color background_color;
  color line_color;
  Theme(color[] pc, color bc, color lc) {
    player_colors = pc;
    background_color = bc;
    line_color = lc;
  }
  color getPlayerColor(int index) {
    if(index >= player_colors.length) {
      return color(255);
    } else {
      return player_colors[index];
    }
  }
}
static class Logger {
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
  static void read(String file, PApplet context) {
    try {
      log = context.loadStrings(file);
    } catch(Exception e) {
      log = new String[0];
      context.saveStrings(file, log);
      System.err.println("File not found: " + file + " - creating new blank file.");
    }
  }
  static void save(String file, PApplet context) {
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
int frc = 0;
boolean setup = true;
ArrayList<Player> players = new ArrayList<Player>();
Theme main_theme = new Theme(
  new color[] {
    color(255, 0, 0),
    color(0, 255, 0),
    color(127, 127, 255),
    color(255, 255, 0),
    color(0, 255, 255),
    color(255, 0, 191),
    color(255, 255, 255),
    color(255, 127, 0),
    color(127, 127, 127),
    color(191, 0, 255)
  }, color(0), color(255));
Tile[] setup_tiles, game_tiles;
Tile add_player_button, remove_player_button, one_point_button, ten_point_button, custom_tricks_button, reset_button, theme_button, begin_game_button;
Tile popup_window, close_popup_button;
Tile number_suits_button, cards_per_suit_button, trick_mode_button, starting_point_button;
Tile setup_button, change_bids_button, proceed_button, end_game_button, trump_suit_bounding_box;
Tile restart_button;
final int MAX_PLAYERS = 10;
final float MAX_NAME_WIDTH = 0.42;
final int MAX_SUITS = 12;
final int MAX_CARDS_PER_SUIT = 50;
final String FILE_SEPARATOR = System.getProperty("file.separator");
int selected_player;
String error_message;
int error_frames;
boolean editing_name;
boolean bidding;
boolean game_over;
boolean custom_tricks_window;
int hands_played;
int suits;
int cards_per_suit;
int trick_mode;
int[] tricks;
int trick_index;
int trump_suit;
int low_framerate_cooldown;
PImage spades, clubs, hearts, diamonds, dots, crosses;
float aspect_ratio;
int millis_last_frame = 0;
boolean debug = false;
void displayError(String m, int f) {
  error_message = m;
  error_frames = f;
}
void displayError(String m) {
  displayError(m, 75);
}
void numberOfPlayersChanged(boolean holdIndex) {
  setGameTiles();
  if(setup) {
    for(int i = 0; i < players.size(); i++) {
      players.get(i).setColor(main_theme.getPlayerColor(i)).setTile(setup_tiles[i]);
    }
  } else {
    for(int i = 0; i < players.size(); i++) {
      players.get(i).setColor(main_theme.getPlayerColor(i)).setTile(game_tiles[i]);
    }
  }
  int max_deal = suits*cards_per_suit/players.size();
  if(trick_mode != 6 && trick_mode != 0) {
    int total_hands = (trick_mode == 1 || trick_mode == 3) ? total_hands = 2*max_deal - 1 : max_deal;
    tricks = new int[total_hands];
    for(int i = 0; i < tricks.length; i++) {
      if(trick_mode == 1) {
        tricks[i] = max_deal - i > 0 ? max_deal - i : 2 + i - max_deal;
      }
      if(trick_mode == 2) {
        tricks[i] = max_deal - i;
      }
      if(trick_mode == 3) {
        tricks[i] = i < max_deal ? i + 1 : 2*max_deal - 1 - i;
      }
      if(trick_mode == 4) {
        tricks[i] = i + 1;
      }
      if(trick_mode == 5) {
        if(i < (tricks.length + 1)/2) {
          tricks[i] = max_deal - 2*i;
        } else {
          tricks[i] = 2*(i - tricks.length/2) + 1 - max_deal % 2;
        }
      }
    }
  }
  if(!holdIndex || trick_index >= tricks.length) {
    trick_index = 0;
  }
}
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
  for(Player p : players) {
    state.add("player:" + p.toString());
  }
  saveStrings(filename + ".ohsc", state.toArray(new String[state.size()]));
  Logger.save(filename + ".log", this);
}
void loadState(String filename) {
  String[] state = loadStrings(filename + ".ohsc");
  Logger.read(filename + ".log", this);
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
    else if(label.equals("player")) players.add(new Player("").parse(value));
    else System.err.println("Unrecognized label: " + label);
  }
  numberOfPlayersChanged(true);
}
void setInitialValues() {
  selected_player = -1;
  error_message = "";
  error_frames = 0;
  players = new ArrayList<Player>();
  editing_name = false;
  bidding = true;
  game_over = false;
  for(int i = 0; i < 4; i++) {
    players.add(new Player(""));
  }
  hands_played = 0;
  suits = 4;
  cards_per_suit = 13;
  trick_mode = 1;
  trump_suit = 0;
  low_framerate_cooldown = 60;
  custom_tricks_window = false;
  setup = true;
  setup_tiles = new Tile[] {
    new Tile(0, 0, 1./2, 1./8),
    new Tile(1./2, 0, 1., 1./8),
    new Tile(0, 1./8, 1./2, 1./4),
    new Tile(1./2, 1./8, 1, 1./4),
    new Tile(0, 1./4, 1./2, 3./8),
    new Tile(1./2, 1./4, 1, 3./8),
    new Tile(0, 3./8, 1./2, 1./2),
    new Tile(1./2, 3./8, 1, 1./2),
    new Tile(0, 1./2, 1./2, 5./8),
    new Tile(1./2, 1./2, 1, 5./8)
  };
  add_player_button = new Tile(1./25, 7./8, 6./25, 23./24);
  remove_player_button = new Tile(7./25, 7./8, 12./25, 23./24);
  one_point_button = new Tile(1./25, 3./4, 6./25, 5./6);
  ten_point_button = new Tile(7./25, 3./4, 12./25, 5./6);
  custom_tricks_button = new Tile(13./25, 3./4, 18./25, 5./6);
  reset_button = new Tile(19./25, 3./4, 24./25, 5./6);
  theme_button = new Tile(13./25, 7./8, 18./25, 23./24);
  begin_game_button = new Tile(19./25, 7./8, 24./25, 23./24);
  popup_window = new Tile(1./6, 1./6, 5./6, 5./6);
  close_popup_button = new Tile(47./60, 1./6 + aspect_ratio/60, 49./60, 1./6 + aspect_ratio/20);
  number_suits_button = new Tile(29./150, 17./24, 49./150, 19./24);
  cards_per_suit_button = new Tile(53./150, 17./24, 73./150, 19./24);
  trick_mode_button = new Tile(77./150, 17./24, 97./150, 19./24);
  starting_point_button = new Tile(101./150, 17./24, 121./150, 19./24);
  setup_button = new Tile(1./25, 7./8, 4./25, 23./24);
  change_bids_button = new Tile(1./5, 7./8, 8./25, 23./24);
  proceed_button = new Tile(9./25, 7./8, 12./25, 23./24);
  end_game_button = new Tile(13./25, 7./8, 16./25, 23./24);
  restart_button = new Tile(18./25, 7./8, 23./25, 23./24);
  trump_suit_bounding_box = new Tile(23./25, 1 - aspect_ratio*2./25, 1, 1);
  numberOfPlayersChanged(false);
  Logger.reset();
}
void setGameTiles() {
  if(players.size() == 2) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./2, 5./6),
      new Tile(1./2, 0, 1, 5./6)
    };
  }
  if(players.size() == 3) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./3, 5./6),
      new Tile(1./3, 0, 2./3, 5./6),
      new Tile(2./3, 0, 1, 5./6)
    };
  }
  if(players.size() == 4) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./2, 5./12),
      new Tile(1./2, 0, 1, 5./12),
      new Tile(0, 5./12, 1./2, 5./6),
      new Tile(1./2, 5./12, 1, 5./6)
    };
  }
  if(players.size() == 5) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./3, 5./12),
      new Tile(1./3, 0, 2./3, 5./12),
      new Tile(2./3, 0, 1, 5./12),
      new Tile(0, 5./12, 1./2, 5./6),
      new Tile(1./2, 5./12, 1, 5./6)
    };
  }
  if(players.size() == 6) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./3, 5./12),
      new Tile(1./3, 0, 2./3, 5./12),
      new Tile(2./3, 0, 1, 5./12),
      new Tile(0, 5./12, 1./3, 5./6),
      new Tile(1./3, 5./12, 2./3, 5./6),
      new Tile(2./3, 5./12, 1, 5./6)
    };
  }
  if(players.size() == 7) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./4, 5./12),
      new Tile(1./4, 0, 1./2, 5./12),
      new Tile(1./2, 0, 3./4, 5./12),
      new Tile(3./4, 0, 1, 5./12),
      new Tile(0, 5./12, 1./3, 5./6),
      new Tile(1./3, 5./12, 2./3, 5./6),
      new Tile(2./3, 5./12, 1, 5./6)
    };
  }
  if(players.size() == 8) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./4, 5./12),
      new Tile(1./4, 0, 1./2, 5./12),
      new Tile(1./2, 0, 3./4, 5./12),
      new Tile(3./4, 0, 1, 5./12),
      new Tile(0, 5./12, 1./4, 5./6),
      new Tile(1./4, 5./12, 1./2, 5./6),
      new Tile(1./2, 5./12, 3./4, 5./6),
      new Tile(3./4, 5./12, 1, 5./6)
    };
  }
  if(players.size() == 9) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./3, 5./18),
      new Tile(1./3, 0, 2./3, 5./18),
      new Tile(2./3, 0, 1, 5./18),
      new Tile(0, 5./18, 1./3, 5./9),
      new Tile(1./3, 5./18, 2./3, 5./9),
      new Tile(2./3, 5./18, 1, 5./9),
      new Tile(0, 5./9, 1./3, 5./6),
      new Tile(1./3, 5./9, 2./3, 5./6),
      new Tile(2./3, 5./9, 1, 5./6)
    };
  }
  if(players.size() == 10) {
    game_tiles = new Tile[] {
      new Tile(0, 0, 1./4, 5./18),
      new Tile(1./4, 0, 1./2, 5./18),
      new Tile(1./2, 0, 3./4, 5./18),
      new Tile(3./4, 0, 1, 5./18),
      new Tile(0, 5./18, 1./3, 5./9),
      new Tile(1./3, 5./18, 2./3, 5./9),
      new Tile(2./3, 5./18, 1, 5./9),
      new Tile(0, 5./9, 1./3, 5./6),
      new Tile(1./3, 5./9, 2./3, 5./6),
      new Tile(2./3, 5./9, 1, 5./6)
    };
  }
}
void drawButton(Tile location, String text, float size, boolean enabled, boolean hoverable) {
  if(enabled && hoverable && location.mouseInTile()) {
    fill(mousePressed ? 127 : 64);
  } else {
    fill(0);
  }
  strokeWeight(2);
  stroke(255);
  rect(location.x(), location.y(), location.w(), location.h());
  fill(enabled ? 255 : 64);
  textSize(size*width);
  text(text, location.cx(), location.cy() - size*width*0.1);
}
int getKeyValue(char k) {
  switch(k) {
    case '1': return 1;
    case '2': return 2;
    case '3': return 3;
    case '4': return 4;
    case '5': return 5;
    case '6': return 6;
    case '7': return 7;
    case '8': return 8;
    case '9': return 9;
    case '0': return 10;
    case 'q': return -1;
    case 'w': return -2;
    case 'e': return -3;
    case 'r': return -4;
    case 't': return -5;
    case 'y': return -6;
    case 'u': return -7;
    case 'i': return -8;
    case 'o': return -9;
    case 'p': return -10;
    default: return 0;
  }
}
String trickMode() {
  switch(trick_mode) {
    case 0: return "Off";
    case 1: return "Down/Up";
    case 2: return "Down";
    case 3: return "Up/Down";
    case 4: return "Up";
    case 5: return "Split";
    case 6: return "Custom";
    default: return "Undefined";
  }
}
PImage trumpIcon() {
  switch(trump_suit) {
    case 1: return spades;
    case 2: return clubs;
    case 3: return hearts;
    case 4: return diamonds;
    case 5: return dots;
    case 6: return crosses;
    default: return null;
  }
}
PImage loadImage(String file) {
  PImage img = super.loadImage(file);
  if(img == null) {
    System.err.println("Failed to load image: " + file + " - cannot start program.");
    System.exit(1);
  }
  return img;
}
void saveRecord() {
  String[] out = new String[players.size() + 1];
  out[0] = "Hands played: " + hands_played;
  for(int i = 0; i < players.size(); i++) {
    Player p = players.get(i);
    out[i + 1] = p.name + ": score=" + p.score + " bid=" + p.total_bid + " taken=" + p.total_taken + " bonus=" + p.bonuses + " set=" + p.times_set + " hands=" + p.hands_played;
  }
  out = Logger.append(out);
  String timestamp = year() + "-" + month() + "-" + day() + "-" + hour() + "_" + minute() + "_" + second();
  saveStrings("saves" + FILE_SEPARATOR + timestamp + ".txt", out);
}
void resetFramerateCooldown() {
  low_framerate_cooldown = 60;
  frameRate(30);
}
void setup() {
  if(debug) {
    println("System properties:");
    System.getProperties().list(System.out);
  }
  fullScreen();
  frameRate(30);
  aspect_ratio = (float)width/height;
  setInitialValues();
  try {
    loadState("latest");
  } catch(Exception e) {
    System.err.println("Exception loading save: " + e.toString());
    setInitialValues();
  }
  spades = loadImage("assets" + FILE_SEPARATOR + "spades.png");
  hearts = loadImage("assets" + FILE_SEPARATOR + "hearts.png");
  clubs = loadImage("assets" + FILE_SEPARATOR + "clubs.png");
  diamonds = loadImage("assets" + FILE_SEPARATOR + "diamonds.png");
  dots = loadImage("assets" + FILE_SEPARATOR + "dots.png");
  crosses = loadImage("assets" + FILE_SEPARATOR + "crosses.png");
}
void draw() {
  int frametime = millis() - millis_last_frame;
  millis_last_frame = millis();
  frc++;
  low_framerate_cooldown--;
  if(low_framerate_cooldown == 0) {
    frameRate(2);
  }
  background(0);
  if(frc % 5 == 0) saveState("latest");
  strokeWeight(2);
  stroke(255);
  textAlign(CENTER, CENTER);
  if(setup) {
    for(int i = 0; i < players.size(); i++) {
      Player p = players.get(i);
      noFill();
      if(i == selected_player) {
        fill(p.display_color, 127);
      }
      rect(p.tile.x(), p.tile.y(), p.tile.w(), p.tile.h());
      fill(p.display_color);
      if(i == selected_player && editing_name) {
        resetFramerateCooldown();
        if((frc / 10) % 2 == 0) {
          fill(p.display_color, 127);
        }
      }
      textAlign(LEFT, CENTER);
      textSize(width*0.05);
      text(p.name.equals("") ? ("Player " + (i+1)) : p.name, p.tile.x() + width*0.01, p.tile.cy() - width*0.005);
      textAlign(CENTER, CENTER);
      textSize(width*0.03);
      fill(p.display_color);
      text(p.score, p.tile.mx() - width*0.04, p.tile.cy() - width*0.003);
    }
    boolean popup_shown = custom_tricks_window;
    drawButton(add_player_button, selected_player == -1 ? "Add Player" : "Add Player Before", 0.02, players.size() < MAX_PLAYERS, !popup_shown);
    drawButton(remove_player_button, "Remove Player", 0.02, selected_player != -1 && players.size() > 2, !popup_shown);
    drawButton(one_point_button, "Add/Remove 1 pt", 0.02, selected_player != -1, !popup_shown);
    drawButton(ten_point_button, "Add/Remove 10 pts", 0.02, selected_player != -1, !popup_shown);
    drawButton(custom_tricks_button, "Trick Options", 0.02, true, !popup_shown);
    drawButton(reset_button, "Reset", 0.02, true, !popup_shown);
    drawButton(theme_button, "Themes", 0.02, false, !popup_shown);
    drawButton(begin_game_button, "Begin Game", 0.02, true, !popup_shown);
    if(custom_tricks_window) {
      fill(64, 230);
      stroke(255);
      rect(popup_window.x(), popup_window.y(), popup_window.w(), popup_window.h());
      drawButton(close_popup_button, "X", 0.02, true, true);
      fill(255);
      textSize(width*0.05);
      text("Trick Customization", popup_window.cx(), popup_window.y() + height/12);
      textAlign(CENTER, TOP);
      textSize(width*0.02);
      text("Number of suits: " + suits + "\nCards per suit: " + cards_per_suit + "\nTotal cards in deck: " + (suits*cards_per_suit) + "\nTrick mode: " + trickMode() + "\nPreview:", popup_window.cx(), popup_window.y() + width/10);
      String preview = "";
      fill(255, 0, 0);
      if(trick_mode == 0) {
        preview = "Trick sequence disabled; some checks will not function";
      } else if(trick_mode == 6) {
        preview = "Custom trick sequences are not currently available.";
      } else {
        fill(255);
        for(int i = 0; i < tricks.length; i++) {
          if(i == trick_index) {
            preview += "*";
          }
          preview += tricks[i];
          if(i < tricks.length - 1) {
            preview += ", ";
          }
        }
      }
      if(textWidth(preview) > popup_window.w()*0.9) {
        textSize(width*0.02*0.9*popup_window.w()/textWidth(preview));
      }
      textAlign(CENTER, CENTER);
      text(preview, popup_window.cx(), popup_window.y() + width*0.26);
      drawButton(number_suits_button, "Number of Suits", 0.015, true, true);
      drawButton(cards_per_suit_button, "Cards per Suit", 0.015, true, true);
      drawButton(trick_mode_button, "Trick Mode", 0.015, true, true);
      drawButton(starting_point_button, "Starting Point", 0.015, true, true);
    } else if(trick_mode == 6) {
      trick_mode = 0;
    }
  } else {
    textAlign(CENTER, CENTER);
    int total_bid = 0;
    int total_taken = 0;
    for(int i = 0; i < players.size(); i++) {
      Player p = players.get(i);
      total_bid += p.bid;
      total_taken += p.taken;
      fill(0);
      rect(p.tile.x(), p.tile.y(), p.tile.w(), p.tile.h());
      fill(p.display_color);
      textSize(game_tiles[0].w()*0.1);
      text(p.name.equals("") ? ("Player " + (i+1)) : p.name, p.tile.cx(), p.tile.y() + p.tile.h()/6);
      textSize(p.tile.h()*0.6);
      if(p.score >= 0) {
        text(p.score, p.tile.cx(), p.tile.my() - 2*p.tile.h()/5);
      } else {
        text(-p.score, p.tile.cx() + p.tile.h()*0.15, p.tile.my() - 2*p.tile.h()/5);
        noStroke();
        rect(p.tile.cx() + p.tile.h()*0.16 - 0.5*textWidth(String.valueOf(-p.score)), p.tile.y() + 0.55*p.tile.h(), -p.tile.h()/6, p.tile.h()/24);
        stroke(255);
      }
      textSize(game_tiles[0].w()*0.04);
      text("Bid", p.tile.x() + p.tile.w()/8, p.tile.my() - p.tile.w()/6);
      if(p.has_bid) {
        textSize(p.tile.w()*0.1);
        text(p.bid, p.tile.x() + p.tile.w()/8, p.tile.my() - p.tile.w()/10);
      }
      if(!bidding) {
        textSize(game_tiles[0].w()*0.04);
        text("Taken", p.tile.mx() - p.tile.w()/8, p.tile.my() - p.tile.w()/6);
        textSize(p.tile.w()*0.1);
        text(p.taken, p.tile.mx() - p.tile.w()/8, p.tile.my() - p.tile.w()/10);
      }
    }
    if(!game_over) {
      drawButton(setup_button, "Setup", 0.02, true, true);
      drawButton(change_bids_button, "Change Bids", 0.02, !bidding, true);
      drawButton(proceed_button, "Proceed", 0.02, trick_mode == 0 || (bidding && total_bid != tricks[trick_index]) || (!bidding && total_taken == tricks[trick_index]), true);
      drawButton(end_game_button, "End Game", 0.02, true, true);
      textSize(width*0.01);
      fill(255);
      text("Deal", 18*width/25, height*17/20);
      text("Bid", 4*width/5, height*17/20);
      text("Taken", 22*width/25, height*17/20);
      text("Trump", 24*width/25, height*17/20);
      textSize(width*0.05);
      text(trick_mode == 0 ? "--" : String.valueOf(tricks[trick_index]), 18*width/25, 11*height/12);
      if(!bidding) {
        if(trick_mode != 0) {
          if(total_bid < tricks[trick_index]) {
            fill(255, 127, 127);
          } else {
            fill(127, 191, 255);
          }
        }
      }
      text(total_bid, 4*width/5, 11*height/12);
      fill(255);
      text(total_taken, 22*width/25, 11*height/12);
      PImage trump_icon = trumpIcon();
      if(trump_icon != null) {
        image(trump_icon, trump_suit_bounding_box.x() + trump_suit_bounding_box.w()/4, trump_suit_bounding_box.y() + trump_suit_bounding_box.h()/4, trump_suit_bounding_box.w()/2, trump_suit_bounding_box.h()/2);
      }
    } else {
      drawButton(restart_button, "Restart", 0.02, true, true);
      textSize(width*0.05);
      fill(255);
      text("Game Over", width/2, height*11/12 - width*0.005);
    }
  }
  if(error_frames > 0) {
    fill(64, 230);
    stroke(255);
    if(error_frames <= 15) {
      fill(64, 16*error_frames);
      stroke(255, 16*error_frames);
    }
    textSize(width*0.025);
    textAlign(CENTER, CENTER);
    rect(width*0.45 - 0.5*textWidth(error_message), height*0.45, width*0.1 + textWidth(error_message), height*0.1);
    fill(255, 0, 0);
    if(error_frames <= 25) {
      fill(255, 0, 0, 10*error_frames);
    }
    text(error_message, width/2, height/2 - width*0.0025);
    error_frames--;
    low_framerate_cooldown++;
  }
  if(debug) {
    textAlign(LEFT, TOP);
    textSize(20);
    fill(255);
    text("Milliseconds since last frame: " + frametime, 2, 2);
  }
}
void mouseMoved() {
  resetFramerateCooldown();
}
void mousePressed() {
  resetFramerateCooldown();
  if(game_over) {
    if(restart_button.mouseInTile()) {
      setInitialValues();
    }
    return;
  }
  if(setup) {
    if(custom_tricks_window) {
      if(close_popup_button.mouseInTile()) {
        custom_tricks_window = false;
        return;
      }
      if(number_suits_button.mouseInTile()) {
        if(mouseButton == LEFT) {
          if(suits < MAX_SUITS) {
            suits++;
            numberOfPlayersChanged(false);
          } else {
            displayError("The maximum number of suits is " + MAX_SUITS);
          }
        } else if((suits - 1)*cards_per_suit >= MAX_PLAYERS) {
          suits--;
          numberOfPlayersChanged(false);
        } else {
          displayError("The minimum deck size is " + MAX_PLAYERS);
        }
        return;
      }
      if(cards_per_suit_button.mouseInTile()) {
        if(mouseButton == LEFT) {
          if(cards_per_suit < MAX_CARDS_PER_SUIT) {
            cards_per_suit++;
            numberOfPlayersChanged(false);
          } else {
            displayError("The maximum cards per suit is " + MAX_CARDS_PER_SUIT);
          }
        } else if(suits*(cards_per_suit - 1) >= MAX_PLAYERS) {
          cards_per_suit--;
          numberOfPlayersChanged(false);
        } else {
          displayError("The minimum deck size is " + MAX_PLAYERS);
        }
        return;
      }
      if(trick_mode_button.mouseInTile()) {
        if(mouseButton == LEFT) {
          trick_mode++;
          if(trick_mode > 6) {
            trick_mode = 0;
          }
        } else {
          trick_mode--;
          if(trick_mode < 0) {
            trick_mode = 6;
          }
        }
        trick_index = 0;
        numberOfPlayersChanged(false);
        return;
      }
      if(starting_point_button.mouseInTile()) {
        if(mouseButton == LEFT) {
          trick_index++;
          if(trick_index >= tricks.length) {
            trick_index = 0;
          }
        } else {
          trick_index--;
          if(trick_index < 0) {
            trick_index = tricks.length - 1;
          }
        }
      }
      return;
    }
    for(int i = 0; i < players.size(); i++) {
      if(players.get(i).tile.mouseInTile()) {
        if(selected_player == i) {
          editing_name = !editing_name;
        } else {
          selected_player = i;
          editing_name = false;
        }
        return;
      }
    }
    editing_name = false;
    if(add_player_button.mouseInTile()) {
      if(players.size() < MAX_PLAYERS) {
        if(selected_player == -1) {
          players.add(new Player("").setColor(main_theme.getPlayerColor(players.size())).setTile(setup_tiles[players.size()]));
          numberOfPlayersChanged(false);
        } else {
          players.add(selected_player, new Player("").setColor(main_theme.getPlayerColor(players.size())).setTile(setup_tiles[players.size()]));
          selected_player++;
          numberOfPlayersChanged(false);
        }
      } else {
        displayError("The maximum number of players is " + MAX_PLAYERS);
      }
      return;
    }
    if(remove_player_button.mouseInTile()) {
      if(players.size() > 2) {
        if(selected_player == -1) {
          displayError("Must select a player to remove");
        } else {
          players.remove(selected_player);
          selected_player--;
          numberOfPlayersChanged(false);
        }
      } else {
        displayError("The minimum number of players is 2");
      }
      return;
    }
    if(one_point_button.mouseInTile()) {
      if(selected_player == -1) {
        displayError("Must select a player to change score");
      } else {
        players.get(selected_player).score += (mouseButton == LEFT ? 1 : -1);
      }
      return;
    }
    if(ten_point_button.mouseInTile()) {
      if(selected_player == -1) {
        displayError("Must select a player to change score");
      } else {
        players.get(selected_player).score += (mouseButton == LEFT ? 10 : -10);
      }
      return;
    }
    if(custom_tricks_button.mouseInTile()) {
      custom_tricks_window = true;
      return;
    }
    if(reset_button.mouseInTile()) {
      setInitialValues();
    }
    if(theme_button.mouseInTile()) {
      displayError("Themes have not been implemented yet.");
      return;
    }
    if(begin_game_button.mouseInTile()) {
      setup = false;
      for(int i = 0; i < players.size(); i++) {
        players.get(i).setTile(game_tiles[i]);
      }
      selected_player = -1;
      return;
    }
    selected_player = -1;
  } else {
    if(bidding) {
      for(int i = 0; i < players.size(); i++) {
        Player p = players.get(i);
        if(p.tile.mouseInTile()) {
          if(mouseButton == LEFT) {
            if(p.bid <= tricks[trick_index] || trick_mode == 0) {
              if(!p.has_bid) {
                p.has_bid = true;
              }
              p.bid++;
            } else {
              displayError("Maximum bid for this hand is " + (tricks[trick_index] + 1));
            }
          } else if(p.bid > 0) {
            p.bid--;
          } else {
            if(p.has_bid) {
              displayError("Minimum bid is 0");
            } else {
              p.has_bid = true;
            }
          }
          return;
        }
      }
    } else {
      for(int i = 0; i < players.size(); i++) {
        Player p = players.get(i);
        if(p.tile.mouseInTile()) {
          if(mouseButton == LEFT) {
            if(p.taken < tricks[trick_index] || trick_mode == 0) {
              p.taken++;
            } else {
              displayError("Cannot take more tricks than were dealt");
            }
          } else if(p.taken > 0) {
            p.taken--;
          } else {
            displayError("Tricks taken must be greater than zero");
          }
          return;
        }
      }
    }
    if(setup_button.mouseInTile()) {
      setup = true;
      for(int i = 0; i < players.size(); i++) {
        players.get(i).setTile(setup_tiles[i]);
      }
      return;
    }
    if(change_bids_button.mouseInTile()) {
      if(bidding) {
        displayError("Already changing bids");
      } else {
        bidding = true;
      }
      return;
    }
    if(proceed_button.mouseInTile()) {
      if(bidding) {
        int total_bid = 0;
        boolean all_players_bid = true;
        for(Player p : players) {
          total_bid += p.bid;
          all_players_bid &= p.has_bid;
        }
        if(all_players_bid) {
          if(trick_mode == 0 || total_bid != tricks[trick_index] || (keyPressed && key == ENTER && mouseButton == RIGHT)) {
            bidding = false;
          } else {
            displayError("Tricks bid can't equal tricks dealt - override with enter + right click");
          }
        } else {
          displayError("Not all players have a bid entered");
        }
      } else {
        int total_taken = 0;
        for(int i = 0; i < players.size(); i++) {
          total_taken += players.get(i).taken;
        }
        if(trick_mode == 0 || total_taken == tricks[trick_index] || (keyPressed && key == ENTER && mouseButton == RIGHT)) {
          hands_played++;
          Logger.write("--------------------------------");
          Logger.write("Hand #" + hands_played + " - Tricks: " + tricks[trick_index]);
          for(Player p : players) {
            int old = p.score;
            p.total_bid += p.bid;
            p.total_taken += p.taken;
            if(p.taken < p.bid) {
              p.score += p.taken;
            } else if(p.taken == p.bid) {
              p.score += p.taken + 10;
              p.bonuses++;
            } else {
              p.score -= 10;
              p.times_set++;
            }
            p.hands_played++;
            Logger.write(p.name + " bid " + p.bid + " tricks and took " + p.taken + ". " + old + " --> " + p.score);
            p.bid = 0;
            p.has_bid = false;
            p.taken = 0;
          }
          if(trick_mode != 0) {
            trick_index++;
            if(trick_index >= tricks.length) {
              game_over = true;
              saveRecord();
              trick_index--;
            }
          }
          trump_suit = 0;
          bidding = true;
        } else {
          displayError("Tricks taken must equal tricks dealt - override with enter + right click");
        }
      }
      return;
    }
    if(end_game_button.mouseInTile()) {
      if(keyPressed && key == ENTER && mouseButton == RIGHT) {
        game_over = true;
        saveRecord();
      } else {
        displayError("End game? Confirm with enter + right click");
      }
      return;
    }
    if(trump_suit_bounding_box.mouseInTile()) {
      trump_suit += mouseButton == LEFT ? 1 : -1;
      if(trump_suit < 0) {
        trump_suit = 6;
      }
      if(trump_suit > 6) {
        trump_suit = 0;
      }
      return;
    }
  }
}
void keyPressed() {
  resetFramerateCooldown();
}
void keyTyped() {
  resetFramerateCooldown();
  if(game_over) {
    return;
  }
  if(setup) {
    if(custom_tricks_window) {
      return;
    }
    if(editing_name) {
      String s = players.get(selected_player).name;
      textSize(width*0.05);
      if(key == ',') {
        displayError("That character conflicts with the OHSC v2.0 autosave format");
        return;
      }
      if(key == BACKSPACE) {
        if(s.length() > 0) {
          players.get(selected_player).name = s.substring(0, s.length() - 1);
        }
      } else if(key == ENTER) {
        editing_name = false;
      } else if(textWidth(s + key) <= MAX_NAME_WIDTH*width) {
        players.get(selected_player).name += key;
      } else {
        displayError("The maximum name width is " + round(MAX_NAME_WIDTH*width) + " pixels");
      }
    } else {
      int i = Math.abs(getKeyValue(key));
      if(i != 0 && i - 1 < players.size()) {
        i--;
        if(selected_player == i) {
          editing_name = true;
        } else {
          selected_player = i;
        }
      }
      if(selected_player >= 0 && key == ENTER) {
        editing_name = true;
      }
    }
  } else {
    if(key == 's') {
      trump_suit = 1;
      return;
    }
    if(key == 'c') {
      trump_suit = 2;
      return;
    }
    if(key == 'h') {
      trump_suit = 3;
      return;
    }
    if(key == 'd') {
      trump_suit = 4;
      return;
    }
    if(key == 'o') {
      trump_suit = 5;
      return;
    }
    if(key == 'x') {
      trump_suit = 6;
      return;
    }
    if(bidding) {
      int i = Math.abs(getKeyValue(key));
      if(i != 0 && i - 1 < players.size()) {
        i--;
        Player p = players.get(i);
        if(getKeyValue(key) > 0) {
          if(p.bid <= tricks[trick_index] || p.bid == 0 || trick_mode == 0) {
            p.bid++;
          } else {
            displayError("Maximum bid for this hand is " + (tricks[trick_index] + 1));
          }
        } else if(players.get(i).bid > 0) {
          p.bid--;
        } else {
          displayError("Minimum bid is 0");
        }
      }
    } else {
      int i = Math.abs(getKeyValue(key));
      if(i != 0 && i - 1 < players.size()) {
        i--;
        Player p = players.get(i);
        if(getKeyValue(key) > 0) {
          if(p.taken < tricks[trick_index] || trick_mode == 0) {
            p.taken++;
          } else {
            displayError("Cannot take more tricks than were dealt");
          }
        } else if(p.taken > 0) {
          p.taken--;
        } else {
          displayError("Tricks taken must be greater than zero");
        }
      }
    }
  }
}

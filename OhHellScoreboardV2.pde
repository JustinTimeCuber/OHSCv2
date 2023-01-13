int frc = 0;
boolean setup = true;
ArrayList<Player> players = new ArrayList<Player>();
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
      players.get(i).setColor(theme.getPlayerColor(i)).setTile(setup_tiles[i]);
    }
  } else {
    for(int i = 0; i < players.size(); i++) {
      players.get(i).setColor(theme.getPlayerColor(i)).setTile(game_tiles[i]);
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
void drawButton(Tile location, String text, float size, boolean enabled, boolean hoverable) {
  if(enabled && hoverable && location.mouseInTile()) {
    fill(mousePressed ? theme.button_click_color : theme.button_hover_color);
  } else {
    fill(theme.background_color);
  }
  strokeWeight(2);
  stroke(theme.line_color);
  rect(location.x(), location.y(), location.w(), location.h());
  fill(enabled ? theme.text_color : theme.grayed_text_color);
  textSize(size*width);
  text(text, location.cx(), location.cy() - size*width*0.1);
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
void handleBidChange(Player p, boolean pos) {
  if(pos) {
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
}
void handleTakenChange(Player p, boolean pos) {
  if(pos) {
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
void setup() {
  if(debug) {
    println("System properties:");
    System.getProperties().list(System.out);
  }
  Logger.context = this;
  fullScreen();
  frameRate(30);
  aspect_ratio = (float)width/height;
  loadThemes();
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
  background(theme.background_color);
  if(frc % 5 == 0) saveState("latest");
  strokeWeight(2);
  stroke(theme.line_color);
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
      fill(theme.popup_background_color, 230);
      stroke(theme.line_color);
      rect(popup_window.x(), popup_window.y(), popup_window.w(), popup_window.h());
      drawButton(close_popup_button, "X", 0.02, true, true);
      fill(theme.line_color);
      textSize(width*0.05);
      text("Trick Customization", popup_window.cx(), popup_window.y() + height/12);
      textAlign(CENTER, TOP);
      textSize(width*0.02);
      text("Number of suits: " + suits + "\nCards per suit: " + cards_per_suit + "\nTotal cards in deck: " + (suits*cards_per_suit) + "\nTrick mode: " + trickMode() + "\nPreview:", popup_window.cx(), popup_window.y() + width/10);
      String preview = "";
      fill(theme.error_text_color);
      if(trick_mode == 0) {
        preview = "Trick sequence disabled; some checks will not function";
      } else if(trick_mode == 6) {
        preview = "Custom trick sequences are not currently available.";
      } else {
        fill(theme.text_color);
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
      fill(theme.background_color);
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
        stroke(theme.line_color);
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
      fill(theme.text_color);
      text("Deal", 18*width/25, height*17/20);
      text("Bid", 4*width/5, height*17/20);
      text("Taken", 22*width/25, height*17/20);
      text("Trump", 24*width/25, height*17/20);
      textSize(width*0.05);
      text(trick_mode == 0 ? "--" : String.valueOf(tricks[trick_index]), 18*width/25, 11*height/12);
      if(!bidding) {
        if(trick_mode != 0) {
          if(total_bid < tricks[trick_index]) {
            fill(theme.underbid_color);
          } else {
            fill(theme.overbid_color);
          }
        }
      }
      text(total_bid, 4*width/5, 11*height/12);
      fill(theme.text_color);
      text(total_taken, 22*width/25, 11*height/12);
      PImage trump_icon = trumpIcon();
      if(trump_icon != null) {
        image(trump_icon, trump_suit_bounding_box.x() + trump_suit_bounding_box.w()/4, trump_suit_bounding_box.y() + trump_suit_bounding_box.h()/4, trump_suit_bounding_box.w()/2, trump_suit_bounding_box.h()/2);
      }
    } else {
      drawButton(restart_button, "Restart", 0.02, true, true);
      textSize(width*0.05);
      fill(theme.text_color);
      text("Game Over", width/2, height*11/12 - width*0.005);
    }
  }
  if(error_frames > 0) {
    fill(theme.popup_background_color, 230);
    stroke(theme.line_color);
    if(error_frames <= 15) {
      fill(theme.popup_background_color, 16*error_frames);
      stroke(theme.line_color, 16*error_frames);
    }
    textSize(width*0.025);
    textAlign(CENTER, CENTER);
    rect(width*0.45 - 0.5*textWidth(error_message), height*0.45, width*0.1 + textWidth(error_message), height*0.1);
    fill(theme.error_text_color);
    if(error_frames <= 25) {
      fill(theme.error_text_color, 10*error_frames);
    }
    text(error_message, width/2, height/2 - width*0.0025);
    error_frames--;
    low_framerate_cooldown++;
  }
  if(debug) {
    textAlign(LEFT, TOP);
    textSize(20);
    fill(theme.text_color);
    text("Milliseconds since last frame: " + frametime, 2, 2);
  }
}

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

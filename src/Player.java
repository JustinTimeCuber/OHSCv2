class Player {
    static OhHellScoreboardV2 sc;
    int score = 0;
    int bid = 0;
    boolean has_bid = false;
    int taken = 0;
    int total_bid = 0;
    int total_taken = 0;
    int bonuses = 0;
    int times_set = 0;
    int hands_played = 0;
    int display_color = sc.color(255);
    String name = "";

    Player() {
    }

    Player setColor(int c) {
        display_color = c;
        return this;
    }

    @Override
    public String toString() {
        return score + "," + (has_bid ? bid : "X") + "," + taken + "," + total_bid + "," + total_taken + "," + bonuses + "," + times_set + "," + hands_played + "," + name;
    }

    Player parse(String in) {
        String[] inputs = in.split(",");
        score = Integer.parseInt(inputs[0]);
        if(inputs[1].equals("X")) {
            bid = 0;
            has_bid = false;
        } else {
            bid = Integer.parseInt(inputs[1]);
            has_bid = true;
        }
        taken = Integer.parseInt(inputs[2]);
        total_bid = Integer.parseInt(inputs[3]);
        total_taken = Integer.parseInt(inputs[4]);
        bonuses = Integer.parseInt(inputs[5]);
        times_set = Integer.parseInt(inputs[6]);
        hands_played = Integer.parseInt(inputs[7]);
        if(inputs.length > 8) {
            name = inputs[8];
        }
        return this;
    }
}

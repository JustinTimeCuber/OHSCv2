import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.io.*;
import java.util.ArrayList;

public class OhHellScoreboardV2 extends PApplet {
    int frc = 0;
    ArrayList<Player> players = new ArrayList<>();
    Tile[] setup_tiles, game_tiles;
    Tile add_player_button, remove_player_button, one_point_button, ten_point_button, custom_tricks_button, reset_button, theme_button, begin_game_button;
    Tile popup_window, close_popup_button;
    Tile number_suits_button, cards_per_suit_button, trick_mode_button, starting_point_button;
    Tile refresh_themes_button;
    Tile setup_button, change_bids_button, proceed_button, end_game_button, trump_suit_bounding_box;
    Tile restart_button, statistics_button;
    final int MAX_PLAYERS = 10;
    final float MAX_NAME_WIDTH = 0.42f;
    final int MAX_SUITS = 12;
    final int MAX_CARDS_PER_SUIT = 50;
    final String FILE_SEPARATOR = System.getProperty("file.separator");
    final String DATA_PATH = getOSSpecificDataPath() + FILE_SEPARATOR + "OHSCv2" + FILE_SEPARATOR;
    String last_save = null;
    int selected_player;
    String error_message;
    int error_frames;
    boolean editing_name;
    Window current_window = Window.NONE;
    Screen current_screen = Screen.SETUP_TO_BIDDING;
    int hands_played;
    int suits;
    int cards_per_suit;
    int trick_mode;
    int[] tricks;
    int trick_index;
    int trump_suit;
    int low_framerate_cooldown;
    PImage spades, clubs, hearts, diamonds, dots, crosses;
    PFont font;
    float aspect_ratio;
    int millis_last_frame = 0;
    final boolean debug = false;
    final PrintStream system_err = System.err;
    final PrintStream placebo_output = new PrintStream(new OutputStream() {
        @Override
        public void write(int b) {
        }
    });

    static String getOSSpecificDataPath() {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("windows")) {
            return System.getenv("APPDATA");
        } else if(os.contains("linux")) {
            return System.getenv("HOME") + "/.local/share";
        } else if(os.contains("mac")) {
            return System.getenv("HOME") + "/Library/Application Support";
        } else {
            System.err.println("Warning: OS \"" + os + "\" not supported. Will attempt to save to ~/OHSCv2.");
            return System.getenv("HOME");
        }
    }

    void displayError(String m, int f) {
        error_message = m;
        error_frames = f;
    }

    void displayError(String m) {
        displayError(m, 75);
    }

    void updatePlayers(boolean resetIndex) {
        Tile.setGameTiles();
        if(current_screen.isSetup()) {
            for(int i = 0; i < players.size(); i++) {
                players.get(i).setColor(Theme.theme.getPlayerColor(i)).setTile(setup_tiles[i]);
            }
        } else {
            for(int i = 0; i < players.size(); i++) {
                players.get(i).setColor(Theme.theme.getPlayerColor(i)).setTile(game_tiles[i]);
            }
        }
        int max_deal = suits * cards_per_suit / players.size();
        if(trick_mode != 6 && trick_mode != 0) {
            int total_hands = (trick_mode == 1 || trick_mode == 3) ? 2 * max_deal - 1 : max_deal;
            tricks = new int[total_hands];
            for(int i = 0; i < tricks.length; i++) {
                if(trick_mode == 1) {
                    tricks[i] = max_deal - i > 0 ? max_deal - i : 2 + i - max_deal;
                }
                if(trick_mode == 2) {
                    tricks[i] = max_deal - i;
                }
                if(trick_mode == 3) {
                    tricks[i] = i < max_deal ? i + 1 : 2 * max_deal - 1 - i;
                }
                if(trick_mode == 4) {
                    tricks[i] = i + 1;
                }
                if(trick_mode == 5) {
                    if(i < (tricks.length + 1) / 2) {
                        tricks[i] = max_deal - 2 * i;
                    } else {
                        tricks[i] = 2 * (i - tricks.length / 2) + 1 - max_deal % 2;
                    }
                }
            }
        }
        if(resetIndex || trick_index >= tricks.length) {
            trick_index = 0;
        }
    }

    void setInitialValues() {
        selected_player = -1;
        error_message = "";
        error_frames = 0;
        players = new ArrayList<>();
        editing_name = false;
        current_window = Window.NONE;
        current_screen = Screen.SETUP_TO_BIDDING;
        for(int i = 0; i < 4; i++) {
            players.add(new Player());
        }
        hands_played = 0;
        suits = 4;
        cards_per_suit = 13;
        trick_mode = 1;
        trump_suit = 0;
        low_framerate_cooldown = 60;
        setup_tiles = new Tile[]{
                new Tile(0, 0, 0.5, 0.125),
                new Tile(0.5, 0, 1., 0.125),
                new Tile(0, 0.125, 0.5, 0.25),
                new Tile(0.5, 0.125, 1, 0.25),
                new Tile(0, 0.25, 0.5, 0.375),
                new Tile(0.5, 0.25, 1, 0.375),
                new Tile(0, 0.375, 0.5, 0.5),
                new Tile(0.5, 0.375, 1, 0.5),
                new Tile(0, 0.5, 0.5, 0.625),
                new Tile(0.5, 0.5, 1, 0.625)
        };
        add_player_button = new Tile(0.04, 0.875, 0.24, 0.958);
        remove_player_button = new Tile(0.28, 0.875, 0.48, 0.958);
        one_point_button = new Tile(0.04, 0.75, 0.24, 0.833);
        ten_point_button = new Tile(0.28, 0.75, 0.48, 0.833);
        custom_tricks_button = new Tile(0.52, 0.75, 0.72, 0.833);
        reset_button = new Tile(0.76, 0.75, 0.96, 0.833);
        theme_button = new Tile(0.52, 0.875, 0.72, 0.958);
        begin_game_button = new Tile(0.76, 0.875, 0.96, 0.958);
        popup_window = new Tile(0.167, 0.167, 0.833, 0.833);
        close_popup_button = new Tile(0.783, 0.167 * (1 + aspect_ratio * 0.1), 0.817, 0.167 * (1 + aspect_ratio * 0.3));
        number_suits_button = new Tile(0.193, 0.708, 0.327, 0.792);
        cards_per_suit_button = new Tile(0.353, 0.708, 0.487, 0.792);
        trick_mode_button = new Tile(0.513, 0.708, 0.647, 0.792);
        starting_point_button = new Tile(0.673, 0.708, 0.807, 0.792);
        refresh_themes_button = new Tile(0.733, 0.167 * (1 + aspect_ratio * 0.1), 0.767, 0.167 * (1 + aspect_ratio * 0.3));
        setup_button = new Tile(0.04, 0.875, 0.16, 0.958);
        change_bids_button = new Tile(0.2, 0.875, 0.32, 0.958);
        proceed_button = new Tile(0.36, 0.875, 0.48, 0.958);
        end_game_button = new Tile(0.52, 0.875, 0.64, 0.958);
        statistics_button = new Tile(0.08, 0.875, 0.28, 0.958);
        restart_button = new Tile(0.72, 0.875, 0.92, 0.958);
        trump_suit_bounding_box = new Tile(0.92, 1 - aspect_ratio * 0.08, 1, 1);
        Theme.loadThemes();
        updatePlayers(true);
        Logger.reset();
    }

    void drawButton(Tile location, String text, float size, boolean enabled, boolean hoverable) {
        drawButton(Theme.theme, location, text, size, enabled, hoverable);
    }

    void drawButton(Theme theme, Tile location, String text, float size, boolean enabled, boolean hoverable) {
        if(enabled && hoverable && location.mouseInTile()) {
            fill(mousePressed ? theme.button_click_color : theme.button_hover_color);
        } else {
            fill(theme.background_color);
        }
        strokeWeight(2);
        stroke(theme.line_color);
        rect(location.x(), location.y(), location.w(), location.h());
        fill(enabled ? theme.text_color : theme.grayed_text_color);
        textSize(size * width);
        text(text, location.cx(), location.cy());
    }

    String trickMode() {
        return switch(trick_mode) {
            case 0 -> "Off";
            case 1 -> "Down/Up";
            case 2 -> "Down";
            case 3 -> "Up/Down";
            case 4 -> "Up";
            case 5 -> "Split";
            case 6 -> "Custom";
            default -> "Undefined";
        };
    }

    PImage trumpIcon() {
        return switch(trump_suit) {
            case 1 -> spades;
            case 2 -> clubs;
            case 3 -> hearts;
            case 4 -> diamonds;
            case 5 -> dots;
            case 6 -> crosses;
            default -> null;
        };
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
        String file = DATA_PATH + "saves" + FILE_SEPARATOR + timestamp + ".txt";
        saveStrings(file, out);
        last_save = file;
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
            p.has_bid = !p.has_bid;
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

    void handleSuitsChange(boolean direction) {
        if(direction) {
            if(suits < MAX_SUITS) {
                suits++;
                updatePlayers(true);
            } else {
                displayError("The maximum number of suits is " + MAX_SUITS);
            }
        } else {
            if((suits - 1) * cards_per_suit >= MAX_PLAYERS) {
                suits--;
                updatePlayers(true);
            } else {
                displayError("The minimum deck size is " + MAX_PLAYERS);
            }
        }
    }

    void handleCardsPerSuitChange(boolean direction) {
        if(direction) {
            if(cards_per_suit < MAX_CARDS_PER_SUIT) {
                cards_per_suit++;
                updatePlayers(true);
            } else {
                displayError("The maximum cards per suit is " + MAX_CARDS_PER_SUIT);
            }
        } else {
            if(suits * (cards_per_suit - 1) >= MAX_PLAYERS) {
                cards_per_suit--;
                updatePlayers(true);
            } else {
                displayError("The minimum deck size is " + MAX_PLAYERS);
            }
        }
    }

    void handleTrickModeChange(boolean direction) {
        if(direction) {
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
        updatePlayers(true);
    }

    void handleStartingPointChange(boolean direction) {
        if(direction) {
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

    void handleAddPlayer() {
        if(players.size() < MAX_PLAYERS) {
            if(selected_player == -1) {
                players.add(new Player().setColor(Theme.theme.getPlayerColor(players.size())).setTile(setup_tiles[players.size()]));
                updatePlayers(false);
            } else {
                players.add(selected_player, new Player().setColor(Theme.theme.getPlayerColor(players.size())).setTile(setup_tiles[players.size()]));
                selected_player++;
                updatePlayers(false);
            }
            current_screen = Screen.SETUP_TO_BIDDING;
        } else {
            displayError("The maximum number of players is " + MAX_PLAYERS);
        }
    }

    void handleRemovePlayer() {
        if(players.size() > 2) {
            if(selected_player == -1) {
                displayError("Must select a player to remove");
            } else {
                players.remove(selected_player);
                selected_player--;
                updatePlayers(false);
            }
        } else {
            displayError("The minimum number of players is 2");
        }
    }

    void handleChangeScore(boolean direction, int amount) {
        if(selected_player == -1) {
            displayError("Must select a player to change score");
        } else {
            players.get(selected_player).score += (direction ? amount : -amount);
        }
    }

    void handleBeginGame() {
        if(current_screen == Screen.SETUP_TO_BIDDING) {
            current_screen = Screen.BIDDING;
        } else if(current_screen == Screen.SETUP_TO_TAKING) {
            current_screen = Screen.TAKING;
        }
        for(int i = 0; i < players.size(); i++) {
            players.get(i).setTile(game_tiles[i]);
        }
        selected_player = -1;
    }

    void handleSetup() {
        if(current_screen == Screen.BIDDING) {
            current_screen = Screen.SETUP_TO_BIDDING;
        } else if(current_screen == Screen.TAKING) {
            current_screen = Screen.SETUP_TO_TAKING;
        }
        for(int i = 0; i < players.size(); i++) {
            players.get(i).setTile(setup_tiles[i]);
        }
    }

    void handleChangeBids() {
        if(current_screen == Screen.BIDDING) {
            displayError("Already changing bids");
        } else {
            current_screen = Screen.BIDDING;
        }
    }

    void handleFinishBidding() {
        int total_bid = 0;
        boolean all_players_bid = true;
        for(Player p : players) {
            total_bid += p.bid;
            all_players_bid &= p.has_bid;
        }
        if(all_players_bid) {
            if(trick_mode == 0 || total_bid != tricks[trick_index] || (keyPressed && key == ENTER && mouseButton == RIGHT)) {
                current_screen = Screen.TAKING;
            } else {
                displayError("Tricks bid can't equal tricks dealt - override with enter + right click");
            }
        } else {
            displayError("Not all players have a bid entered");
        }
    }

    void handleFinishRound() {
        int total_taken = 0;
        for(Player player : players) {
            total_taken += player.taken;
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
                    current_screen = Screen.GAME_OVER;
                    saveRecord();
                    trick_index--;
                    return;
                }
            }
            trump_suit = 0;
            current_screen = Screen.BIDDING;
        } else {
            displayError("Tricks taken must equal tricks dealt - override with enter + right click");
        }
    }

    void handleEndGame() {
        if(keyPressed && key == ENTER && mouseButton == RIGHT) {
            current_screen = Screen.GAME_OVER;
            saveRecord();
        } else {
            displayError("End game? Confirm with enter + right click");
        }
    }

    void handleTrumpButton() {
        trump_suit += mouseButton == LEFT ? 1 : -1;
        if(trump_suit < 0) {
            trump_suit = 6;
        }
        if(trump_suit > 6) {
            trump_suit = 0;
        }
    }

    void openLatestSave() {
        if(last_save == null) {
            displayError("No save found");
            return;
        }
        try {
            java.awt.Desktop.getDesktop().open(new File(last_save));
        } catch(Exception e) {
            displayError("Cannot open file: " + e.getMessage());
        }
    }

    // Approximate fix for vertically-centered text to seem slightly too low
    @Override
    public void text(String str, float x, float y) {
        if(g.textAlignY == CENTER) {
            super.text(str, x, y - g.textSize * 0.15f);
        } else {
            super.text(str, x, y);
        }
    }

    @Override
    public void text(int i, float x, float y) {
        text(String.valueOf(i), x, y);
    }

    // Suppress unnecessary error messages
    @Override
    public String[] loadStrings(String filename) {
        System.setErr(placebo_output);
        String[] strings = super.loadStrings(filename);
        System.setErr(system_err);
        return strings;
    }

    // Try loading from both locations, while suppressing unnecessary error messages
    @Override
    public PImage loadImage(String filename) {
        System.setErr(placebo_output);
        String external = DATA_PATH + filename.replace("/", FILE_SEPARATOR);
        PImage image = super.loadImage(external);
        if(image == null) {
            image = super.loadImage(filename);
        }
        if(image == null) {
            System.err.println("Failed to load image: " + filename + " - cannot start program.");
            System.exit(1);
        }
        System.setErr(system_err);
        return image;
    }

    public static void main(String[] args) {
        PApplet.main("OhHellScoreboardV2");
    }

    public void settings() {
        if(debug) {
            size(960, 540);
        } else {
            fullScreen();
        }
    }

    @Override
    public void setup() {
        if(debug) {
            println("System properties:");
            System.getProperties().list(System.out);
        }
        Tile.sc = this;
        Logger.sc = this;
        Theme.sc = this;
        StateIO.sc = this;
        Player.sc = this;
        frameRate(30);
        aspect_ratio = (float) width / height;
        setInitialValues();
        try {
            StateIO.loadState(DATA_PATH + "latest");
        } catch(Exception e) {
            System.err.println("Exception loading save: " + e);
            setInitialValues();
            StateIO.saveState(DATA_PATH + "latest");
        }
        spades = loadImage("assets/spades.png");
        hearts = loadImage("assets/hearts.png");
        clubs = loadImage("assets/clubs.png");
        diamonds = loadImage("assets/diamonds.png");
        dots = loadImage("assets/dots.png");
        crosses = loadImage("assets/crosses.png");
        font = createFont("assets/NTSomic-Bold.ttf", 64);
        textFont(font);
    }

    @Override
    public void draw() {
        int frametime = millis() - millis_last_frame;
        millis_last_frame = millis();
        frc++;
        low_framerate_cooldown--;
        if(low_framerate_cooldown == 0) {
            frameRate(2);
        }
        background(Theme.theme.background_color);
        strokeWeight(2);
        stroke(Theme.theme.line_color);
        textAlign(CENTER, CENTER);
        if(current_screen.isSetup()) {
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
                textSize(width * 0.05f);
                text(p.name.equals("") ? ("Player " + (i + 1)) : p.name, p.tile.x() + width * 0.01f, p.tile.cy());
                textAlign(CENTER, CENTER);
                textSize(width * 0.03f);
                fill(p.display_color);
                text(p.score, p.tile.mx() - width * 0.04f, p.tile.cy());
            }
            boolean popup_shown = current_window != Window.NONE;
            drawButton(add_player_button, selected_player == -1 ? "Add Player" : "Add Player Before", 0.02f, players.size() < MAX_PLAYERS, !popup_shown);
            drawButton(remove_player_button, "Remove Player", 0.02f, selected_player != -1 && players.size() > 2, !popup_shown);
            drawButton(one_point_button, "Add/Remove 1 pt", 0.02f, selected_player != -1, !popup_shown);
            drawButton(ten_point_button, "Add/Remove 10 pts", 0.02f, selected_player != -1, !popup_shown);
            drawButton(custom_tricks_button, "Trick Options", 0.02f, true, !popup_shown);
            drawButton(reset_button, "Reset", 0.02f, true, !popup_shown);
            drawButton(theme_button, "Themes", 0.02f, true, !popup_shown);
            drawButton(begin_game_button, "Begin Game", 0.02f, true, !popup_shown);
            if(current_window == Window.TRICKS) {
                fill(Theme.theme.popup_background_color, 230);
                stroke(Theme.theme.line_color);
                rect(popup_window.x(), popup_window.y(), popup_window.w(), popup_window.h());
                drawButton(close_popup_button, "X", 0.02f, true, true);
                fill(Theme.theme.text_color);
                textSize(width * 0.05f);
                text("Trick Customization", popup_window.cx(), popup_window.y() + height * 0.083f);
                textAlign(CENTER, TOP);
                textSize(width * 0.02f);
                text("Number of suits: " + suits + "\nCards per suit: " + cards_per_suit + "\nTotal cards in deck: " + (suits * cards_per_suit) +
                        "\nTrick mode: " + trickMode() + "\nPreview:", popup_window.cx(), popup_window.y() + width * 0.1f);
                StringBuilder preview = new StringBuilder();
                fill(Theme.theme.error_text_color);
                if(trick_mode == 0) {
                    preview = new StringBuilder("Trick sequence disabled; some checks will not function");
                } else if(trick_mode == 6) {
                    preview = new StringBuilder("Custom trick sequences are not currently available.");
                } else {
                    fill(Theme.theme.text_color);
                    for(int i = 0; i < tricks.length; i++) {
                        if(i == trick_index) {
                            preview.append("*");
                        }
                        preview.append(tricks[i]);
                        if(i < tricks.length - 1) {
                            preview.append(", ");
                        }
                    }
                }
                if(textWidth(preview.toString()) > popup_window.w() * 0.9) {
                    textSize(width * 0.02f * 0.9f * popup_window.w() / textWidth(preview.toString()));
                }
                textAlign(CENTER, CENTER);
                text(preview.toString(), popup_window.cx(), popup_window.y() + width * 0.26f);
                drawButton(number_suits_button, "Number of Suits", 0.015f, true, true);
                drawButton(cards_per_suit_button, "Cards per Suit", 0.015f, true, true);
                drawButton(trick_mode_button, "Trick Mode", 0.015f, true, true);
                drawButton(starting_point_button, "Starting Point", 0.015f, true, true);
            } else if(trick_mode == 6) {
                trick_mode = 0;
            }
            if(current_window == Window.THEMES) {
                fill(Theme.theme.popup_background_color, 230);
                stroke(Theme.theme.line_color);
                rect(popup_window.x(), popup_window.y(), popup_window.w(), popup_window.h());
                drawButton(close_popup_button, "X", 0.02f, true, true);
                drawButton(refresh_themes_button, "", 0.02f, true, true);
                noFill();
                stroke(Theme.theme.text_color);
                strokeWeight(width * 0.002f);
                arc(refresh_themes_button.cx(), refresh_themes_button.cy(), refresh_themes_button.w() * 0.4f, refresh_themes_button.w() * 0.4f, -5, 0);
                noStroke();
                fill(Theme.theme.text_color);
                triangle(refresh_themes_button.cx() + refresh_themes_button.w() * 0.1f, refresh_themes_button.cy(),
                        refresh_themes_button.cx() + refresh_themes_button.w() * 0.3f, refresh_themes_button.cy(),
                        refresh_themes_button.cx() + refresh_themes_button.w() * 0.2f, refresh_themes_button.cy() + refresh_themes_button.h() * 0.2f);
                textSize(width * 0.05f);
                text("Theme Selector", popup_window.cx(), popup_window.y() + height * 0.083f);
                float box_left = popup_window.x() + width * 0.05f;
                float box_right = popup_window.mx() - width * 0.05f;
                float box_width = box_right - box_left;
                float box_top = popup_window.y() + height * 0.15f;
                float row_height = width * 0.035f;
                int rows = Theme.themes.size() + 1;
                for(int i = 0; i < Theme.themes.size(); i++) {
                    fill(Theme.themes.get(i).background_color);
                    rect(box_left + box_width * 0.7f, box_top + row_height * (i + 1), box_width * 0.3f, row_height);
                }
                strokeWeight(2);
                stroke(Theme.theme.line_color);
                fill(Theme.theme.text_color);
                for(int i = 0; i <= rows; i++) {
                    line(box_left, box_top + i * row_height, box_right, box_top + i * row_height);
                }
                line(box_left, box_top, box_left, box_top + rows * row_height);
                line(box_left + box_width * 0.3f, box_top, box_left + box_width * 0.3f, box_top + rows * row_height);
                line(box_left + box_width * 0.7f, box_top, box_left + box_width * 0.7f, box_top + rows * row_height);
                line(box_right, box_top, box_right, box_top + rows * row_height);
                textSize(width * 0.02f);
                text("Theme", box_left + box_width * 0.15f, box_top + row_height * 0.5f);
                text("File", box_left + box_width * 0.5f, box_top + row_height * 0.5f);
                text("Colors", box_left + box_width * 0.85f, box_top + row_height * 0.5f);
                for(int i = 0; i < Theme.themes.size(); i++) {
                    textSize(width * 0.02f);
                    fill(Theme.theme_index == i ? Theme.theme.highlight_text_color : Theme.theme.text_color);
                    Theme current = Theme.themes.get(i);
                    text(current.name, box_left + box_width * 0.15f, box_top + row_height * (i + 1.5f));
                    text(current.file, box_left + box_width * 0.5f, box_top + row_height * (i + 1.5f));
                    for(int j = 0; j < MAX_PLAYERS; j++) {
                        fill(current.getPlayerColor(j));
                        stroke(current.line_color);
                        rect(box_left + box_width * (0.75f + 0.02f * j), box_top + row_height * (i + 1.1f), box_width * 0.015f, box_width * 0.015f);
                    }
                    fill(current.overbid_color);
                    textSize(width * 0.015f);
                    text("+", box_left + box_width * 0.725f, box_top + row_height * (i + 1.2f));
                    fill(current.underbid_color);
                    text("-", box_left + box_width * 0.975f, box_top + row_height * (i + 1.2f));
                    Tile popup_button = Tile.fromCoordinates(box_left + box_width * 0.72f, box_top + row_height * (i + 1.5f), box_left + box_width * 0.84f, box_top + row_height * (i + 1.9f));
                    drawButton(current, popup_button, "Pop-up", 0.01f, true, true);
                    Tile error_button = Tile.fromCoordinates(box_left + box_width * 0.86f, box_top + row_height * (i + 1.5f), box_left + box_width * 0.98f, box_top + row_height * (i + 1.9f));
                    drawButton(current, error_button, "Error", 0.01f, false, false);
                    if(mousePressed) {
                        if(popup_button.mouseInTile()) {
                            fill(current.popup_background_color, 230);
                            stroke(current.line_color);
                            rect(box_left + box_width * 0.75f, box_top + row_height * (i + 1.1f), box_width * 0.2f, row_height * 0.8f);
                            fill(current.text_color);
                            text("Pop-up Window", box_left + box_width * 0.85f, box_top + row_height * (i + 1.5f));
                        } else if(error_button.mouseInTile()) {
                            fill(current.popup_background_color, 230);
                            stroke(current.line_color);
                            rect(box_left + box_width * 0.75f, box_top + row_height * (i + 1.1f), box_width * 0.2f, row_height * 0.8f);
                            fill(current.error_text_color);
                            text("Error Message", box_left + box_width * 0.85f, box_top + row_height * (i + 1.5f));
                        }
                    }
                }
                for(int i = 0; i < Theme.themes.size(); i++) {
                    Theme current = Theme.themes.get(i);
                    if(mouseX > box_left + box_width * 0.3 && mouseX < box_left + box_width * 0.7 && mouseY > box_top + row_height * (i + 1) && mouseY < box_top + row_height * (i + 2)) {
                        String file = current.directory + current.file;
                        // Test whether file is in the jar
                        if(!file.startsWith("/") && file.charAt(1) != ':') {
                            file = "jar/" + file;
                        }
                        textSize(width * 0.015f);
                        fill(Theme.theme.background_color);
                        stroke(Theme.theme.line_color);
                        float tw = textWidth(file);
                        float px = (mouseX + tw < width * 0.98f) ? mouseX : (width * 0.98f - tw);
                        rect(px, mouseY, tw + width * 0.02f, width * 0.03f);
                        fill(Theme.theme.text_color);
                        textAlign(LEFT, CENTER);
                        text(file, px + width * 0.01f, mouseY + width * 0.015f);
                        textAlign(CENTER, CENTER);
                    }
                }
            }
        } else if(current_screen.usesGameTiles()) {
            textAlign(CENTER, CENTER);
            int total_bid = 0;
            int total_taken = 0;
            for(int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                total_bid += p.bid;
                total_taken += p.taken;
                fill(Theme.theme.background_color);
                rect(p.tile.x(), p.tile.y(), p.tile.w(), p.tile.h());
                fill(p.display_color);
                textSize(game_tiles[0].w() * 0.1f);
                text(p.name.equals("") ? ("Player " + (i + 1)) : p.name, p.tile.cx(), p.tile.y() + p.tile.h() * 0.167f);
                textSize(p.tile.h() * 0.5f);
                text(p.score, p.tile.cx(), p.tile.my() - p.tile.h() * 0.43f);
                if(current_screen != Screen.GAME_OVER) {
                    textSize(game_tiles[0].w() * 0.04f);
                    text("Bid", p.tile.x() + p.tile.w() * 0.125f, p.tile.my() - game_tiles[0].w() * 0.167f);
                    if(p.has_bid) {
                        textSize(game_tiles[0].w() * 0.1f);
                        text(p.bid, p.tile.x() + p.tile.w() * 0.125f, p.tile.my() - game_tiles[0].w() * 0.1f);
                    }
                    if(current_screen == Screen.TAKING) {
                        textSize(game_tiles[0].w() * 0.04f);
                        text("Taken", p.tile.mx() - p.tile.w() * 0.125f, p.tile.my() - game_tiles[0].w() * 0.167f);
                        textSize(game_tiles[0].w() * 0.1f);
                        text(p.taken, p.tile.mx() - p.tile.w() * 0.125f, p.tile.my() - game_tiles[0].w() * 0.1f);
                    }
                }
            }
            if(current_screen != Screen.GAME_OVER) {
                drawButton(setup_button, "Setup", 0.02f, true, true);
                drawButton(change_bids_button, "Change Bids", 0.02f, current_screen == Screen.TAKING, true);
                drawButton(proceed_button, "Proceed", 0.02f, trick_mode == 0 || (current_screen == Screen.BIDDING && total_bid != tricks[trick_index]) || (current_screen == Screen.TAKING && total_taken == tricks[trick_index]), true);
                drawButton(end_game_button, "End Game", 0.02f, true, true);
                textSize(width * 0.01f);
                fill(Theme.theme.text_color);
                text("Deal", width * 0.72f, height * 0.87f);
                text("Bid", width * 0.8f, height * 0.87f);
                text("Taken", width * 0.88f, height * 0.87f);
                text("Trump", width * 0.96f, height * 0.87f);
                textSize(width * 0.05f);
                text(trick_mode == 0 ? "--" : String.valueOf(tricks[trick_index]), width * 0.72f, height * 0.93f);
                if(current_screen == Screen.TAKING) {
                    if(trick_mode != 0) {
                        if(total_bid < tricks[trick_index]) {
                            fill(Theme.theme.underbid_color);
                        } else {
                            fill(Theme.theme.overbid_color);
                        }
                    }
                }
                text(total_bid, width * 0.8f, height * 0.93f);
                fill(Theme.theme.text_color);
                text(total_taken, width * 0.88f, height * 0.93f);
                PImage trump_icon = trumpIcon();
                if(trump_icon != null) {
                    image(trump_icon, trump_suit_bounding_box.x() + trump_suit_bounding_box.w() * 0.25f, trump_suit_bounding_box.y() + trump_suit_bounding_box.h() * 0.25f, trump_suit_bounding_box.w() * 0.5f, trump_suit_bounding_box.h() * 0.5f);
                }
            } else {
                drawButton(statistics_button, "Statistics", 0.02f, true, true);
                drawButton(restart_button, "Restart", 0.02f, true, true);
                textSize(width * 0.05f);
                fill(Theme.theme.text_color);
                text("Game Over", width * 0.5f, height * 0.92f);
            }
        } else if(current_screen == Screen.STATISTICS) {
            drawButton(statistics_button, "Open Save", 0.02f, true, true);
            drawButton(restart_button, "Restart", 0.02f, true, true);
        }
        if(error_frames > 0) {
            fill(Theme.theme.popup_background_color, 230);
            stroke(Theme.theme.line_color);
            if(error_frames <= 15) {
                fill(Theme.theme.popup_background_color, 16 * error_frames);
                stroke(Theme.theme.line_color, 16 * error_frames);
            }
            textSize(width * 0.025f);
            textAlign(CENTER, CENTER);
            rect(width * 0.45f - 0.5f * textWidth(error_message), height * 0.45f, width * 0.1f + textWidth(error_message), height * 0.1f);
            fill(Theme.theme.error_text_color);
            if(error_frames <= 25) {
                fill(Theme.theme.error_text_color, 10 * error_frames);
            }
            text(error_message, width * 0.5f, height * 0.5f);
            error_frames--;
            low_framerate_cooldown++;
        }
        if(debug) {
            textAlign(LEFT, TOP);
            textSize(20);
            fill(Theme.theme.text_color);
            text("Milliseconds since last frame: " + frametime, 2, 2);
        }
    }

    @Override
    public void keyPressed() {
        resetFramerateCooldown();
    }

    @Override
    public void keyTyped() {
        if(key == ESC) {
            return;
        }
        resetFramerateCooldown();
        if(current_screen.isSetup()) {
            if(current_window == Window.NONE) {
                if(key == TAB) {
                    selected_player++;
                    if(selected_player >= players.size()) {
                        selected_player = 0;
                    }
                    return;
                } else if(key == ENTER && selected_player >= 0) {
                    editing_name = !editing_name;
                    return;
                }
                if(editing_name) {
                    String s = players.get(selected_player).name;
                    textSize(width * 0.05f);
                    if(key == ',') {
                        displayError("That character conflicts with the OHSC v2.0 autosave format");
                    } else if(key == BACKSPACE) {
                        if(s.length() > 0) {
                            players.get(selected_player).name = s.substring(0, s.length() - 1);
                        }
                    } else if(textWidth(s + key) <= MAX_NAME_WIDTH * width) {
                        players.get(selected_player).name += key;
                    } else {
                        displayError("The maximum name width is " + round(MAX_NAME_WIDTH * width) + " pixels");
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
                }
            }
        } else {
            if(key == 'b') {
                trump_suit = 0;
            } else if(key == 's') {
                trump_suit = 1;
            } else if(key == 'c') {
                trump_suit = 2;
            } else if(key == 'h') {
                trump_suit = 3;
            } else if(key == 'd') {
                trump_suit = 4;
            } else if(key == '.') {
                trump_suit = 5;
            } else if(key == 'x') {
                trump_suit = 6;
            }
            int i = Math.abs(getKeyValue(key));
            if(current_screen == Screen.BIDDING) {
                if(i != 0 && i - 1 < players.size()) {
                    i--;
                    Player p = players.get(i);
                    handleBidChange(p, getKeyValue(key) > 0);
                }
            } else {
                if(i != 0 && i - 1 < players.size()) {
                    i--;
                    Player p = players.get(i);
                    handleTakenChange(p, getKeyValue(key) > 0);
                }
            }
        }
        StateIO.saveState(DATA_PATH + "latest");
    }

    int getKeyValue(char k) {
        return switch(k) {
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case '0' -> 10;
            case 'q' -> -1;
            case 'w' -> -2;
            case 'e' -> -3;
            case 'r' -> -4;
            case 't' -> -5;
            case 'y' -> -6;
            case 'u' -> -7;
            case 'i' -> -8;
            case 'o' -> -9;
            case 'p' -> -10;
            default -> 0;
        };
    }

    @Override
    public void mouseMoved() {
        resetFramerateCooldown();
    }

    @Override
    public void mousePressed() {
        resetFramerateCooldown();
        if(current_screen == Screen.GAME_OVER) {
            if(restart_button.mouseInTile()) {
                setInitialValues();
            } else if(statistics_button.mouseInTile()) {
                current_screen = Screen.STATISTICS;
            }
        } else if(current_screen == Screen.STATISTICS) {
            if(restart_button.mouseInTile()) {
                setInitialValues();
            } else if(statistics_button.mouseInTile()) {
                openLatestSave();
            }
        } else if(current_screen.isSetup()) {
            if(current_window == Window.TRICKS) {
                if(close_popup_button.mouseInTile()) {
                    current_window = Window.NONE;
                } else if(number_suits_button.mouseInTile()) {
                    handleSuitsChange(mouseButton == LEFT);
                } else if(cards_per_suit_button.mouseInTile()) {
                    handleCardsPerSuitChange(mouseButton == LEFT);
                } else if(trick_mode_button.mouseInTile()) {
                    handleTrickModeChange(mouseButton == LEFT);
                } else if(starting_point_button.mouseInTile()) {
                    handleStartingPointChange(mouseButton == LEFT);
                }
            } else if(current_window == Window.THEMES) {
                if(close_popup_button.mouseInTile()) {
                    current_window = Window.NONE;
                } else if(refresh_themes_button.mouseInTile()) {
                    Theme.themes = null;
                    Theme.loadThemes();
                } else {
                    for(int i = 0; i < Theme.themes.size(); i++) {
                        Theme current = Theme.themes.get(i);
                        float box_left = popup_window.x() + width * 0.05f;
                        float box_right = popup_window.mx() - width * 0.05f;
                        float box_width = box_right - box_left;
                        float box_top = popup_window.y() + height * 0.15f;
                        float row_height = width * 0.035f;
                        if(mouseX > box_left + box_width * 0.3 && mouseX < box_left + box_width * 0.7 && mouseY > box_top + row_height * (i + 1) && mouseY < box_top + row_height * (i + 2)) {
                            String file = current.directory + current.file;
                            // Test whether file is in the jar
                            if(!file.startsWith("/") && file.charAt(1) != ':') {
                                displayError("Cannot edit internal file within program jar");
                                break;
                            }
                            try {
                                java.awt.Desktop.getDesktop().open(new File(file));
                            } catch(Exception e) {
                                displayError("Cannot open file: " + e.getMessage());
                            }
                            break;
                        } else if(mouseX > box_left && mouseX < box_left + box_width * 0.3f && mouseY > box_top + row_height * (i + 1) && mouseY < box_top + row_height * (i + 2)) {
                            Theme.setTheme(i);
                        }
                    }
                }
            } else {
                boolean clicked_player = false;
                if(add_player_button.mouseInTile()) {
                    handleAddPlayer();
                } else if(remove_player_button.mouseInTile()) {
                    handleRemovePlayer();
                } else if(one_point_button.mouseInTile()) {
                    handleChangeScore(mouseButton == LEFT, 1);
                } else if(ten_point_button.mouseInTile()) {
                    handleChangeScore(mouseButton == LEFT, 10);
                } else if(custom_tricks_button.mouseInTile()) {
                    current_window = Window.TRICKS;
                } else if(reset_button.mouseInTile()) {
                    setInitialValues();
                } else if(theme_button.mouseInTile()) {
                    current_window = Window.THEMES;
                } else if(begin_game_button.mouseInTile()) {
                    handleBeginGame();
                } else {
                    for(int i = 0; i < players.size(); i++) {
                        if(players.get(i).tile.mouseInTile()) {
                            clicked_player = true;
                            if(selected_player == i) {
                                editing_name = !editing_name;
                            } else {
                                selected_player = i;
                                editing_name = false;
                            }
                            break;
                        }
                    }
                    if(!clicked_player) {
                        selected_player = -1;
                    }
                }
                if(!clicked_player) {
                    editing_name = false;
                }
            }
        } else {
            for(Player p : players) {
                if(p.tile.mouseInTile()) {
                    if(current_screen == Screen.BIDDING) {
                        handleBidChange(p, mouseButton == LEFT);
                    } else {
                        handleTakenChange(p, mouseButton == LEFT);
                    }
                    break;
                }
            }
            if(setup_button.mouseInTile()) {
                handleSetup();
            } else if(change_bids_button.mouseInTile()) {
                handleChangeBids();
            } else if(proceed_button.mouseInTile()) {
                if(current_screen == Screen.BIDDING) {
                    handleFinishBidding();
                } else {
                    handleFinishRound();
                }
            } else if(end_game_button.mouseInTile()) {
                handleEndGame();
            } else if(trump_suit_bounding_box.mouseInTile()) {
                handleTrumpButton();
            }
        }
        StateIO.saveState(DATA_PATH + "latest");
    }
}

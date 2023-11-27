import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class OhHellScoreboardV2 extends PApplet {
    int frc = 0;
    final int MAX_PLAYERS = 10;
    final float MAX_NAME_WIDTH = 0.42f;
    final int MAX_SUITS = 12;
    final int MAX_CARDS_PER_SUIT = 50;
    final String FILE_SEPARATOR = System.getProperty("file.separator");
    final String DATA_PATH = getOSSpecificDataPath() + FILE_SEPARATOR + "OHSCv2" + FILE_SEPARATOR;
    String last_save = null;
    String error_message;
    int error_frames;
    int hands_played;
    int suits;
    int cards_per_suit;
    int trick_mode;
    int[] tricks;
    int trick_index;
    Suit trump_suit;
    int low_framerate_cooldown;
    PFont font;
    float aspect_ratio;
    int millis_last_frame = 0;
    final boolean debug = false;
    final PrintStream system_err = System.err;
    final PrintStream placebo_output = new PrintStream(new OutputStream() {
        @Override
        public void write(int b) {
            // Fake System.err.write to allow suppressing Processing error messages
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
        StatisticsScreen.INSTANCE.sortPlayers();
        Tile.setPlayerCountBasedTiles();
        if(ScreenManager.currentScreen() instanceof SetupScreen) {
            for(int i = 0; i < Player.count(); i++) {
                Player.get(i).setColor(Theme.theme.getPlayerColor(i));
            }
        } else {
            for(int i = 0; i < Player.count(); i++) {
                Player.get(i).setColor(Theme.theme.getPlayerColor(i));
            }
        }
        int max_deal = suits * cards_per_suit / Player.count();
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
        error_message = "";
        error_frames = 0;
        Player.players = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            new Player();
        }
        ScreenManager.initDefault();
        Window.init(this);
        for(Screen s : ScreenManager.all_screens) {
            s.init(this);
        }
        hands_played = 0;
        suits = 4;
        cards_per_suit = 13;
        trick_mode = 1;
        trump_suit = null;
        low_framerate_cooldown = 60;
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

    void saveRecord() {
        String[] out = new String[Player.count() + 1];
        out[0] = "Hands played: " + hands_played;
        for(int i = 0; i < Player.count(); i++) {
            Player p = Player.get(i);
            out[i + 1] = p.getName(i) + ": score=" + p.score + " bid=" + p.total_bid + " taken=" + p.total_taken + " bonus=" + p.bonuses + " set=" + p.times_set + " hands=" + p.hands_played;
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

    boolean enterRightClick() {
        return keyPressed && key == ENTER && mouseButton == RIGHT;
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

    @Override
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
        Theme.sc = this;
        ScreenManager.sc = this;
        frameRate(30);
        aspect_ratio = (float) width / height;
        setInitialValues();
        try {
            StateIO.loadState(DATA_PATH + "latest", this);
        } catch(Exception e) {
            System.err.println("Exception loading save: " + e);
            setInitialValues();
            StateIO.saveState(DATA_PATH + "latest", this);
        }
        Suit.loadAll(this);
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
        ScreenManager.currentScreen().draw(this);
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
        ScreenManager.currentScreen().keyTyped(this);
        StateIO.saveState(DATA_PATH + "latest", this);
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

    void setTrumpFromKey(char k) {
        if(k == 'b') {
            trump_suit = null;
        } else {
            Suit new_trump = Suit.getByKey(k);
            if(new_trump != null) {
                trump_suit = new_trump;
            }
        }
    }

    @Override
    public void mouseMoved() {
        resetFramerateCooldown();
    }

    @Override
    public void mousePressed() {
        resetFramerateCooldown();
        ScreenManager.currentScreen().mousePressed(this);
        StateIO.saveState(DATA_PATH + "latest", this);
    }
}

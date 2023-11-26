import java.io.File;

public class SetupScreen implements Screen {
    static final SetupScreen INSTANCE = new SetupScreen();
    Tile[] setup_tiles;
    Tile add_player_button, remove_player_button, edit_score_button, settings_button, custom_tricks_button, reset_button, theme_button, begin_game_button;
    Tile number_suits_button, cards_per_suit_button, trick_mode_button, starting_point_button;
    Tile theme_folder_button, refresh_themes_button, theme_scroll_up_button, theme_scroll_down_button;
    int selected_player;
    int theme_scroll_offset;
    String new_score;
    boolean editing_name;
    final int MAX_THEME_ROWS = 6;

    private SetupScreen() {
    }

    void handleAddPlayer(OhHellScoreboardV2 sc) {
        if(Player.count() < sc.MAX_PLAYERS) {
            if(selected_player == -1) {
                new Player();
                sc.updatePlayers(false);
            } else {
                new Player(selected_player);
                selected_player++;
                sc.updatePlayers(false);
            }
            ScreenManager.popScreen();
            ScreenManager.setScreen(BiddingScreen.INSTANCE);
            ScreenManager.pushScreen(SetupScreen.INSTANCE);
        } else {
            sc.displayError("The maximum number of players is " + sc.MAX_PLAYERS);
        }
    }

    void handleRemovePlayer(OhHellScoreboardV2 sc) {
        if(Player.count() > 2) {
            if(selected_player == -1) {
                sc.displayError("Must select a player to remove");
            } else {
                Player.players.remove(selected_player);
                selected_player--;
                sc.updatePlayers(false);
            }
        } else {
            sc.displayError("The minimum number of players is 2");
        }
    }

    void handleChangeScore(boolean direction, int amount, OhHellScoreboardV2 sc) {
        if(selected_player == -1) {
            sc.displayError("Must select a player to change score");
        } else {
            Player.get(selected_player).score += (direction ? amount : -amount);
        }
    }

    void handleBeginGame() {
        if(ScreenManager.currentScreen() instanceof SetupScreen) {
            ScreenManager.popScreen();
        }
        selected_player = -1;
    }

    @Override
    public void draw(OhHellScoreboardV2 sc) {
        for(int i = 0; i < Player.count(); i++) {
            Player p = Player.get(i);
            sc.noFill();
            if(i == selected_player) {
                sc.fill(p.display_color, 127);
            }
            sc.rect(setup_tiles[i].x(), setup_tiles[i].y(), setup_tiles[i].w(), setup_tiles[i].h());
            sc.fill(p.display_color);
            if(i == selected_player && editing_name) {
                sc.resetFramerateCooldown();
                if((sc.frc / 10) % 2 == 0) {
                    sc.fill(p.display_color, 127);
                }
            }
            sc.textAlign(sc.LEFT, sc.CENTER);
            sc.textSize(sc.width * 0.05f);
            sc.text(p.getName(i), setup_tiles[i].x() + sc.width * 0.01f, setup_tiles[i].cy());
            sc.textAlign(sc.CENTER, sc.CENTER);
            sc.textSize(sc.width * 0.03f);
            sc.fill(p.display_color);
            sc.text(p.score, setup_tiles[i].mx() - sc.width * 0.04f, setup_tiles[i].cy());
        }
        boolean popup_shown = Window.current != Window.NONE;
        sc.drawButton(add_player_button, selected_player == -1 ? "Add Player" : "Add Player Before", 0.02f, Player.count() < sc.MAX_PLAYERS, !popup_shown);
        sc.drawButton(remove_player_button, "Remove Player", 0.02f, selected_player != -1 && Player.count() > 2, !popup_shown);
        sc.drawButton(edit_score_button, "Edit score", 0.02f, selected_player != -1, !popup_shown);
        sc.drawButton(settings_button, "Settings", 0.02f, false, !popup_shown);
        sc.drawButton(custom_tricks_button, "Trick Options", 0.02f, true, !popup_shown);
        sc.drawButton(reset_button, "Reset", 0.02f, true, !popup_shown);
        sc.drawButton(theme_button, "Themes", 0.02f, true, !popup_shown);
        sc.drawButton(begin_game_button, "Begin Game", 0.02f, true, !popup_shown);
        if(Window.current == Window.TRICKS) {
            sc.fill(Theme.theme.popup_background_color, 230);
            sc.stroke(Theme.theme.line_color);
            sc.rect(Window.tile.x(), Window.tile.y(), Window.tile.w(), Window.tile.h());
            sc.drawButton(Window.close_button, "X", 0.02f, true, true);
            sc.fill(Theme.theme.text_color);
            sc.textSize(sc.width * 0.05f);
            sc.text("Trick Customization", Window.tile.cx(), Window.tile.y() + sc.height * 0.083f);
            sc.textAlign(sc.CENTER, sc.TOP);
            sc.textSize(sc.width * 0.02f);
            sc.text("Number of suits: " + sc.suits + "\nCards per suit: " + sc.cards_per_suit + "\nTotal cards in deck: " + (sc.suits * sc.cards_per_suit) +
                    "\nTrick mode: " + sc.trickMode() + "\nPreview:", Window.tile.cx(), Window.tile.y() + sc.width * 0.1f);
            StringBuilder preview = new StringBuilder();
            sc.fill(Theme.theme.error_text_color);
            if(sc.trick_mode == 0) {
                preview = new StringBuilder("Trick sequence disabled; some checks will not function");
            } else if(sc.trick_mode == 6) {
                preview = new StringBuilder("Custom trick sequences are not currently available.");
            } else {
                sc.fill(Theme.theme.text_color);
                for(int i = 0; i < sc.tricks.length; i++) {
                    if(i == sc.trick_index) {
                        preview.append("*");
                    }
                    preview.append(sc.tricks[i]);
                    if(i < sc.tricks.length - 1) {
                        preview.append(", ");
                    }
                }
            }
            if(sc.textWidth(preview.toString()) > Window.tile.w() * 0.9) {
                sc.textSize(sc.width * 0.02f * 0.9f * Window.tile.w() / sc.textWidth(preview.toString()));
            }
            sc.textAlign(sc.CENTER, sc.CENTER);
            sc.text(preview.toString(), Window.tile.cx(), Window.tile.y() + sc.width * 0.26f);
            sc.drawButton(number_suits_button, "Number of Suits", 0.015f, true, true);
            sc.drawButton(cards_per_suit_button, "Cards per Suit", 0.015f, true, true);
            sc.drawButton(trick_mode_button, "Trick Mode", 0.015f, true, true);
            sc.drawButton(starting_point_button, "Starting Point", 0.015f, true, true);
        } else if(sc.trick_mode == 6) {
            sc.trick_mode = 0;
        }
        if(Window.current == Window.THEMES) {
            sc.fill(Theme.theme.popup_background_color, 230);
            sc.stroke(Theme.theme.line_color);
            sc.rect(Window.tile.x(), Window.tile.y(), Window.tile.w(), Window.tile.h());
            sc.drawButton(Window.close_button, "X", 0.02f, true, true);
            sc.drawButton(refresh_themes_button, "", 0.02f, true, true);
            sc.drawButton(theme_folder_button, "F", 0.02f, true, true);
            sc.noFill();
            sc.stroke(Theme.theme.text_color);
            sc.strokeWeight(sc.width * 0.002f);
            sc.arc(refresh_themes_button.cx(), refresh_themes_button.cy(), refresh_themes_button.w() * 0.4f, refresh_themes_button.w() * 0.4f, -5, 0);
            sc.noStroke();
            sc.fill(Theme.theme.text_color);
            sc.triangle(refresh_themes_button.x(0.6), refresh_themes_button.cy(),
                    refresh_themes_button.x(0.8), refresh_themes_button.cy(),
                    refresh_themes_button.x(0.7), refresh_themes_button.y(0.7));
            sc.textSize(sc.width * 0.05f);
            sc.text("Theme Selector", Window.tile.cx(), Window.tile.y() + sc.height * 0.083f);
            float box_left = Window.tile.x() + sc.width * 0.05f;
            float box_right = Window.tile.mx() - sc.width * 0.05f;
            float box_width = box_right - box_left;
            float box_top = Window.tile.y() + sc.height * 0.15f;
            float row_height = sc.width * 0.035f;
            int rows = Math.min(MAX_THEME_ROWS, Theme.themes.size()) + 1;
            for(int i = theme_scroll_offset; i < Math.min(MAX_THEME_ROWS + theme_scroll_offset, Theme.themes.size()); i++) {
                int t = i - theme_scroll_offset;
                sc.fill(Theme.themes.get(i).background_color);
                sc.rect(box_left + box_width * 0.7f, box_top + row_height * (t + 1), box_width * 0.3f, row_height);
            }
            if(theme_scroll_offset > 0) {
                sc.drawButton(theme_scroll_up_button, "", 0.02f, true, true);
                sc.noStroke();
                sc.triangle(theme_scroll_up_button.x(0.2), theme_scroll_up_button.y(0.75),
                        theme_scroll_up_button.x(0.8), theme_scroll_up_button.y(0.75),
                        theme_scroll_up_button.x(0.5), theme_scroll_up_button.y(0.25));
            }
            if(theme_scroll_offset < Theme.themes.size() - MAX_THEME_ROWS) {
                sc.drawButton(theme_scroll_down_button, "", 0.02f, true, true);
                sc.noStroke();
                sc.triangle(theme_scroll_down_button.x(0.2), theme_scroll_down_button.y(0.25),
                        theme_scroll_down_button.x(0.8), theme_scroll_down_button.y(0.25),
                        theme_scroll_down_button.x(0.5), theme_scroll_down_button.y(0.75));
            }
            sc.strokeWeight(2);
            sc.stroke(Theme.theme.line_color);
            sc.fill(Theme.theme.text_color);
            for(int i = 0; i <= rows; i++) {
                sc.line(box_left, box_top + i * row_height, box_right, box_top + i * row_height);
            }
            sc.line(box_left, box_top, box_left, box_top + rows * row_height);
            sc.line(box_left + box_width * 0.3f, box_top, box_left + box_width * 0.3f, box_top + rows * row_height);
            sc.line(box_left + box_width * 0.7f, box_top, box_left + box_width * 0.7f, box_top + rows * row_height);
            sc.line(box_right, box_top, box_right, box_top + rows * row_height);
            sc.textSize(sc.width * 0.02f);
            sc.text("Theme", box_left + box_width * 0.15f, box_top + row_height * 0.5f);
            sc.text("File", box_left + box_width * 0.5f, box_top + row_height * 0.5f);
            sc.text("Colors", box_left + box_width * 0.85f, box_top + row_height * 0.5f);
            for(int i = theme_scroll_offset; i < Math.min(MAX_THEME_ROWS + theme_scroll_offset, Theme.themes.size()); i++) {
                int t = i - theme_scroll_offset;
                sc.textSize(sc.width * 0.02f);
                sc.fill(Theme.theme_index == i ? Theme.theme.highlight_text_color : Theme.theme.text_color);
                Theme current = Theme.themes.get(i);
                sc.text(current.name, box_left + box_width * 0.15f, box_top + row_height * (t + 1.5f));
                sc.text(current.file, box_left + box_width * 0.5f, box_top + row_height * (t + 1.5f));
                for(int j = 0; j < sc.MAX_PLAYERS; j++) {
                    sc.fill(current.getPlayerColor(j));
                    sc.stroke(current.line_color);
                    sc.rect(box_left + box_width * (0.75f + 0.02f * j), box_top + row_height * (t + 1.1f), box_width * 0.015f, box_width * 0.015f);
                }
                sc.fill(current.overbid_color);
                sc.textSize(sc.width * 0.015f);
                sc.text("+", box_left + box_width * 0.725f, box_top + row_height * (t + 1.2f));
                sc.fill(current.underbid_color);
                sc.text("-", box_left + box_width * 0.975f, box_top + row_height * (t + 1.2f));
                Tile popup_button = Tile.fromCoordinates(box_left + box_width * 0.72f, box_top + row_height * (t + 1.5f), box_left + box_width * 0.84f, box_top + row_height * (t + 1.9f));
                sc.drawButton(current, popup_button, "Pop-up", 0.01f, true, true);
                Tile error_button = Tile.fromCoordinates(box_left + box_width * 0.86f, box_top + row_height * (t + 1.5f), box_left + box_width * 0.98f, box_top + row_height * (t + 1.9f));
                sc.drawButton(current, error_button, "Error", 0.01f, false, false);
                if(sc.mousePressed) {
                    if(popup_button.mouseInTile()) {
                        sc.fill(current.popup_background_color, 230);
                        sc.stroke(current.line_color);
                        sc.rect(box_left + box_width * 0.75f, box_top + row_height * (t + 1.1f), box_width * 0.2f, row_height * 0.8f);
                        sc.fill(current.text_color);
                        sc.text("Pop-up Window", box_left + box_width * 0.85f, box_top + row_height * (t + 1.5f));
                    } else if(error_button.mouseInTile()) {
                        sc.fill(current.popup_background_color, 230);
                        sc.stroke(current.line_color);
                        sc.rect(box_left + box_width * 0.75f, box_top + row_height * (t + 1.1f), box_width * 0.2f, row_height * 0.8f);
                        sc.fill(current.error_text_color);
                        sc.text("Error Message", box_left + box_width * 0.85f, box_top + row_height * (t + 1.5f));
                    }
                }
            }
            if(theme_folder_button.mouseInTile()) {
                String dir = sc.DATA_PATH + "themes";
                sc.textSize(sc.width * 0.015f);
                sc.fill(Theme.theme.background_color);
                sc.stroke(Theme.theme.line_color);
                float tw = sc.textWidth(dir);
                float px = (sc.mouseX + tw < sc.width * 0.98f) ? sc.mouseX : (sc.width * 0.98f - tw);
                sc.rect(px, sc.mouseY, tw + sc.width * 0.02f, sc.width * 0.03f);
                sc.fill(Theme.theme.text_color);
                sc.textAlign(sc.LEFT, sc.CENTER);
                sc.text(dir, px + sc.width * 0.01f, sc.mouseY + sc.width * 0.015f);
                sc.textAlign(sc.CENTER, sc.CENTER);
            }
            for(int i = theme_scroll_offset; i < Math.min(MAX_THEME_ROWS + theme_scroll_offset, Theme.themes.size()); i++) {
                int t = i - theme_scroll_offset;
                Theme current = Theme.themes.get(i);
                if(sc.mouseX > box_left + box_width * 0.3 && sc.mouseX < box_left + box_width * 0.7 && sc.mouseY > box_top + row_height * (t + 1) && sc.mouseY < box_top + row_height * (t + 2)) {
                    String file = current.directory + current.file;
                    // Test whether file is in the jar
                    if(!file.startsWith("/") && file.charAt(1) != ':') {
                        file = "jar/" + file;
                    }
                    sc.textSize(sc.width * 0.015f);
                    sc.fill(Theme.theme.background_color);
                    sc.stroke(Theme.theme.line_color);
                    float tw = sc.textWidth(file);
                    float px = (sc.mouseX + tw < sc.width * 0.98f) ? sc.mouseX : (sc.width * 0.98f - tw);
                    sc.rect(px, sc.mouseY, tw + sc.width * 0.02f, sc.width * 0.03f);
                    sc.fill(Theme.theme.text_color);
                    sc.textAlign(sc.LEFT, sc.CENTER);
                    sc.text(file, px + sc.width * 0.01f, sc.mouseY + sc.width * 0.015f);
                    sc.textAlign(sc.CENTER, sc.CENTER);
                }
            }
        } else if(Window.current == Window.SCORE_EDITOR) {
            if(selected_player == -1) {
                selected_player = 0;
            }
            Player p = Player.get(selected_player);
            if(new_score == null) {
                new_score = String.valueOf(p.score);
            }
            sc.fill(Theme.theme.popup_background_color, 230);
            sc.stroke(Theme.theme.line_color);
            sc.rect(Window.tile.x(), Window.tile.y(), Window.tile.w(), Window.tile.h());
            sc.drawButton(Window.close_button, "X", 0.02f, true, true);
            String a = "Editing score for: ";
            String b = p.getName(selected_player);
            sc.textSize(sc.width * 0.03f);
            float tw = sc.textWidth(a + b);
            float tw1 = sc.textWidth(a);
            float pos = Window.tile.cx() - tw/2;
            sc.textAlign(sc.LEFT, sc.CENTER);
            sc.fill(Theme.theme.text_color);
            sc.text(a, pos, Window.tile.y(0.2));
            sc.fill(Theme.theme.getPlayerColor(selected_player));
            sc.text(b, pos + tw1, Window.tile.y(0.2));
            sc.textAlign(sc.CENTER, sc.CENTER);
            sc.fill(Theme.theme.text_color);
            sc.text("Current score: " + p.score, Window.tile.cx(), Window.tile.y(0.4));
            sc.text("New score: " + new_score, Window.tile.cx(), Window.tile.y(0.6));
            sc.text("Press Enter to confirm.", Window.tile.cx(), Window.tile.y(0.8));
        }
    }

    @Override
    public void mousePressed(OhHellScoreboardV2 sc) {
        if(Window.current == Window.TRICKS) {
            if(Window.close_button.mouseInTile()) {
                Window.current = Window.NONE;
            } else if(number_suits_button.mouseInTile()) {
                sc.handleSuitsChange(sc.mouseButton == sc.LEFT);
            } else if(cards_per_suit_button.mouseInTile()) {
                sc.handleCardsPerSuitChange(sc.mouseButton == sc.LEFT);
            } else if(trick_mode_button.mouseInTile()) {
                sc.handleTrickModeChange(sc.mouseButton == sc.LEFT);
            } else if(starting_point_button.mouseInTile()) {
                sc.handleStartingPointChange(sc.mouseButton == sc.LEFT);
            }
        } else if(Window.current == Window.THEMES) {
            if(Window.close_button.mouseInTile()) {
                Window.current = Window.NONE;
            } else if(refresh_themes_button.mouseInTile()) {
                Theme.themes = null;
                Theme.loadThemes();
                theme_scroll_offset = 0;
            } else if(theme_folder_button.mouseInTile()) {
                try {
                    java.awt.Desktop.getDesktop().open(new File(sc.DATA_PATH + "themes"));
                } catch(Exception e) {
                    sc.displayError("Cannot open folder: " + e.getMessage());
                }
            } else if(theme_scroll_up_button.mouseInTile() && theme_scroll_offset > 0) {
                theme_scroll_offset--;
            } else if(theme_scroll_down_button.mouseInTile() && theme_scroll_offset < Theme.themes.size() - MAX_THEME_ROWS) {
                theme_scroll_offset++;
            } else {
                for(int i = theme_scroll_offset; i < Math.min(MAX_THEME_ROWS + theme_scroll_offset, Theme.themes.size()); i++) {
                    int t = i - theme_scroll_offset;
                    Theme current = Theme.themes.get(i);
                    float box_left = Window.tile.x() + sc.width * 0.05f;
                    float box_right = Window.tile.mx() - sc.width * 0.05f;
                    float box_width = box_right - box_left;
                    float box_top = Window.tile.y() + sc.height * 0.15f;
                    float row_height = sc.width * 0.035f;
                    if(sc.mouseX > box_left + box_width * 0.3 && sc.mouseX < box_left + box_width * 0.7 && sc.mouseY > box_top + row_height * (t + 1) && sc.mouseY < box_top + row_height * (t + 2)) {
                        String file = current.directory + current.file;
                        // Test whether file is in the jar
                        if(!file.startsWith("/") && file.charAt(1) != ':') {
                            sc.displayError("Cannot edit internal file within program jar");
                            break;
                        }
                        try {
                            java.awt.Desktop.getDesktop().open(new File(file));
                        } catch(Exception e) {
                            sc.displayError("Cannot open file: " + e.getMessage());
                        }
                        break;
                    } else if(sc.mouseX > box_left && sc.mouseX < box_left + box_width * 0.3f && sc.mouseY > box_top + row_height * (t + 1) && sc.mouseY < box_top + row_height * (t + 2)) {
                        Theme.setTheme(i);
                    }
                }
            }
        } else if(Window.current == Window.SCORE_EDITOR) {
            if(Window.close_button.mouseInTile()) {
                Window.current = Window.NONE;
            }
        } else {
            boolean clicked_player = false;
            if(add_player_button.mouseInTile()) {
                handleAddPlayer(sc);
            } else if(remove_player_button.mouseInTile()) {
                handleRemovePlayer(sc);
            } else if(edit_score_button.mouseInTile()) {
                if(selected_player != -1) {
                    Window.current = Window.SCORE_EDITOR;
                    new_score = String.valueOf(Player.get(selected_player).score);
                } else {
                    sc.displayError("Must select a player to edit score");
                }
            } else if(settings_button.mouseInTile()) {
                sc.displayError("Not yet implemented");
                //TODO: Add settings button in place
            } else if(custom_tricks_button.mouseInTile()) {
                Window.current = Window.TRICKS;
            } else if(reset_button.mouseInTile()) {
                sc.setInitialValues();
            } else if(theme_button.mouseInTile()) {
                Window.current = Window.THEMES;
            } else if(begin_game_button.mouseInTile()) {
                handleBeginGame();
            } else {
                for(int i = 0; i < Player.count(); i++) {
                    if(setup_tiles[i].mouseInTile()) {
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
    }

    @Override
    public void init(OhHellScoreboardV2 sc) {
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
        edit_score_button = new Tile(0.04, 0.75, 0.24, 0.833);
        settings_button = new Tile(0.28, 0.75, 0.48, 0.833);
        custom_tricks_button = new Tile(0.52, 0.75, 0.72, 0.833);
        reset_button = new Tile(0.76, 0.75, 0.96, 0.833);
        theme_button = new Tile(0.52, 0.875, 0.72, 0.958);
        begin_game_button = new Tile(0.76, 0.875, 0.96, 0.958);
        number_suits_button = new Tile(0.193, 0.708, 0.327, 0.792);
        cards_per_suit_button = new Tile(0.353, 0.708, 0.487, 0.792);
        trick_mode_button = new Tile(0.513, 0.708, 0.647, 0.792);
        starting_point_button = new Tile(0.673, 0.708, 0.807, 0.792);
        theme_folder_button = Tile.fromCoordinates(Window.tile.x(0.81), Window.tile.y() + Window.tile.w()*0.02f, Window.tile.x(0.86), Window.tile.y() + Window.tile.w()*0.07f);
        refresh_themes_button = Tile.fromCoordinates(Window.tile.x(0.87), Window.tile.y() + Window.tile.w()*0.02f, Window.tile.x(0.92), Window.tile.y() + Window.tile.w()*0.07f);
        theme_scroll_up_button = Tile.fromCoordinates(Window.tile.x(0.1), Window.tile.y(0.21) - Window.tile.w()*0.05f, Window.tile.x(0.15), Window.tile.y(0.21));
        theme_scroll_down_button = Tile.fromCoordinates(Window.tile.x(0.1), Window.tile.y(0.89), Window.tile.x(0.15), Window.tile.y(0.89) + Window.tile.w()*0.05f);
        editing_name = false;
        selected_player = -1;
        theme_scroll_offset = 0;
    }

    @Override
    public void onLoad(OhHellScoreboardV2 sc) {
        theme_scroll_offset = 0;
    }
}

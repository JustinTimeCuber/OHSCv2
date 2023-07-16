public class SetupScreen {
    static void draw(OhHellScoreboardV2 sc) {

        for(int i = 0; i < Player.count(); i++) {
            Player p = Player.players.get(i);
            sc.noFill();
            if(i == sc.selected_player) {
                sc.fill(p.display_color, 127);
            }
            sc.rect(sc.setup_tiles[i].x(), sc.setup_tiles[i].y(), sc.setup_tiles[i].w(), sc.setup_tiles[i].h());
            sc.fill(p.display_color);
            if(i == sc.selected_player && sc.editing_name) {
                sc.resetFramerateCooldown();
                if((sc.frc / 10) % 2 == 0) {
                    sc.fill(p.display_color, 127);
                }
            }
            sc.textAlign(sc.LEFT, sc.CENTER);
            sc.textSize(sc.width * 0.05f);
            sc.text(p.name.equals("") ? ("Player " + (i + 1)) : p.name, sc.setup_tiles[i].x() + sc.width * 0.01f, sc.setup_tiles[i].cy());
            sc.textAlign(sc.CENTER, sc.CENTER);
            sc.textSize(sc.width * 0.03f);
            sc.fill(p.display_color);
            sc.text(p.score, sc.setup_tiles[i].mx() - sc.width * 0.04f, sc.setup_tiles[i].cy());
        }
        boolean popup_shown = sc.current_window != Window.NONE;
        sc.drawButton(sc.add_player_button, sc.selected_player == -1 ? "Add Player" : "Add Player Before", 0.02f, Player.count() < sc.MAX_PLAYERS, !popup_shown);
        sc.drawButton(sc.remove_player_button, "Remove Player", 0.02f, sc.selected_player != -1 && Player.count() > 2, !popup_shown);
        sc.drawButton(sc.one_point_button, "Add/Remove 1 pt", 0.02f, sc.selected_player != -1, !popup_shown);
        sc.drawButton(sc.ten_point_button, "Add/Remove 10 pts", 0.02f, sc.selected_player != -1, !popup_shown);
        sc.drawButton(sc.custom_tricks_button, "Trick Options", 0.02f, true, !popup_shown);
        sc.drawButton(sc.reset_button, "Reset", 0.02f, true, !popup_shown);
        sc.drawButton(sc.theme_button, "Themes", 0.02f, true, !popup_shown);
        sc.drawButton(sc.begin_game_button, "Begin Game", 0.02f, true, !popup_shown);
        if(sc.current_window == Window.TRICKS) {
            sc.fill(Theme.theme.popup_background_color, 230);
            sc.stroke(Theme.theme.line_color);
            sc.rect(sc.popup_window.x(), sc.popup_window.y(), sc.popup_window.w(), sc.popup_window.h());
            sc.drawButton(sc.close_popup_button, "X", 0.02f, true, true);
            sc.fill(Theme.theme.text_color);
            sc.textSize(sc.width * 0.05f);
            sc.text("Trick Customization", sc.popup_window.cx(), sc.popup_window.y() + sc.height * 0.083f);
            sc.textAlign(sc.CENTER, sc.TOP);
            sc.textSize(sc.width * 0.02f);
            sc.text("Number of suits: " + sc.suits + "\nCards per suit: " + sc.cards_per_suit + "\nTotal cards in deck: " + (sc.suits * sc.cards_per_suit) +
                    "\nTrick mode: " + sc.trickMode() + "\nPreview:", sc.popup_window.cx(), sc.popup_window.y() + sc.width * 0.1f);
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
            if(sc.textWidth(preview.toString()) > sc.popup_window.w() * 0.9) {
                sc.textSize(sc.width * 0.02f * 0.9f * sc.popup_window.w() / sc.textWidth(preview.toString()));
            }
            sc.textAlign(sc.CENTER, sc.CENTER);
            sc.text(preview.toString(), sc.popup_window.cx(), sc.popup_window.y() + sc.width * 0.26f);
            sc.drawButton(sc.number_suits_button, "Number of Suits", 0.015f, true, true);
            sc.drawButton(sc.cards_per_suit_button, "Cards per Suit", 0.015f, true, true);
            sc.drawButton(sc.trick_mode_button, "Trick Mode", 0.015f, true, true);
            sc.drawButton(sc.starting_point_button, "Starting Point", 0.015f, true, true);
        } else if(sc.trick_mode == 6) {
            sc.trick_mode = 0;
        }
        if(sc.current_window == Window.THEMES) {
            sc.fill(Theme.theme.popup_background_color, 230);
            sc.stroke(Theme.theme.line_color);
            sc.rect(sc.popup_window.x(), sc.popup_window.y(), sc.popup_window.w(), sc.popup_window.h());
            sc.drawButton(sc.close_popup_button, "X", 0.02f, true, true);
            sc.drawButton(sc.refresh_themes_button, "", 0.02f, true, true);
            sc.noFill();
            sc.stroke(Theme.theme.text_color);
            sc.strokeWeight(sc.width * 0.002f);
            sc.arc(sc.refresh_themes_button.cx(), sc.refresh_themes_button.cy(), sc.refresh_themes_button.w() * 0.4f, sc.refresh_themes_button.w() * 0.4f, -5, 0);
            sc.noStroke();
            sc.fill(Theme.theme.text_color);
            sc.triangle(sc.refresh_themes_button.cx() + sc.refresh_themes_button.w() * 0.1f, sc.refresh_themes_button.cy(),
                    sc.refresh_themes_button.cx() + sc.refresh_themes_button.w() * 0.3f, sc.refresh_themes_button.cy(),
                    sc.refresh_themes_button.cx() + sc.refresh_themes_button.w() * 0.2f, sc.refresh_themes_button.cy() + sc.refresh_themes_button.h() * 0.2f);
            sc.textSize(sc.width * 0.05f);
            sc.text("Theme Selector", sc.popup_window.cx(), sc.popup_window.y() + sc.height * 0.083f);
            float box_left = sc.popup_window.x() + sc.width * 0.05f;
            float box_right = sc.popup_window.mx() - sc.width * 0.05f;
            float box_width = box_right - box_left;
            float box_top = sc.popup_window.y() + sc.height * 0.15f;
            float row_height = sc.width * 0.035f;
            int rows = Theme.themes.size() + 1;
            for(int i = 0; i < Theme.themes.size(); i++) {
                sc.fill(Theme.themes.get(i).background_color);
                sc.rect(box_left + box_width * 0.7f, box_top + row_height * (i + 1), box_width * 0.3f, row_height);
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
            for(int i = 0; i < Theme.themes.size(); i++) {
                sc.textSize(sc.width * 0.02f);
                sc.fill(Theme.theme_index == i ? Theme.theme.highlight_text_color : Theme.theme.text_color);
                Theme current = Theme.themes.get(i);
                sc.text(current.name, box_left + box_width * 0.15f, box_top + row_height * (i + 1.5f));
                sc.text(current.file, box_left + box_width * 0.5f, box_top + row_height * (i + 1.5f));
                for(int j = 0; j < sc.MAX_PLAYERS; j++) {
                    sc.fill(current.getPlayerColor(j));
                    sc.stroke(current.line_color);
                    sc.rect(box_left + box_width * (0.75f + 0.02f * j), box_top + row_height * (i + 1.1f), box_width * 0.015f, box_width * 0.015f);
                }
                sc.fill(current.overbid_color);
                sc.textSize(sc.width * 0.015f);
                sc.text("+", box_left + box_width * 0.725f, box_top + row_height * (i + 1.2f));
                sc.fill(current.underbid_color);
                sc.text("-", box_left + box_width * 0.975f, box_top + row_height * (i + 1.2f));
                Tile popup_button = Tile.fromCoordinates(box_left + box_width * 0.72f, box_top + row_height * (i + 1.5f), box_left + box_width * 0.84f, box_top + row_height * (i + 1.9f));
                sc.drawButton(current, popup_button, "Pop-up", 0.01f, true, true);
                Tile error_button = Tile.fromCoordinates(box_left + box_width * 0.86f, box_top + row_height * (i + 1.5f), box_left + box_width * 0.98f, box_top + row_height * (i + 1.9f));
                sc.drawButton(current, error_button, "Error", 0.01f, false, false);
                if(sc.mousePressed) {
                    if(popup_button.mouseInTile()) {
                        sc.fill(current.popup_background_color, 230);
                        sc.stroke(current.line_color);
                        sc.rect(box_left + box_width * 0.75f, box_top + row_height * (i + 1.1f), box_width * 0.2f, row_height * 0.8f);
                        sc.fill(current.text_color);
                        sc.text("Pop-up Window", box_left + box_width * 0.85f, box_top + row_height * (i + 1.5f));
                    } else if(error_button.mouseInTile()) {
                        sc.fill(current.popup_background_color, 230);
                        sc.stroke(current.line_color);
                        sc.rect(box_left + box_width * 0.75f, box_top + row_height * (i + 1.1f), box_width * 0.2f, row_height * 0.8f);
                        sc.fill(current.error_text_color);
                        sc.text("Error Message", box_left + box_width * 0.85f, box_top + row_height * (i + 1.5f));
                    }
                }
            }
            for(int i = 0; i < Theme.themes.size(); i++) {
                Theme current = Theme.themes.get(i);
                if(sc.mouseX > box_left + box_width * 0.3 && sc.mouseX < box_left + box_width * 0.7 && sc.mouseY > box_top + row_height * (i + 1) && sc.mouseY < box_top + row_height * (i + 2)) {
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
        }
    }
}

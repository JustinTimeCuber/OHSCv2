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
        handleSuitsChange(mouseButton == LEFT);
        return;
      }
      if(cards_per_suit_button.mouseInTile()) {
        handleCardsPerSuitChange(mouseButton == LEFT);
        return;
      }
      if(trick_mode_button.mouseInTile()) {
        handleTrickModeChange(mouseButton == LEFT);
        return;
      }
      if(starting_point_button.mouseInTile()) {
        handleStartingPointChange(mouseButton == LEFT);
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
      handleAddPlayer();
      return;
    }
    if(remove_player_button.mouseInTile()) {
      handleRemovePlayer();
      return;
    }
    if(one_point_button.mouseInTile()) {
      handleChangeScore(mouseButton == LEFT, 1);
      return;
    }
    if(ten_point_button.mouseInTile()) {
      handleChangeScore(mouseButton == LEFT, 10);
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
      setTheme(++theme_index);
      return;
    }
    if(begin_game_button.mouseInTile()) {
      handleBeginGame();
      return;
    }
    selected_player = -1;
  } else {
    for(Player p : players) {
      if(p.tile.mouseInTile()) {
        if(bidding) {
          handleBidChange(p, mouseButton == LEFT);
        } else {
          handleTakenChange(p, mouseButton == LEFT);
        }
        return;
      }
    }
    if(setup_button.mouseInTile()) {
      handleSetup();
      return;
    }
    if(change_bids_button.mouseInTile()) {
      handleChangeBids();
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

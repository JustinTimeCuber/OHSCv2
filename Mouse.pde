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
          players.add(new Player("").setColor(theme.getPlayerColor(players.size())).setTile(setup_tiles[players.size()]));
          numberOfPlayersChanged(false);
        } else {
          players.add(selected_player, new Player("").setColor(theme.getPlayerColor(players.size())).setTile(setup_tiles[players.size()]));
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
          handleBidChange(p, mouseButton == LEFT);
          return;
        }
      }
    } else {
      for(int i = 0; i < players.size(); i++) {
        Player p = players.get(i);
        if(p.tile.mouseInTile()) {
          handleTakenChange(p, mouseButton == LEFT);
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

void mouseMoved() {
  resetFramerateCooldown();
}
void mousePressed() {
  resetFramerateCooldown();
  if(current_screen == Screen.GAME_OVER) {
    if(restart_button.mouseInTile()) {
      setInitialValues();
    }
    return;
  }
  if(current_screen.isSetup()) {
    if(current_window == Window.TRICKS) {
      if(close_popup_button.mouseInTile()) {
        current_window = Window.NONE;
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
      current_window = Window.TRICKS;
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
        if(current_screen == Screen.BIDDING) {
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
      if(current_screen == Screen.BIDDING) {
        handleFinishBidding();
      } else {
        handleFinishRound();
      }
      return;
    }
    if(end_game_button.mouseInTile()) {
      handleEndGame();
      return;
    }
    if(trump_suit_bounding_box.mouseInTile()) {
      handleTrumpButton();
      return;
    }
  }
}

void keyPressed() {
  resetFramerateCooldown();
}
void keyTyped() {
  resetFramerateCooldown();
  if(current_screen == Screen.GAME_OVER) {
    return;
  }
  if(current_screen.isSetup()) {
    if(current_window != Window.NONE) {
      return;
    }
    if(editing_name) {
      String s = players.get(selected_player).name;
      textSize(width*0.05);
      if(key == ',') {
        displayError("That character conflicts with the OHSC v2.0 autosave format");
        return;
      }
      if(key == BACKSPACE) {
        if(s.length() > 0) {
          players.get(selected_player).name = s.substring(0, s.length() - 1);
        }
      } else if(key == ENTER) {
        editing_name = false;
      } else if(textWidth(s + key) <= MAX_NAME_WIDTH*width) {
        players.get(selected_player).name += key;
      } else {
        displayError("The maximum name width is " + round(MAX_NAME_WIDTH*width) + " pixels");
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
      if(selected_player >= 0 && key == ENTER) {
        editing_name = true;
      }
    }
  } else {
    if(key == 's') {
      trump_suit = 1;
      return;
    }
    if(key == 'c') {
      trump_suit = 2;
      return;
    }
    if(key == 'h') {
      trump_suit = 3;
      return;
    }
    if(key == 'd') {
      trump_suit = 4;
      return;
    }
    if(key == 'o') {
      trump_suit = 5;
      return;
    }
    if(key == 'x') {
      trump_suit = 6;
      return;
    }
    if(current_screen == Screen.BIDDING) {
      int i = Math.abs(getKeyValue(key));
      if(i != 0 && i - 1 < players.size()) {
        i--;
        Player p = players.get(i);
        handleBidChange(p, getKeyValue(key) > 0);
        return;
      }
    } else {
      int i = Math.abs(getKeyValue(key));
      if(i != 0 && i - 1 < players.size()) {
        i--;
        Player p = players.get(i);
        handleTakenChange(p, getKeyValue(key) > 0);
        return;
      }
    }
  }
}
int getKeyValue(char k) {
  switch(k) {
    case '1': return 1;
    case '2': return 2;
    case '3': return 3;
    case '4': return 4;
    case '5': return 5;
    case '6': return 6;
    case '7': return 7;
    case '8': return 8;
    case '9': return 9;
    case '0': return 10;
    case 'q': return -1;
    case 'w': return -2;
    case 'e': return -3;
    case 'r': return -4;
    case 't': return -5;
    case 'y': return -6;
    case 'u': return -7;
    case 'i': return -8;
    case 'o': return -9;
    case 'p': return -10;
    default: return 0;
  }
}

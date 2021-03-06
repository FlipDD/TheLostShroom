void keyPressed() {
  if (key == ' ' && moomu != null) {
    moomu.jump = true;
  }

  if (key == ' ') {
    if (gamestate == GameState.MAIN_MENU) { 
      mg = new ObjectManager();
      map = new Map(this, level);
      gamestate = GameState.PLAYING;
    }
    if (gamestate == GameState.LOST) {
      mg = new ObjectManager();
      map = new Map(this, level);
      gamestate = GameState.PLAYING;
    }
  }

  if (key == 'n' || key == 'N' && gamestate == GameState.WIN) {
    if (level != 3) {
      level++;
      mg = new ObjectManager();
      map = new Map(this, level);
      gamestate = GameState.PLAYING;
    }
  }
  
   if (key == 'm' || key == 'M' && gamestate == GameState.WIN) {
    if (level != 1) {
      level--;
      mg = new ObjectManager();
      map = new Map(this, level);
      gamestate = GameState.PLAYING;
    }
  }
}

void keyReleased() {
  if (key == ' ' && moomu != null)  moomu.jump = false;
}

void mousePressed() {
  if (!shoot && moomu != null) {
    bullet = new Bullet(moomu.r, new PVector(0, 0), moomu.mouse.heading(), 1.25);
    shoot = true;
  }
}

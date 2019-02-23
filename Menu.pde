class Menu {

  Menu() {
  }

  void mainMenu() {
    background(255);
    pushMatrix();
    image(fmoomu, 170, height/2);
    image(fmoomu1, width - 170, height/2);
    scale(0.20);
    imageMode(CENTER);
    image(logo, 3500, 1350); 
    textSize(500);
    fill(255, 0, 0);
    text(" Press P to Play!", 1500, 3500);
    popMatrix();
  }

  void endMenu() {
    pushMatrix();
    background(255, 0, 0);
    textSize(100);
    fill(0);
    text("      You lost!", 300, height/2-100);
    image(fmoomu, width/2-20, height/2);
    text("Press P to restart", 300, height/2+250);
    popMatrix();
  }

  void winMenu() {
    pushMatrix();
    background(0, 200, 0);
    textSize(100);
    fill(0);
    text("      You win!", 300, height/2-100);
    image(fmoomu, width/2-20, height/2);
    textSize(75);
    text("Press N to play the next Level!", 140, height/2+250);
    popMatrix();
  }
}
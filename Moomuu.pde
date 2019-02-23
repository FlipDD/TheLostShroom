import ptmx.*;

Map map;
Camera camera;
Moomu moomu;
Bullet bullet;
Menu menu;
ObjectManager mg;
GameObject gameobject;
GameState gamestate;

PImage fmoomu, fmoomu1, logo, birdUp;
int level;
boolean shoot;

void setup() {
  size(1366, 768, P2D);
  frameRate(60); 
  logo = loadImage("logo.png");
  fmoomu = loadImage("fmoomu.png");
  fmoomu1 = loadImage("fmoomu1.png");
  birdUp = loadImage("birdupleft.png");
  menu =  new Menu();
  gamestate = GameState.MAIN_MENU;
  level = 1;
}

void draw() {
  switch (gamestate) {
    
  case MAIN_MENU:
    menu.mainMenu();
    break;

  case PLAYING:
    mg.run();
    break;

  case LOST:
    menu.endMenu();
    break;

  case WIN:
    menu.winMenu();
    break;
  }
  
  cursor(CROSS);
}
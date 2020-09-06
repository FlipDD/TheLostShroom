import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ptmx.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Moomuu extends PApplet {



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

public void setup() {
  //size(1360, 720, P2D);
  surface.setSize(1368, 720);  
  frameRate(60);
  logo = loadImage("logo.png");
  fmoomu = loadImage("fmoomu.png");
  fmoomu1 = loadImage("fmoomu1.png");
  birdUp = loadImage("birdupleft.png");
  menu =  new Menu();
  gamestate = GameState.MAIN_MENU;
  level = 1;
}

public void draw() {
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
class BridgeButton extends Button {
  float currentWidth, currentHeight, currentX, currentY;

  BridgeButton(float x, float y, float currentX, float currentY) {
    super(x, y);
    this.currentX = currentX;
    this.currentY = currentY;
    currentWidth = 450;
    currentHeight = 70;
    buttonPressed = false;
  }

  public void update() {
    handlingCollisions(); 
  }
  
  public void handlingCollisions() {
    if (buttonPressed) {
      //handling collisions moomu/bridge
      if (moomu.r.y + moomu.s.y / 2 >= currentY 
       && moomu.r.x + moomu.s.x / 2 >= currentX 
       && moomu.r.y - moomu.s.y / 2 <= currentY + currentHeight 
       && moomu.r.x - moomu.s.x / 2 <= currentX + currentWidth) {

        moomu.r.y = currentY - moomu.s.y / 2;
        moomu.v.y = 0;
        if (moomu.jump) moomu.jump();
      } 
      //handling collisions bullet/button
      if (shoot) {
        if (bullet.r.y + bullet.bulletHeight / 2 >= currentY 
         && bullet.r.x + bullet.bulletWidth / 2 >= currentX 
         && bullet.r.y - bullet.bulletHeight / 2 <= currentY + currentHeight 
         && bullet.r.x - bullet.bulletWidth / 2 <= currentX + currentWidth) bullet.v.y = -(bullet.v.y);
      }
    }
    openDoor();
  }

  public void display() {
    super.display();
    if (buttonPressed) {
      fill(90, 90, 90);
      rect(currentX, currentY, currentWidth, currentHeight);
    }
  }
}
class Bullet extends GameObject {
  PVector v, a;
  float angle, bulletDistance, lifetime, scale, bulletWidth, bulletHeight, mv;
  boolean collision, isShot;

  Bullet(PVector r, PVector s, float angle, float scale) {
    super(r, s);
    this.angle= angle;
    this.r = r.copy();
    v = PVector.fromAngle(angle).mult(18);
    a = new PVector(0, 0);
    this.scale = scale;
    s.x = 12 * scale;
    s.y = 12 * scale;
    lifetime = 145;
  }

  public void update() {
    v.add(a);    
    r.add(v);
    handlingCollisions();
    lifetime -= 1;
  }

  public void handlingCollisions() {
    for (Platform platform : map.platforms) {
      if (isCollidingWith(platform)) {

        switch(directionOfCollisionWith(platform)) {

        case LEFT:
          v.x = -v.x;
          break;

        case RIGHT:
          v.x = -v.x;
          break;

        case ABOVE:
          v.y = -v.y;
          break;    

        case BELOW:
          v.y = -v.y;
          break;
        }
      }
    }
  }

  public void display() {
    pushMatrix();
    stroke(0);
    translate(r.x, r.y);
    fill(255, 0, 0);
    ellipse(0, 0, s.x, s.y);
    popMatrix();
  }
}
class Button {
  float x, y, buttonRadius;
  boolean buttonPressed;
  
  Button(float x, float y) {
    this.x = x;
    this.y = y;
    buttonRadius = 60;
  }
  
  public void openDoor() {
    if (shoot) {
      if (dist(x, y, bullet.r.x, bullet.r.y) < 45) buttonPressed = true;
    }
  }
  
  public void display() {
    pushMatrix();
    stroke(0);
    if (!buttonPressed) {
      fill(170, 0, 0);
      ellipse(x, y, buttonRadius, buttonRadius);
    } else {
      fill(0, 170, 0);
      ellipse(x, y, buttonRadius / 2, buttonRadius / 2);
    }
    popMatrix();
  }
}
class Camera {
  PVector r;

  Camera(PVector p) {
    this.r = p.copy();
  }

  public void update() {
    if (moomu == null) return;
    if (r.x > moomu.r.x + 50)
      r.x = moomu.r.x + 50;
    else if (r.x < moomu.r.x - 50)
      r.x = moomu.r.x - 50;
  }

  public PVector worldFromDisplay(PVector pd) {
    return PVector.add(pd, new PVector(r.x - width / 2, r.y - height / 2));
  }

  public void apply() {
    translate(width/2 - r.x, height/2 - r.y);
  }
}
class DoorButton extends Button {
  float currentWidth, currentHeight, currentX, currentY;

  DoorButton(float x, float y, float currentX, float currentY) {
    super(x, y);
    this.currentX = currentX;
    this.currentY = currentY;
    currentWidth = 64;
    currentHeight = 128;
    buttonPressed = false;
  }

  public void update() {
    handlingCollisions();
  }
  
  public void handlingCollisions() {
    if (!buttonPressed) {
      //handlingCollisions moomu/door
      if (moomu.r.y + moomu.s.y / 2 >= currentY 
       && moomu.r.x + moomu.s.x / 2 >= currentX 
       && moomu.r.y - moomu.s.y / 2 <= currentY + currentHeight 
       && moomu.r.x - moomu.s.x / 2 <= currentX + currentWidth) {
        
        moomu.v.x = -(moomu.v.x);
      }
      //handling collisions bullet/door
      if (shoot) {  
        if (bullet.r.y + bullet.bulletHeight / 2 >= currentY 
         && bullet.r.x + bullet.bulletWidth / 2 >= currentX 
         && bullet.r.y - bullet.bulletHeight / 2 <= currentY + currentHeight 
         && bullet.r.x - bullet.bulletWidth / 2 <= currentX + currentWidth) bullet.v.x = -(bullet.v.x);
      }
    }
    openDoor();
  }

  public void display() {
    super.display();
    if (!buttonPressed) {
      fill(0);
      rect(currentX, currentY, currentWidth, currentHeight);
    }
  }
}
class Enemy extends GameObject {
  PVector v;
  boolean trigger, isDead;
  float moveDistance, scale;

  Enemy(PVector r, PVector s, boolean isDead) {
    super(r, s);
    this.isDead = isDead;
    trigger = false;
  }

  public void update() {
    if (shoot) {
      if (dist(bullet.r.x, bullet.r.y, r.x, r.y) <= 35) isDead = true;
    }
  }
  
    public void display() {
    
  }
}
class EnemyBird extends Enemy {
  private PVector v2;

  EnemyBird(PVector r, PVector s, boolean isDead) {
    super(r, s, isDead);
    v = new PVector(-2.5f, 5);
    v2 = new PVector(-0.5f, 0);
    s.x = birdUp.width * scale;
    s.y = birdUp.height * scale;
  }

  public void update() {
    super.update();
    
    handlingCollisions();

    moveDistance = dist(r.x, r.y, moomu.r.x, moomu.r.y);
    if (r.x - moomu.r.x < 700) r.add(v2);
    if (moveDistance < 300) trigger = true;
    if (trigger) r.add(v);
    if (moveDistance < 75) gamestate = GameState.LOST; //moomu.isDead = true;
  }

  public void handlingCollisions() {
    for (Platform platform : map.platforms) {
      if (isCollidingWith(platform)) {

        switch(directionOfCollisionWith(platform)) {

        case LEFT:
          v.x = -v.x;
          break;

        case RIGHT:
          v.x = -v.x;
          break;

        case ABOVE:
          v.y = -v.y;
          break;

        case BELOW:
          v.y = -v.y;
          break;
        }
      }
    }
  }

  public void display() {
    pushMatrix();
    imageMode(CENTER);
    image(birdUp, r.x, r.y);
    popMatrix();
  }
}
public class GameObject {
  PVector r;
  PVector s;

  GameObject(PVector r, PVector s) {
    this.r = r;
    this.s = s;
  }

  public boolean isCollidingWith(GameObject other) {
    return (r.x - s.x / 1.99f <= other.r.x + other.s.x / 2
         && r.x + s.x / 2 >= other.r.x - other.s.x / 1.98f
         && r.y - s.y / 1.99f <= other.r.y + other.s.y / 2
         && r.y + s.y / 2 >= other.r.y - other.s.y / 1.98f);
  }

  public CollisionDirection directionOfCollisionWith(GameObject other) {
    float nx = max(other.r.x - other.s.x / 1.99f, r.x - s.x / 1.99f);
    float ny = max(other.r.y - other.s.y / 2, r.y - s.y / 2);
    float nWidth = min(r.x + s.x / 1.99f, other.r.x + other.s.x / 1.99f) - nx;
    float nHeight = min(r.y + s.y / 2, other.r.y + other.s.y / 2) - ny;

    //check collision side
    if (nWidth < nHeight) {

      if (r.x >= other.r.x) {
        return CollisionDirection.LEFT;
      } else {
        return CollisionDirection.RIGHT ;
      }
    } else if (r.y <= other.r.y) {
      return CollisionDirection.BELOW;
    } else {
      return CollisionDirection.ABOVE;
    }
  }
}
//No time to improve this / data hiding

enum GameState {

  MAIN_MENU, 
    PLAYING,
    WIN, 
    LOST;
}

enum CollisionDirection {
  
  ABOVE, 
    BELOW, 
    LEFT, 
    RIGHT;
}
public void keyPressed() {
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

public void keyReleased() {
  if (key == ' ' && moomu != null)  moomu.jump = false;
}

public void mousePressed() {
  if (!shoot && moomu != null) {
    bullet = new Bullet(moomu.r, new PVector(0, 0), moomu.mouse.heading(), 1.25f);
    shoot = true;
  }
}
class Map {
  Ptmx map;
  ArrayList<Platform> platforms;
  ArrayList<Spike> spikes;

  Map(PApplet papplet, int level) {
    //depending on the current number of the level, get the right map and platforms / spikes position and size
    map = new Ptmx(papplet, "Level" + level + "l.tmx");
    map.setDrawMode(CENTER);
    map.setPositionMode("CANVAS");
    
    //platforms from the current map
    platforms = new ArrayList<Platform>();
    StringDict[] platformObjects = map.getObjects(1);
    for (StringDict platformObject : platformObjects) {
      float x = parseFloat(platformObject.get("x"));
      float y = parseFloat(platformObject.get("y"));
      float width = parseFloat(platformObject.get("width"));
      float height = parseFloat(platformObject.get("height"));
      platforms.add(new Platform(new PVector(x + width / 2, y + height / 2), new PVector(width, height)));
    }
    
    //spikes from the current map
    spikes = new ArrayList<Spike>();
    StringDict[] spikeObjects = map.getObjects(0);
    for (StringDict spikeObject : spikeObjects) {
      float x = parseFloat(spikeObject.get("x"));
      float y = parseFloat(spikeObject.get("y"));
      float width = parseFloat(spikeObject.get("width"));
      float height = parseFloat(spikeObject.get("height"));
      spikes.add(new Spike(new PVector(x + width / 2, y + height / 2), new PVector(width, height)));
    }
  }

  public void display() {
    map.draw(camera.r.x, camera.r.y);
    
    //display framerate (top left)
    fill(255);
    textSize(15);
    text(frameRate, camera.r.x - width/2, 15);
  }
}
class Menu {

  Menu() {
  }

  public void mainMenu() {
    background(255);
    pushMatrix();
    image(fmoomu, 170, height/2);
    image(fmoomu1, width - 170, height/2);
    scale(0.20f);
    imageMode(CENTER);
    image(logo, 3500, 1350); 
    textSize(500);
    fill(255, 0, 0);
    text(" Press [Space] to Play!", width/2, 3500);
    popMatrix();
  }

  public void endMenu() {
    pushMatrix();
    background(255, 0, 0);
    textSize(100);
    fill(0);
    text("      You lost!", 300, height/2-100);
    image(fmoomu, width/2-20, height/2);
    text("Press [Space] to restart", 140, height/2+250);
    popMatrix();
  }

  public void winMenu() {
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
public class Moomu extends GameObject {
  private PVector v, a, mouse;
  private float scale, duration;
  boolean collision, isDead, left, right, stickTrigger, jump, jumping, speeding;

  Moomu(PVector r, PVector s, float scale, boolean isDead) {
    super(r, s);
    v = new PVector(5, 0.2f);
    a = new PVector(0, 0.63f);
    this.scale = scale;
    this.isDead = isDead;
    s.x = fmoomu.width * scale;
    s.y = fmoomu.height * scale;
    isDead = false;
    duration = 225;
    collision = false;
  }

  public void update() {
    
    if (!collision) v.add(a);
    r.add(v);

    handlingCollisions();
    handlingSpikes();

    speedBoost();

    if (moomu.r.y < -100 || moomu.r.y > 764) gamestate = GameState.LOST;

    mouse = camera.worldFromDisplay(new PVector(mouseX, mouseY));
    mouse.sub(r);
    mouse.setMag(55);

    if (moomu.v.x >= 0) {
      right = true;
      left = false;
    } else {
      left = true;
      right = false;
    }
  }

  public void jump() {
    v.y = -14;
  }

  public void handlingCollisions() {
    for (Platform platform : map.platforms) {
      if (isCollidingWith(platform)) {
        collision = true;
        switch(directionOfCollisionWith(platform)) {

        case BELOW:
          v.y = 0;
          r.y = platform.r.y - platform.s.y / 2 - s.y / 2 + 1;
          if (jump) jump();
          break;

        case LEFT:
          v.x = -v.x;
          break;

        case RIGHT:
          v.x = -v.x;
          break;

        case ABOVE:
          v.y = a.y;
          break;
        }
      } else {
        collision = false;
      }
    }
  }

  public void handlingSpikes() {
    for (Spike spike : map.spikes) 
      if (isCollidingWith(spike)) gamestate = GameState.LOST;
  }

  public void speedBoost() {
    if (speeding) {
      if (moomu.v.x > 0) {
        moomu.v.x = 6.5f;
      } else moomu.v.x = -6.5f;
      duration--;
      if (duration < 0) speeding = false;
    } else {
      if (moomu.v.x >= 0) {
        moomu.v.x = 5;
      } else moomu.v.x = -5;
      duration = 225;
    }
  }

  public void display() {
    pushMatrix();
    translate(r.x, r.y);
    strokeWeight(1.8f);
    stroke(255, 255, 255, 40);
    line(0, 0, mouse.x, mouse.y);
    scale(scale);
    imageMode(CENTER);
    if (right) image(fmoomu, 0, 0);
    if (left) image(fmoomu1, 0, 0);
    popMatrix();
  }
}
class ObjectManager {
  ArrayList<Bullet> bullets;

  ArrayList <Enemy> enemies1;
  DoorButton doorButton10;

  ArrayList <Enemy> enemies2;
  DoorButton doorButton20;
  BridgeButton bridgeButton20;

  ArrayList <Enemy> enemies3;
  DoorButton doorButton30;
  BridgeButton bridgeButton30;
  SpeedBoost speedBoost30;
  SpeedBoost speedBoost31;

  ObjectManager() {
    //common to all levels
    bullets = new ArrayList<Bullet>();
    moomu = new Moomu(new PVector(width/2, height/2), new PVector(0, 0), .45f, false);  
    camera = new Camera(new PVector(moomu.r.x, height / 2)); 
    shoot = false;

    //level 1
    enemies1 = new ArrayList<Enemy>();
    enemies1.add(new EnemyBird(new PVector(3400, 300), new PVector(0, 0), false));
    enemies1.add(new EnemyBird(new PVector(6075, 370), new PVector(0, 0), false));
    doorButton10 = new DoorButton(7900, 90, 8000, 320);

    //level2
    enemies2 = new ArrayList<Enemy>();
    enemies2.add(new EnemyBird(new PVector(4850, 400), new PVector(0, 0), false));
    bridgeButton20 = new BridgeButton(3550, 340, 3750, 350);
    doorButton20 = new DoorButton(7990, 64, 8068, 384);

    //level3
    enemies3 = new ArrayList<Enemy>();
    enemies3.add(new EnemyBird(new PVector(4850, 400), new PVector(0, 0), false));
    enemies3.add(new EnemyBird(new PVector(6575, 330), new PVector(0, 0), false));
    enemies3.add(new EnemyBird(new PVector(7232, 150), new PVector(0, 0), false));
    doorButton30 = new DoorButton(5360, 400, 5440, 128);
    bridgeButton30 = new BridgeButton(8200, 120, 7700, 450);
    speedBoost30 = new SpeedBoost(1200, 480, false);
    speedBoost31 = new SpeedBoost(5400, 600, false);
  }

  public void run() {

    if (moomu == null) return;
    
    moomu.update();

    if (shoot) {     
      bullet.update();
      if (moomu.r.x - bullet.r.x > 700 || bullet.r.x - moomu.r.x > 700 || bullet.r.y < 0 || bullet.r.y > height || bullet.lifetime < 0) shoot = false;
    } else bullet = null; 

    camera.update();
    camera.apply();

    map.display();
    moomu.display();
    if (shoot) bullet.display();

    switch (level) {
      //level1
    case 1: 
      for (int i = enemies1.size()-1; i >= 0; i--) {
        Enemy enemie = enemies1.get(i);
        enemie.update();
        enemie.display();

        if (enemie.isDead) {
          enemies1.remove(i);
          shoot = false;
        }
      }
      doorButton10.update();
      doorButton10.display();      
      break;

      //level2
    case 2:
      for (int i = enemies2.size()-1; i >= 0; i--) {
        Enemy enemie = enemies2.get(i);
        enemie.update();
        enemie.display();

        if (enemie.isDead) {
          enemies2.remove(i);
          shoot = false;
        }
      }

      bridgeButton20.update();
      doorButton20.update();

      bridgeButton20.display();
      doorButton20.display();
      break;

      //level3
    case 3:     
      for (int i = enemies3.size()-1; i >= 0; i--) {
        Enemy enemie = enemies3.get(i);
        enemie.update();
        enemie.display();

        if (enemie.isDead) {
          enemies3.remove(i);
          shoot = false;
        }
      }

      if (!speedBoost30.isUsed) {
        speedBoost30.update();
        speedBoost30.display();
      }

      if (!speedBoost31.isUsed) {
        speedBoost31.update();
        speedBoost31.display();
      }     

      doorButton30.update();
      bridgeButton30.update();

      doorButton30.display();
      bridgeButton30.display();

      break;
    }

    //
    if (moomu.r.x > 8300) gamestate = GameState.WIN;
    if (moomu == null) menu.endMenu();
    if (moomu.isDead) moomu = null;
    //if (moomu == null) gamestate = GameState.LOST;
  }
}
public class Platform extends GameObject {
  float x, y, width, height;

  Platform(PVector r, PVector s) {
    super(r, s);
    r = new PVector(x, y);
    s = new PVector(width, height);
  }

  public void display() {
    fill(0, 255, 0);
    rectMode(CENTER);
    rect(r.x, r.y, s.x, s.y);
  }
}
class SpeedBoost {
  PVector r;
  float boostDistance;
  boolean isUsed;

  SpeedBoost(float x, float y, boolean isUsed) {
    r = new PVector(x, y);
    this.isUsed = isUsed;
  }

  public void update() {
    if (moomu != null) boostDistance = dist(moomu.r.x, moomu.r.y, r.x,r.y);
    
    if (boostDistance < 35) {
      moomu.speeding = true;
      isUsed = true;
    }
  }

  public void display() {
    pushMatrix();
    stroke(0);
    fill(0, 90, 255);
    rect(r.x, r.y, 30, 30);
    popMatrix();
  }
}
class Spike extends GameObject {
  float x, y, width, height;

  Spike(PVector r, PVector s) {
    super(r, s);
    r = new PVector(x, y);
    s = new PVector(width, height);
  }
  
  public void display() {
    fill(0, 255, 0);
    rectMode(CENTER);
    rect(r.x, r.y, s.x, s.y);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Moomuu" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

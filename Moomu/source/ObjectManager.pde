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
    moomu = new Moomu(new PVector(width/2, height/2), new PVector(0, 0), .45, false);  
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

  void run() {

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

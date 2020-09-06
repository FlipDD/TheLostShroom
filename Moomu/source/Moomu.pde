public class Moomu extends GameObject {
  private PVector v, a, mouse;
  private float scale, duration;
  boolean collision, isDead, left, right, stickTrigger, jump, jumping, speeding;

  Moomu(PVector r, PVector s, float scale, boolean isDead) {
    super(r, s);
    v = new PVector(5, 0.2);
    a = new PVector(0, 0.63);
    this.scale = scale;
    this.isDead = isDead;
    s.x = fmoomu.width * scale;
    s.y = fmoomu.height * scale;
    isDead = false;
    duration = 225;
    collision = false;
  }

  void update() {
    
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

  void jump() {
    v.y = -14;
  }

  void handlingCollisions() {
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

  void handlingSpikes() {
    for (Spike spike : map.spikes) 
      if (isCollidingWith(spike)) gamestate = GameState.LOST;
  }

  void speedBoost() {
    if (speeding) {
      if (moomu.v.x > 0) {
        moomu.v.x = 6.5;
      } else moomu.v.x = -6.5;
      duration--;
      if (duration < 0) speeding = false;
    } else {
      if (moomu.v.x >= 0) {
        moomu.v.x = 5;
      } else moomu.v.x = -5;
      duration = 225;
    }
  }

  void display() {
    pushMatrix();
    translate(r.x, r.y);
    strokeWeight(1.8);
    stroke(255, 255, 255, 40);
    line(0, 0, mouse.x, mouse.y);
    scale(scale);
    imageMode(CENTER);
    if (right) image(fmoomu, 0, 0);
    if (left) image(fmoomu1, 0, 0);
    popMatrix();
  }
}

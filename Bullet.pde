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

  void update() {
    v.add(a);    
    r.add(v);
    handlingCollisions();
    lifetime -= 1;
  }

  void handlingCollisions() {
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

  void display() {
    pushMatrix();
    stroke(0);
    translate(r.x, r.y);
    fill(255, 0, 0);
    ellipse(0, 0, s.x, s.y);
    popMatrix();
  }
}
class EnemyBird extends Enemy {
  private PVector v2;

  EnemyBird(PVector r, PVector s, boolean isDead) {
    super(r, s, isDead);
    v = new PVector(-2.5, 5);
    v2 = new PVector(-0.5, 0);
    s.x = birdUp.width * scale;
    s.y = birdUp.height * scale;
  }

  void update() {
    super.update();
    
    handlingCollisions();

    moveDistance = dist(r.x, r.y, moomu.r.x, moomu.r.y);
    if (r.x - moomu.r.x < 700) r.add(v2);
    if (moveDistance < 300) trigger = true;
    if (trigger) r.add(v);
    if (moveDistance < 75) moomu.isDead = true;
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
    imageMode(CENTER);
    image(birdUp, r.x, r.y);
    popMatrix();
  }
}
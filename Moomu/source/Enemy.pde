class Enemy extends GameObject {
  PVector v;
  boolean trigger, isDead;
  float moveDistance, scale;

  Enemy(PVector r, PVector s, boolean isDead) {
    super(r, s);
    this.isDead = isDead;
    trigger = false;
  }

  void update() {
    if (shoot) {
      if (dist(bullet.r.x, bullet.r.y, r.x, r.y) <= 35) isDead = true;
    }
  }
  
    void display() {
    
  }
}

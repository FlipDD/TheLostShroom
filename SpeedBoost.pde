class SpeedBoost {
  PVector r;
  float boostDistance;
  boolean isUsed;

  SpeedBoost(float x, float y, boolean isUsed) {
    r = new PVector(x, y);
    this.isUsed = isUsed;
  }

  void update() {
    if (moomu != null) boostDistance = dist(moomu.r.x, moomu.r.y, r.x,r.y);
    
    if (boostDistance < 35) {
      moomu.speeding = true;
      isUsed = true;
    }
  }

  void display() {
    pushMatrix();
    stroke(0);
    fill(0, 90, 255);
    rect(r.x, r.y, 30, 30);
    popMatrix();
  }
}
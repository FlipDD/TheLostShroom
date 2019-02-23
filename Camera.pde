class Camera {
  PVector r;

  Camera(PVector p) {
    this.r = p.copy();
  }

  void update() {
    if (r.x > moomu.r.x + 50)
      r.x = moomu.r.x + 50;
    else if (r.x < moomu.r.x - 50)
      r.x = moomu.r.x - 50;
  }

  PVector worldFromDisplay(PVector pd) {
    return PVector.add(pd, new PVector(r.x - width / 2, r.y - height / 2));
  }

  void apply() {
    translate(width/2 - r.x, height/2 - r.y);
  }
}
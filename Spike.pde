class Spike extends GameObject {
  float x, y, width, height;

  Spike(PVector r, PVector s) {
    super(r, s);
    r = new PVector(x, y);
    s = new PVector(width, height);
  }
  
  void display() {
    fill(0, 255, 0);
    rectMode(CENTER);
    rect(r.x, r.y, s.x, s.y);
  }
}
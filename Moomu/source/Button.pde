class Button {
  float x, y, buttonRadius;
  boolean buttonPressed;
  
  Button(float x, float y) {
    this.x = x;
    this.y = y;
    buttonRadius = 60;
  }
  
  void openDoor() {
    if (shoot) {
      if (dist(x, y, bullet.r.x, bullet.r.y) < 45) buttonPressed = true;
    }
  }
  
  void display() {
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

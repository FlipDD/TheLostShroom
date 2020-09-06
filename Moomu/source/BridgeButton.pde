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

  void update() {
    handlingCollisions(); 
  }
  
  void handlingCollisions() {
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

  void display() {
    super.display();
    if (buttonPressed) {
      fill(90, 90, 90);
      rect(currentX, currentY, currentWidth, currentHeight);
    }
  }
}

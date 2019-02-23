class DoorButton extends Button {
  float currentWidth, currentHeight, currentX, currentY;

  DoorButton(float x, float y, float currentX, float currentY) {
    super(x, y);
    this.currentX = currentX;
    this.currentY = currentY;
    currentWidth = 64;
    currentHeight = 128;
    buttonPressed = false;
  }

  void update() {
    handlingCollisions();
  }
  
  void handlingCollisions() {
    if (!buttonPressed) {
      //handlingCollisions moomu/door
      if (moomu.r.y + moomu.s.y / 2 >= currentY 
       && moomu.r.x + moomu.s.x / 2 >= currentX 
       && moomu.r.y - moomu.s.y / 2 <= currentY + currentHeight 
       && moomu.r.x - moomu.s.x / 2 <= currentX + currentWidth) {
        
        moomu.v.x = -(moomu.v.x);
      }
      //handling collisions bullet/door
      if (shoot) {  
        if (bullet.r.y + bullet.bulletHeight / 2 >= currentY 
         && bullet.r.x + bullet.bulletWidth / 2 >= currentX 
         && bullet.r.y - bullet.bulletHeight / 2 <= currentY + currentHeight 
         && bullet.r.x - bullet.bulletWidth / 2 <= currentX + currentWidth) bullet.v.x = -(bullet.v.x);
      }
    }
    openDoor();
  }

  void display() {
    super.display();
    if (!buttonPressed) {
      fill(0);
      rect(currentX, currentY, currentWidth, currentHeight);
    }
  }
}
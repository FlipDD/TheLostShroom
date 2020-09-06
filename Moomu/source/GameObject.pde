public class GameObject {
  PVector r;
  PVector s;

  GameObject(PVector r, PVector s) {
    this.r = r;
    this.s = s;
  }

  boolean isCollidingWith(GameObject other) {
    return (r.x - s.x / 1.99 <= other.r.x + other.s.x / 2
         && r.x + s.x / 2 >= other.r.x - other.s.x / 1.98
         && r.y - s.y / 1.99 <= other.r.y + other.s.y / 2
         && r.y + s.y / 2 >= other.r.y - other.s.y / 1.98);
  }

  CollisionDirection directionOfCollisionWith(GameObject other) {
    float nx = max(other.r.x - other.s.x / 1.99, r.x - s.x / 1.99);
    float ny = max(other.r.y - other.s.y / 2, r.y - s.y / 2);
    float nWidth = min(r.x + s.x / 1.99, other.r.x + other.s.x / 1.99) - nx;
    float nHeight = min(r.y + s.y / 2, other.r.y + other.s.y / 2) - ny;

    //check collision side
    if (nWidth < nHeight) {

      if (r.x >= other.r.x) {
        return CollisionDirection.LEFT;
      } else {
        return CollisionDirection.RIGHT ;
      }
    } else if (r.y <= other.r.y) {
      return CollisionDirection.BELOW;
    } else {
      return CollisionDirection.ABOVE;
    }
  }
}

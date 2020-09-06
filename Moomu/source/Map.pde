class Map {
  Ptmx map;
  ArrayList<Platform> platforms;
  ArrayList<Spike> spikes;

  Map(PApplet papplet, int level) {
    //depending on the current number of the level, get the right map and platforms / spikes position and size
    map = new Ptmx(papplet, "Level" + level + "l.tmx");
    map.setDrawMode(CENTER);
    map.setPositionMode("CANVAS");
    
    //platforms from the current map
    platforms = new ArrayList<Platform>();
    StringDict[] platformObjects = map.getObjects(1);
    for (StringDict platformObject : platformObjects) {
      float x = parseFloat(platformObject.get("x"));
      float y = parseFloat(platformObject.get("y"));
      float width = parseFloat(platformObject.get("width"));
      float height = parseFloat(platformObject.get("height"));
      platforms.add(new Platform(new PVector(x + width / 2, y + height / 2), new PVector(width, height)));
    }
    
    //spikes from the current map
    spikes = new ArrayList<Spike>();
    StringDict[] spikeObjects = map.getObjects(0);
    for (StringDict spikeObject : spikeObjects) {
      float x = parseFloat(spikeObject.get("x"));
      float y = parseFloat(spikeObject.get("y"));
      float width = parseFloat(spikeObject.get("width"));
      float height = parseFloat(spikeObject.get("height"));
      spikes.add(new Spike(new PVector(x + width / 2, y + height / 2), new PVector(width, height)));
    }
  }

  void display() {
    map.draw(camera.r.x, camera.r.y);
    
    //display framerate (top left)
    fill(255);
    textSize(15);
    text(frameRate, camera.r.x - width/2, 15);
  }
}

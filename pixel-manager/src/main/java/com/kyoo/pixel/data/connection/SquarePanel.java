package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedHashMap;
import java.util.Optional;
import lombok.Data;

@Data
public final class SquarePanel implements ConnectionComponent {

  private Point startPosition;
  private Optional<Point> endPosition;
  private LinkedHashMap<Point, Led> leds;
  private int idx;

  public SquarePanel(int idx, Point point) {
    this.idx = idx;
    this.startPosition = point;
    this.leds = new LinkedHashMap<>();
    this.endPosition = Optional.empty();
  }

  @Override
  public boolean intersects(int x, int y) {
    if(endPosition.isPresent()){
      if(startPosition.x<=x && x<=endPosition.get().x){
        if(startPosition.y<=y && y<=endPosition.get().y){
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean internalSelect(int x, int y) {
    return leds.containsKey(new Point(x,y));
  }

  @Override
  public ComponentType connectionType() {
    return ComponentType.SQUARE_PANEL;
  }

  @Override
  public Optional<ConnectionComponent> internalIntersects(Point position) {
    return Optional.empty();
  }

  @Override
  public CreationType creationType() {
    return CreationType.TWO_POINTS;
  }

  public void endComponent(Point endPoint) {
    this.endPosition = Optional.of(endPoint);
    for (int i = startPosition.y; i <= endPoint.y; i++) {
      for (int j = startPosition.x; j <= endPoint.x; j++) {
        Point ledPoint = new Point(j, i);
        this.leds.put(ledPoint, new Led(ledPoint, this));
      }
    }
  }

  public Point getStartPosition(){
    return startPosition;
  }

  public Point getEndPosition(){
    return endPosition.get();
  }
}

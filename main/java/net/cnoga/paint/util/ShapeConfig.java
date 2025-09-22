package net.cnoga.paint.util;


public record ShapeConfig(ShapeType type, int sides, boolean isRightTriangle) {

  // Default configs for common shapes
  public static ShapeConfig defaultFor(ShapeType type) {
    return switch (type) {
      case TRIANGLE -> new ShapeConfig(type, 3, false);
      case N_GON -> new ShapeConfig(type, 5, false);
      default -> new ShapeConfig(type, 0, false);
    };
  }
}
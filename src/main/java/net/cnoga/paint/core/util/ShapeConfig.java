package net.cnoga.paint.core.util;

/**
 * Immutable record defining basic shape drawing configuration.
 *
 * <p>Includes fill, stroke, color, and geometric parameters for
 * consistent states across classes.</p>
 *
 * @param type the type of shape (e.g., TRIANGLE, N_GON, STAR)
 * @param sides the number of sides for polygonal shapes (ignored for non-polygons)
 * @param isRightTriangle whether the shape is a right triangle (only relevant if {@code type} is TRIANGLE)
 */
public record ShapeConfig(ShapeType type, int sides, boolean isRightTriangle) {

  // Default configs for common shapes
  public static ShapeConfig defaultFor(ShapeType type) {
    return switch (type) {
      case TRIANGLE -> new ShapeConfig(type, 3, false);
      case N_GON, STAR -> new ShapeConfig(type, 5, false);
      default -> new ShapeConfig(type, 0, false);
    };
  }
}
package net.cnoga.paint.util;

/**
 * Enum of anchor positions used for positioning subwindows
 * relative to the main application window.
 *
 * <p>Anchors include top, middle, and bottom positions, each aligned
 * left, center, or right.</p>
 *
 * <p>Used by {@link PaintUtil#setSubwindowSpawnPoint} to determine
 * where subwindows should be placed when shown.</p>
 */
public enum AnchorTypes {
  TOP_LEFT, TOP_CENTER, TOP_RIGHT,
  MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT,
  BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
}

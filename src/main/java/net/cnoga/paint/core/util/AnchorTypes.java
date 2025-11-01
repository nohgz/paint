package net.cnoga.paint.core.util;

import net.cnoga.paint.core.brews.SubWindowBrew;

/**
 * Enum of anchor positions used for positioning subwindows relative to the main application
 * window.
 *
 * <p>Used by {@link SubWindowBrew} to determine
 * where subwindows should be placed when shown.</p>
 */
public enum AnchorTypes {
  TOP_LEFT, TOP_CENTER, TOP_RIGHT,
  MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT,
  BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
}

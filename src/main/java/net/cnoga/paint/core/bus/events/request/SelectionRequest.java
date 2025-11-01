package net.cnoga.paint.core.bus.events.request;


import javafx.geometry.Rectangle2D;

/**
 * Fired when a new selection is made.
 * @param bounds the bounds of the selection
 */
public record SelectionRequest(Rectangle2D bounds) {

}

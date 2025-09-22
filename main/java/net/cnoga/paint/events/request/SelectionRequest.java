package net.cnoga.paint.events.request;


import javafx.geometry.Rectangle2D;

/**
 * Fired when a new selection is made.
 */
public record SelectionRequest(Rectangle2D bounds) {

}

package net.cnoga.paint.events.request;


import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;

/**
 * Fired when a new selection is made.
 *
 * Carries the selected image snapshot and its original bounds.
 */
public record SelectionRequest(Rectangle2D bounds, WritableImage selection) {

}

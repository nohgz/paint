package net.cnoga.paint.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

/**
 * Represents a single paint workspace.
 * <p>
 * A {@code Workspace} manages a scrollable view of layered canvases, including base, transparency,
 * effects, and user-added drawing layers. It also tracks file state and zoom controls.
 */
public class Workspace {

  private final ScrollPane scrollPane;
  private final StackPane stackPane;
  private final Group canvasGroup;
  private final List<Canvas> layers = new ArrayList<>();

  private final String name;
  private final ZoomCapability zoomCapability;

  private File currentFile;
  private boolean dirty;

  /**
   * Creates a new workspace with a given name and initial canvas size.
   *
   * @param name   the display name of this workspace
   * @param width  the width of the canvas
   * @param height the height of the canvas
   * @throws IllegalArgumentException if width or height are non-positive
   */
  public Workspace(String name, double width, double height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Canvas size must be positive");
    }

    this.stackPane = new StackPane();
    this.canvasGroup = new Group();
    this.scrollPane = new ScrollPane(stackPane);
    this.zoomCapability = new ZoomCapability(scrollPane, canvasGroup);
    this.name = name;

    stackPane.getChildren().add(canvasGroup);
    setupDefaultLayers(width, height);
  }

  /**
   * Initializes the default non-removable layers:
   * <ul>
   *   <li>Transparency (bottom, mouse-transparent)</li>
   *   <li>Base (middle, main drawing surface)</li>
   *   <li>Effects (top, mouse-transparent)</li>
   * </ul>
   *
   * @param width  the canvas width
   * @param height the canvas height
   */
  private void setupDefaultLayers(double width, double height) {
    layers.clear();
    canvasGroup.getChildren().clear();

    Canvas transparency = createLayer("Transparency Layer", width, height);
    transparency.setMouseTransparent(true);

    Canvas base = createLayer("Base Layer", width, height);

    Canvas effects = createLayer("Effects Layer", width, height);
    effects.setMouseTransparent(true);

    // Order matters: bottom → top
    layers.add(transparency);
    layers.add(base);
    layers.add(effects);

    canvasGroup.getChildren().addAll(transparency, base, effects);
  }

  /**
   * Creates a new canvas layer.
   *
   * @param debugName a string identifier (used for debugging/UI inspection)
   * @param width     width of the layer
   * @param height    height of the layer
   * @return the new {@link Canvas}
   */
  private Canvas createLayer(String debugName, double width, double height) {
    Canvas canvas = new Canvas(width, height);
    canvas.setId(debugName);
    return canvas;
  }

  /**
   * Adds a new drawing layer above the base but below effects.
   *
   * @return the new drawing layer
   */
  public Canvas addDrawingLayer() {
    int insertIndex = layers.size() - 1; // before effects
    Canvas base = getBaseLayer();
    Canvas layer = createLayer("Drawing Layer", base.getWidth(), base.getHeight());

    layers.add(insertIndex, layer);
    canvasGroup.getChildren().add(insertIndex, layer);
    return layer;
  }

  /**
   * Removes a drawing layer if it exists and is not a reserved layer.
   *
   * @param layer the layer to remove
   * @throws IllegalArgumentException if attempting to remove reserved layers
   */
  public void removeLayer(Canvas layer) {
    if (layer == null || !layers.contains(layer)) {
      return;
    }
    if (isReservedLayer(layer)) {
      throw new IllegalArgumentException("Cannot remove transparency, base, or effects layers.");
    }

    layers.remove(layer);
    canvasGroup.getChildren().remove(layer);
  }

  /**
   * Determines if a given layer is one of the reserved layers.
   *
   * @param layer the layer to check
   * @return true if reserved, false otherwise
   */
  private boolean isReservedLayer(Canvas layer) {
    return layer == getTransparencyLayer()
      || layer == getBaseLayer()
      || layer == getEffectsLayer();
  }


  /**
   * @return true if workspace has unsaved changes
   */
  public boolean isDirty() {
    return dirty;
  }

  /**
   * @param state whether this workspace has unsaved changes
   */
  public void setDirty(boolean state) {
    this.dirty = state;
  }

  /**
   * @return the file currently associated with this workspace, or null
   */
  public File getFile() {
    return currentFile;
  }

  /**
   * @param file set the current associated file
   */
  public void setFile(File file) {
    this.currentFile = file;
  }

  /**
   * @return the scroll pane container for this workspace
   */
  public ScrollPane getScrollPane() {
    return scrollPane;
  }

  /**
   * @return an unmodifiable view of all layers
   */
  public List<Canvas> getLayers() {
    return Collections.unmodifiableList(layers);
  }

  /**
   * @return only user-added drawing layers
   */
  public List<Canvas> getUserLayers() {
    return layers.subList(1, layers.size() - 1);
  }

  /**
   * @return the base layer
   */
  public Canvas getBaseLayer() {
    return layers.get(1);
  }

  /**
   * @return the effects layer
   */
  public Canvas getEffectsLayer() {
    return layers.get(layers.size() - 1);
  }

  /**
   * @return the transparency/background layer
   */
  public Canvas getTransparencyLayer() {
    return layers.get(0);
  }

  /**
   * @return the display name of this workspace
   */
  public String getName() {
    return this.name;
  }
}

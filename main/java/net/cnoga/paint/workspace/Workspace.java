package net.cnoga.paint.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public class Workspace {
  private File currentFile;
  private Boolean dirtyFlag;
  private final ScrollPane scrollPane;
  private final StackPane stackPane;
  private final Group canvasGroup;
  private final List<Canvas> layers = new ArrayList<>();
  private final String name;
  private final ZoomManager zoomManager;

  public Workspace(String name, double width, double height) {
    this.stackPane = new StackPane();
    this.canvasGroup = new Group();
    this.scrollPane = new ScrollPane(stackPane);
    this.zoomManager = new ZoomManager(scrollPane, canvasGroup);
    this.name = name;
    this.dirtyFlag = false;

    stackPane.getChildren().add(canvasGroup);
    setupDefaultLayers(width, height);
  }

  private void setupDefaultLayers(double width, double height) {
    layers.clear();
    canvasGroup.getChildren().clear();

    Canvas transparency = createLayer("Transparency Layer", width, height);
    Canvas base = createLayer("Base Layer", width, height);
    Canvas effects = createLayer("Effects Layer", width, height);

    effects.setMouseTransparent(true);
    transparency.setMouseTransparent(true);

    layers.add(effects); // top
    layers.add(base);
    layers.add(transparency); // bottom

    canvasGroup.getChildren().addAll(base, effects);
  }

  private Canvas createLayer(String debugName, double width, double height) {
    if (width <= 0 || height <= 0) throw new IllegalArgumentException("Canvas size must be positive");
    Canvas canvas = new Canvas(width, height);
    canvas.setId(debugName);
    return canvas;
  }

  public Canvas addDrawingLayer() {
    Canvas base = getBaseLayer();
    Canvas layer = createLayer("Drawing Layer", base.getWidth(), base.getHeight());
    layers.add(layers.size() - 1, layer);
    canvasGroup.getChildren().add(canvasGroup.getChildren().size() - 1, layer);
    return layer;
  }

  public void removeLayer(Canvas layer) {
    if (layer == null || !layers.contains(layer)) return;
    if (layer == layers.get(0) || layer == layers.get(layers.size() - 1)) {
      throw new IllegalStateException("Cannot remove effects or base layer.");
    }
    layers.remove(layer);
    canvasGroup.getChildren().remove(layer);
  }



  // Getters
  public Boolean getDirtyFlag() { return dirtyFlag; }
  public File getFile() { return currentFile; }
  public ScrollPane getScrollPane() { return scrollPane; }
  public List<Canvas> getLayers() { return layers; }
  public List<Canvas> getUserLayers() { return layers.subList(1, layers.size() - 2); }
  public Canvas getBaseLayer() { return layers.get(layers.size() - 2); }
  public Canvas getEffectsLayer() { return layers.get(0); }
  public String getName() { return this.name; }

  // Setters
  public void setDirtyFlag(Boolean state) { this.dirtyFlag = state; }
  public void setFile(File file) { this.currentFile = file; }
}
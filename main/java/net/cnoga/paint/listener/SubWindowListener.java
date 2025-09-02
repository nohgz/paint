package net.cnoga.paint.listener;

import static net.cnoga.paint.util.PaintUtil.createToggledSubwindow;

import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.ToggleColorPickerEvent;
import net.cnoga.paint.events.ToggleHistoryEvent;
import net.cnoga.paint.events.ToggleLayersEvent;
import net.cnoga.paint.events.ToggleToolsEvent;

@EventBusSubscriber
public class SubWindowListener {

  private Stage colorPickerStage;
  private Stage historyStage;
  private Stage layersStage;
  private Stage toolsStage;
  private Stage mainStage;

  private ToggleButton historyButton;
  private ToggleButton toolsButton;
  private ToggleButton layersButton;
  private ToggleButton colorPickerButton;

  private Boolean historyOpen = false;
  private Boolean toolsOpen = false;
  private Boolean layersOpen = false;
  private Boolean colorPickerOpen = false;

  public void initSubwindowListener(Stage stage) {
    this.mainStage = stage;
  }

  public SubWindowListener(ToggleButton historyButton, ToggleButton toolsButton, ToggleButton layersButton, ToggleButton colorPickerButton) {
    this.historyButton = historyButton;
    this.toolsButton = toolsButton;
    this.layersButton = layersButton;
    this.colorPickerButton = colorPickerButton;
  }

  @SubscribeEvent
  public void onToggleColorPicker(ToggleColorPickerEvent event) {
    if (colorPickerStage == null) {
      colorPickerStage = createToggledSubwindow(
        "Color Picker",
        "/net/cnoga/paint/fxml/color_picker.fxml",
        mainStage,
        0.0,
        0.0,
        colorPickerButton);
    }

    if (colorPickerOpen) {
      colorPickerStage.hide();
    } else {
      colorPickerStage.show();
      colorPickerStage.toFront();
    }

    colorPickerOpen = !colorPickerOpen;
  }

  @SubscribeEvent
  public void showHistory(ToggleHistoryEvent event) {
    if (historyStage == null) {
      historyStage = createToggledSubwindow(
        "History",
        "/net/cnoga/paint/fxml/history.fxml",
        mainStage,
        0.0,
        0.0,
        historyButton);
    }
    if (historyOpen) {
      historyStage.hide();
    } else {
      historyStage.show();
      historyStage.toFront();
    }
    historyOpen = !historyOpen;
  }

  // might change this to onlayerstoggleevent or smthn like that
  @SubscribeEvent
  public void showLayers(ToggleLayersEvent event) {
    if (layersStage == null) {
      layersStage = createToggledSubwindow(
        "Layers",
        "/net/cnoga/paint/fxml/layers.fxml",
        mainStage,
        500.0,
        0.0,
        layersButton);

    }

    if (layersOpen) {
      layersStage.hide();
    } else {
      layersStage.show();
      layersStage.toFront();
    }

    layersOpen = !layersOpen;
  }

  @SubscribeEvent
  public void showTools(ToggleToolsEvent event) {
    if (toolsStage == null) {
      toolsStage = createToggledSubwindow(
        "Tools",
        "/net/cnoga/paint/fxml/tools.fxml",
        mainStage,
        700.0,
        400.0,
        toolsButton);
    }
    if (toolsOpen) {
      toolsStage.hide();
    } else {
      toolsStage.show();
      toolsStage.toFront();
    }

    toolsOpen = !toolsOpen;
  }
}

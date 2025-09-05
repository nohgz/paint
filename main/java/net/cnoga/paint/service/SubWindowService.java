package net.cnoga.paint.service;

import static net.cnoga.paint.util.PaintUtil.createToggledSubwindow;
import static net.cnoga.paint.util.PaintUtil.setSubwindowSpawnPoint;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.InitSubWindowServiceRequest;
import net.cnoga.paint.events.request.OpenSettingsRequest;
import net.cnoga.paint.events.request.OpenColorPickerRequest;
import net.cnoga.paint.events.request.OpenHistoryRequest;
import net.cnoga.paint.events.request.OpenLayersRequest;
import net.cnoga.paint.events.request.OpenToolsRequest;
import net.cnoga.paint.util.AnchorTypes;

@EventBusSubscriber
public class SubWindowService extends EventBusPublisher {

  private Stage colorPickerStage;
  private Stage historyStage;
  private Stage layersStage;
  private Stage toolsStage;
  private Stage settingsStage;
  private final Stage mainStage;

  private ToggleButton historyButton;
  private ToggleButton toolsButton;
  private ToggleButton layersButton;
  private ToggleButton colorPickerButton;
  private ToggleButton settingsButton;

  private Boolean historyOpen = false;
  private Boolean toolsOpen = false;
  private Boolean layersOpen = false;
  private Boolean colorPickerOpen = false;
  private Boolean settingsOpen = false;

  public SubWindowService(Stage stage) {
    this.mainStage = stage;
    bus.register(this);
  }

  @SubscribeEvent
  public void initSubWindowService(InitSubWindowServiceRequest req) {
    this.historyButton = req.historyButton();
    this.toolsButton = req.toolsButton();
    this.layersButton = req.layersButton();
    this.colorPickerButton = req.colorPickerButton();
    this.settingsButton = req.settingsButton();
  }

  @SubscribeEvent
  public void onClickColorPicker(OpenColorPickerRequest event) {
    if (colorPickerStage == null) {
      colorPickerStage = createToggledSubwindow(
        "Color Picker",
        "/net/cnoga/paint/fxml/color_picker.fxml",
        mainStage,
        false,
        0.0,
        0.0,
        colorPickerButton);

      setSubwindowSpawnPoint(colorPickerStage, mainStage, AnchorTypes.BOTTOM_LEFT);
    }

    if (colorPickerOpen) {
      colorPickerStage.hide();
      colorPickerButton.setSelected(false);
    } else {
      colorPickerStage.show();
      colorPickerStage.toFront();
      colorPickerButton.setSelected(true);
    }

    colorPickerOpen = !colorPickerOpen;
  }

  @SubscribeEvent
  public void onClickHistory(OpenHistoryRequest event) {
    if (historyStage == null) {
      historyStage = createToggledSubwindow(
        "History",
        "/net/cnoga/paint/fxml/history.fxml",
        mainStage,
        true,
        0.0,
        0.0,
        historyButton);

      setSubwindowSpawnPoint(historyStage, mainStage, AnchorTypes.TOP_RIGHT);
    }

    if (historyOpen) {
      historyStage.hide();
      historyButton.setSelected(false);
    } else {
      historyStage.show();
      historyStage.toFront();
      historyButton.setSelected(true);
    }
    historyOpen = !historyOpen;
  }

  @SubscribeEvent
  public void onClickLayers(OpenLayersRequest event) {
    if (layersStage == null) {
      layersStage = createToggledSubwindow(
        "Layers",
        "/net/cnoga/paint/fxml/layers.fxml",
        mainStage,
        true,
        500.0,
        0.0,
        layersButton);

      setSubwindowSpawnPoint(layersStage, mainStage, AnchorTypes.BOTTOM_RIGHT);
    }

    if (layersOpen) {
      layersStage.hide();
      layersButton.setSelected(false);
    } else {
      layersStage.show();
      layersStage.toFront();
      layersButton.setSelected(true);
    }

    layersOpen = !layersOpen;
  }

  @SubscribeEvent
  public void onClickTools(OpenToolsRequest event) {
    if (toolsStage == null) {
      toolsStage = createToggledSubwindow(
        "Tools",
        "/net/cnoga/paint/fxml/tools.fxml",
        mainStage,
        false,
        mainStage.getWidth()-20,
        mainStage.getHeight()-20,
        toolsButton);

      setSubwindowSpawnPoint(toolsStage, mainStage, AnchorTypes.TOP_LEFT);
    }


    if (toolsOpen) {
      toolsStage.hide();
      toolsButton.setSelected(false);
    } else {
      toolsStage.show();
      toolsStage.toFront();
      toolsButton.setSelected(true);
    }
    toolsOpen = !toolsOpen;
  }

  @SubscribeEvent
  public void onClickSettings(OpenSettingsRequest event) {
    if (settingsStage == null) {
      settingsStage = createToggledSubwindow(
        "Settings",
        "/net/cnoga/paint/fxml/settings.fxml",
        mainStage,
        true,
        0.0,
        0.0,
        settingsButton);
      setSubwindowSpawnPoint(settingsStage, mainStage, AnchorTypes.MIDDLE_CENTER);
    }

    if (settingsOpen) {
      settingsStage.hide();
      settingsButton.setSelected(false);
    } else {
      settingsStage.show();
      settingsStage.toFront();
      settingsButton.setSelected(true);
    }
    settingsOpen = !settingsOpen;
  }
}

package net.cnoga.paint.listener;

import static net.cnoga.paint.util.PaintUtil.createSubwindow;

import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.ToggleColorPickerEvent;
import net.cnoga.paint.events.ToggleHistoryEvent;
import net.cnoga.paint.events.ToggleLayersEvent;
import net.cnoga.paint.events.ToggleToolsEvent;

@EventBusSubscriber
public class SubwindowListener {

  private Stage colorPickerStage;
  private Stage historyStage;
  private Stage layersStage;
  private Stage toolsStage;

  private Stage mainStage;

  public SubwindowListener(Stage stage) {
    this.mainStage = stage;
  }

  @SubscribeEvent
  public void onToggleColorPicker(ToggleColorPickerEvent event) {
    if (colorPickerStage == null) {
      colorPickerStage = createSubwindow("Color Picker", "/net/cnoga/paint/fxml/color_picker.fxml", mainStage);
    }

    if (event.state() == true) {
      colorPickerStage.show();
      colorPickerStage.toFront();
    } else {
      colorPickerStage.hide();
    }
  }

  @SubscribeEvent
  public void showHistory(ToggleHistoryEvent event) {
    if (historyStage == null) {
      historyStage = createSubwindow("History", "/net/cnoga/paint/fxml/history.fxml", mainStage);
    }
    if (event.state() == true) {
      historyStage.show();
      historyStage.toFront();
    } else {
      historyStage.hide();
    }
  }

  // might change this to onlayerstoggleevent or smthn like that
  @SubscribeEvent
  public void showLayers(ToggleLayersEvent event) {
    if (layersStage == null) {
      layersStage = createSubwindow("Layers", "/net/cnoga/paint/fxml/layers.fxml", mainStage);
    }

    if (event.state() == true) {
      layersStage.show();
      layersStage.toFront();
    } else {
      layersStage.hide();
    }
  }

  @SubscribeEvent
  public void showTools(ToggleToolsEvent event) {
    if (toolsStage == null) {
      System.out.println("trying to make subwindow!");
      toolsStage = createSubwindow("Tools", "/net/cnoga/paint/fxml/tools.fxml", mainStage);
    }
    if (event.state() == true) {
      toolsStage.show();
      toolsStage.toFront();
    } else {
      toolsStage.hide();
    }
  }
}

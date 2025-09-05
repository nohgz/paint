package net.cnoga.paint.service;

import static net.cnoga.paint.util.PaintUtil.createSubwindow;
import static net.cnoga.paint.util.PaintUtil.createToggledSubwindow;
import static net.cnoga.paint.util.PaintUtil.setSubwindowSpawnPoint;

import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.InitSubWindowServiceRequest;
import net.cnoga.paint.events.request.OpenAboutRequest;
import net.cnoga.paint.events.request.OpenChangelogRequest;
import net.cnoga.paint.events.request.OpenHelpRequest;
import net.cnoga.paint.events.request.OpenHistoryRequest;
import net.cnoga.paint.events.request.OpenLayersRequest;
import net.cnoga.paint.events.request.OpenSettingsRequest;
import net.cnoga.paint.events.request.OpenToolsRequest;
import net.cnoga.paint.util.AnchorTypes;

@EventBusSubscriber
public class SubWindowService extends EventBusPublisher {

  private Stage historyStage;
  private Stage layersStage;
  private Stage toolsStage;
  private Stage settingsStage;
  private Stage helpStage;
  private Stage changelogStage;
  private Stage aboutStage;
  private final Stage mainStage;

  private ToggleButton historyButton;
  private ToggleButton toolsButton;
  private ToggleButton layersButton;
  private ToggleButton settingsButton;

  private Boolean historyOpen = false;
  private Boolean toolsOpen = false;
  private Boolean layersOpen = false;
  private Boolean settingsOpen = false;
  private final Boolean aboutOpen = false;
  private final Boolean helpOpen = false;
  private final Boolean changelogOpen = false;

  public SubWindowService(Stage stage) {
    this.mainStage = stage;
    bus.register(this);
  }

  @SubscribeEvent
  private void initSubWindowService(InitSubWindowServiceRequest req) {
    this.historyButton = req.historyButton();
    this.toolsButton = req.toolsButton();
    this.layersButton = req.layersButton();
    this.settingsButton = req.settingsButton();
  }

  @SubscribeEvent
  private void onClickHistory(OpenHistoryRequest event) {
    if (historyStage == null) {
      historyStage = createToggledSubwindow(
        "History",
        "/net/cnoga/paint/fxml/subwindow/history.fxml",
        mainStage,
        true,
        200.0,
        0.0,
        historyButton);

      setSubwindowSpawnPoint(historyStage, mainStage, AnchorTypes.BOTTOM_LEFT);
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
  private void onClickLayers(OpenLayersRequest event) {
    if (layersStage == null) {
      layersStage = createToggledSubwindow(
        "Layers",
        "/net/cnoga/paint/fxml/subwindow/layers.fxml",
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
  private void onClickTools(OpenToolsRequest event) {
    if (toolsStage == null) {
      toolsStage = createToggledSubwindow(
        "Tools",
        "/net/cnoga/paint/fxml/subwindow/tools.fxml",
        mainStage,
        false,
        0.0d,
        0.0d,
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
  private void onClickSettings(OpenSettingsRequest event) {
    if (settingsStage == null) {
      settingsStage = createToggledSubwindow(
        "Settings",
        "/net/cnoga/paint/fxml/subwindow/settings.fxml",
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

  @SubscribeEvent
  private void onClickAbout(OpenAboutRequest event) {
    if (aboutStage == null) {
      aboutStage = createSubwindow(
        "About",
        "/net/cnoga/paint/fxml/subwindow/about.fxml",
        mainStage,
        true,
        0.0,
        0.0);
      setSubwindowSpawnPoint(aboutStage, mainStage, AnchorTypes.MIDDLE_CENTER);
    }

    aboutStage.show();
    aboutStage.toFront();
  }


  @SubscribeEvent
  private void onClickHelp(OpenHelpRequest event) {
    if (helpStage == null) {
      helpStage = createSubwindow(
        "Help",
        "/net/cnoga/paint/fxml/subwindow/help.fxml",
        mainStage,
        true,
        0.0,
        0.0);
      setSubwindowSpawnPoint(helpStage, mainStage, AnchorTypes.MIDDLE_CENTER);
    }

    helpStage.show();
    helpStage.toFront();
  }

  @SubscribeEvent
  private void onClickChangelog(OpenChangelogRequest event) {
    if (changelogStage == null) {
      changelogStage = createSubwindow(
        "Changelog",
        "/net/cnoga/paint/fxml/subwindow/changelog.fxml",
        mainStage,
        true,
        0.0,
        0.0);
      setSubwindowSpawnPoint(changelogStage, mainStage, AnchorTypes.MIDDLE_CENTER);
    }

    changelogStage.show();
    changelogStage.toFront();
  }
}

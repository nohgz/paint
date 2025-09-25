package net.cnoga.paint.core.brews;

import static net.cnoga.paint.core.util.SubwindowUtil.createSubwindow;
import static net.cnoga.paint.core.util.SubwindowUtil.createToggledSubwindow;
import static net.cnoga.paint.core.util.SubwindowUtil.setSubwindowSpawnPoint;

import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.init.InitSubWindowServiceRequest;
import net.cnoga.paint.core.bus.events.request.OpenAboutRequest;
import net.cnoga.paint.core.bus.events.request.OpenChangelogRequest;
import net.cnoga.paint.core.bus.events.request.OpenHelpRequest;
import net.cnoga.paint.core.bus.events.request.OpenHistoryRequest;
import net.cnoga.paint.core.bus.events.request.OpenLayersRequest;
import net.cnoga.paint.core.bus.events.request.OpenSettingsRequest;
import net.cnoga.paint.core.bus.events.request.OpenToolsRequest;
import net.cnoga.paint.core.util.AnchorTypes;

/**
 * Service responsible for creating and managing all auxiliary subwindows in the application (e.g.,
 * History, Layers, Tools, Settings, About, Help, Changelog).
 *
 * <p>This service subscribes to UI interaction events and toggles the corresponding
 * subwindows. Each subwindow is lazily created on first use, positioned relative to the main
 * application stage, and either shown, hidden, or brought to the front depending on the current
 * state.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Managing the lifecycle of subwindows (creation, showing, hiding).</li>
 *   <li>Synchronizing toggle button states with subwindow visibility.</li>
 *   <li>Ensuring subwindows are consistently spawned at predefined anchor points
 *       relative to the main stage.</li>
 *   <li>Handling modal and non-modal subwindows appropriately.</li>
 * </ul>
 */
@EventBusSubscriber
public class SubWindowBrew extends EventBusPublisher {

  private final Stage mainStage;
  private Stage historyStage;
  private Stage layersStage;
  private Stage toolsStage;
  private Stage settingsStage;
  private Stage helpStage;
  private Stage changelogStage;
  private Stage aboutStage;
  private ToggleButton historyButton;
  private ToggleButton toolsButton;
  private ToggleButton layersButton;
  private ToggleButton settingsButton;
  private Boolean historyOpen = false;
  private Boolean toolsOpen = false;
  private Boolean layersOpen = false;
  private Boolean settingsOpen = false;

  public SubWindowBrew(Stage stage) {
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
  private void onOpenHistory(OpenHistoryRequest req) {
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
  private void onOpenLayers(OpenLayersRequest req) {
    if (layersStage == null) {
      layersStage = createToggledSubwindow(
        "Layers",
        "/net/cnoga/paint/fxml/subwindow/layers.fxml",
        mainStage,
        true,
        500.0d,
        0.0d,
        layersButton);

      setSubwindowSpawnPoint(layersStage, mainStage, AnchorTypes.MIDDLE_RIGHT);
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
  private void onOpenTools(OpenToolsRequest req) {
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
  private void onOpenSettings(OpenSettingsRequest req) {
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
  private void onOpenAbout(OpenAboutRequest evt) {
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
  private void onOpenHelp(OpenHelpRequest evt) {
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
  private void onOpenChangelog(OpenChangelogRequest evt) {
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

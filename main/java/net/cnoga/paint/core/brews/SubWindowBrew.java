package net.cnoga.paint.core.brews;

import static net.cnoga.paint.core.util.SubwindowUtil.createSubwindow;
import static net.cnoga.paint.core.util.SubwindowUtil.createToggledSubwindow;
import static net.cnoga.paint.core.util.SubwindowUtil.setSubwindowSpawnPoint;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.init.InitSubWindowServiceRequest;
import net.cnoga.paint.core.bus.events.request.ChangeThemeRequest;
import net.cnoga.paint.core.bus.events.request.OpenAboutRequest;
import net.cnoga.paint.core.bus.events.request.OpenChangelogRequest;
import net.cnoga.paint.core.bus.events.request.OpenHelpRequest;
import net.cnoga.paint.core.bus.events.request.OpenHistoryRequest;
import net.cnoga.paint.core.bus.events.request.OpenLayersRequest;
import net.cnoga.paint.core.bus.events.request.OpenSettingsRequest;
import net.cnoga.paint.core.bus.events.request.OpenToolsRequest;
import net.cnoga.paint.core.util.AnchorTypes;

/**
 * Manages all auxiliary subwindows (History, Layers, Tools, Settings, About, Help, Changelog).
 * <p>
 * Listens for UI events and lazily creates, shows, hides, or toggles subwindows while syncing
 * toggle button states and positioning relative to the main stage.
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

  /**
   * Constructs the Brew bound to the main application stage.
   */
  public SubWindowBrew(Stage stage) {
    this.mainStage = stage;
    bus.register(this);
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onThemeChange(ChangeThemeRequest req) {
    String stylesheet = Objects.requireNonNull(
      getClass().getResource(req.theme())
    ).toExternalForm();

    List<Stage> subStages = Arrays.asList(
      historyStage,
      toolsStage,
      layersStage,
      settingsStage,
      helpStage,
      changelogStage,
      aboutStage
    );

    for (Stage stage : subStages) {
      if (stage != null && stage.getScene() != null) {
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(stylesheet);
      }
    }
  }

  /**
   * Initializes subwindow service with the provided toggle buttons.
   */
  @SubscribeEvent
  private void initSubWindowService(InitSubWindowServiceRequest req) {
    this.historyButton = req.historyButton();
    this.toolsButton = req.toolsButton();
    this.layersButton = req.layersButton();
    this.settingsButton = req.settingsButton();
  }

  /**
   * Shows the History subwindow on request.
   */
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

  /**
   * Shows the Layers subwindow on request.
   */
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

  /**
   * Shows the Tools subwindow on request.
   */
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


  /**
   * Shows the Settings subwindow on request.
   */
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

  /**
   * Shows the About subwindow on request.
   */
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

  /**
   * Shows the Help subwindow on request.
   */
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

  /**
   * Shows the Changelog subwindow on request.
   */
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

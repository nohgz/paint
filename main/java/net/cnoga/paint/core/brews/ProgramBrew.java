package net.cnoga.paint.core.brews;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.stage.Stage;
import net.cnoga.paint.client.popup.ProgramSaveWarningPopup;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.CloseProgramRequest;
import net.cnoga.paint.core.bus.events.request.ForceCloseProgramRequest;
import net.cnoga.paint.core.bus.events.request.GetDirtyWorkspacesRequest;
import net.cnoga.paint.core.bus.events.request.OpenGitHubRequest;
import net.cnoga.paint.core.bus.events.response.GotDirtyWorkspacesEvent;

/**
 * Manages the program lifecycle and handles application-level events.
 * <p>
 * Listens to close requests, ensures unsaved changes are addressed,
 * optionally shows a save-warning popup, and handles external link openings.
 */
@EventBusSubscriber
public class ProgramBrew extends EventBusPublisher {

  private final Stage primaryStage;
  private final Runnable closeProgram;
  private ProgramSaveWarningPopup programSaveWarningPopup;
  private Stage warningStage;

  /** Constructs a ProgramBrew bound to the given primary stage. */
  public ProgramBrew(Stage primaryStage) {
    this.primaryStage = primaryStage;
    bus.register(this);

    closeProgram = () -> bus.post(new ForceCloseProgramRequest());
  }

  /**
   * Opens a URL in the system browser asynchronously.
   *
   * @param url the web address to open
   */
  public static void openLink(String url) {
    if (Desktop.isDesktopSupported()) {
      new Thread(() -> {
        try { Desktop.getDesktop().browse(new URI(url)); }
        catch (IOException | URISyntaxException e) { e.printStackTrace(); }
      }).start();
    } else {
      System.err.println("Desktop browsing not supported.");
    }
  }

  /** Immediately closes the application. */
  @SubscribeEvent
  private void onForceClose(ForceCloseProgramRequest req) {
    primaryStage.close();
  }

  /** Initiates a safe shutdown by requesting dirty workspace states. */
  @SubscribeEvent
  private void onCloseProgram(CloseProgramRequest req) {
    bus.post(new GetDirtyWorkspacesRequest());
  }

  /** Handles dirty workspace check and shows save-warning popup if needed. */
  @SubscribeEvent
  private void onGetDirtyWorkspace(GotDirtyWorkspacesEvent evt) {
    if (evt.dirtyWorkspaces() != null && evt.dirtyWorkspaces().size() == 0) {
      bus.post(new ForceCloseProgramRequest());
      return;
    }

    if (programSaveWarningPopup == null) {
      this.programSaveWarningPopup = new ProgramSaveWarningPopup(evt.dirtyWorkspaces(), closeProgram);
    }
    programSaveWarningPopup.show();
  }

  /** Opens the project GitHub page in the default browser. */
  @SubscribeEvent
  private void onOpenGitHub(OpenGitHubRequest req) {
    openLink("https://github.com/nohgz/paint");
  }
}

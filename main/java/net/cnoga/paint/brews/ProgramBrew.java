package net.cnoga.paint.brews;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.stage.Stage;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.events.request.DirtyWorkspacesRequest;
import net.cnoga.paint.events.request.ForceCloseRequest;
import net.cnoga.paint.events.request.OpenGitHubRequest;
import net.cnoga.paint.events.response.GetDirtyWorkspaceEvent;

/**
 * Service responsible for managing the overall lifecycle of the program.
 *
 * <p>This service acts as an event bus subscriber that listens for application-level
 * events such as program close requests and GitHub link openings. It coordinates safe shutdown of
 * the application by requesting a save state before closing and, if necessary, displaying a warning
 * dialog when unsaved changes exist.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Handling program close requests and ensuring unsaved changes are addressed.</li>
 *   <li>Displaying a modal warning dialog when the user attempts to close with unsaved changes.</li>
 *   <li>Forcing application shutdown when explicitly requested.</li>
 *   <li>Opening external resources such as the GitHub project page.</li>
 * </ul>
 */
@EventBusSubscriber
public class ProgramBrew extends EventBusPublisher {

  private final Stage primaryStage;
  private final SaveWarningBrew saveWarningBrew;
  private Stage warningStage;

  public ProgramBrew(Stage primaryStage, SaveWarningBrew saveWarningBrew) {
    this.primaryStage = primaryStage;
    this.saveWarningBrew = saveWarningBrew;
    bus.register(this);
  }

  public static void openLink(String url) {
    if (Desktop.isDesktopSupported()) {
      new Thread(() -> {
        try {
          Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
          e.printStackTrace();
        }
      }).start();
    } else {
      System.err.println("Desktop browsing not supported.");
    }
  }

  @SubscribeEvent
  private void onForceCloseRequest(ForceCloseRequest req) {
    primaryStage.close();
  }

  @SubscribeEvent
  private void onCloseRequest(CloseProgramRequest req) {
    // ask for the states before closing
    bus.post(new DirtyWorkspacesRequest());
  }

  @SubscribeEvent
  private void onGetDirtyWorkspacesEvent(GetDirtyWorkspaceEvent evt) {
    saveWarningBrew.promptProgramClose(evt.dirtyWorkspaces(), primaryStage::close);
  }

  @SubscribeEvent
  private void onOpenGitHub(OpenGitHubRequest req) {
    openLink("https://github.com/nohgz/paint");
  }
}

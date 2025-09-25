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
  private final Runnable closeProgram;
  private ProgramSaveWarningPopup programSaveWarningPopup;
  private Stage warningStage;

  public ProgramBrew(Stage primaryStage) {
    this.primaryStage = primaryStage;
    bus.register(this);

    closeProgram = () -> {
      primaryStage.close();
    };

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
  private void onForceClose(ForceCloseProgramRequest req) {
    primaryStage.close();
  }

  @SubscribeEvent
  private void onCloseProgram(CloseProgramRequest req) {
    // ask for the states before closing
    bus.post(new GetDirtyWorkspacesRequest());
  }

  @SubscribeEvent
  private void onGetDirtyWorkspace(GotDirtyWorkspacesEvent evt) {
    if (evt.dirtyWorkspaces() != null && evt.dirtyWorkspaces().size() == 0) {
      bus.post(new ForceCloseProgramRequest());
      return;
    }

    if (programSaveWarningPopup == null) {
      this.programSaveWarningPopup = new ProgramSaveWarningPopup(evt.dirtyWorkspaces(),
        closeProgram);
    }
    programSaveWarningPopup.show();
  }

  @SubscribeEvent
  private void onOpenGitHub(OpenGitHubRequest req) {
    openLink("https://github.com/nohgz/paint");
  }
}

package net.cnoga.paint.core.brews;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.ClearWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.CloseCurrentWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.FocusWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.NewWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.PasteSelectionRequest;
import net.cnoga.paint.core.bus.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.core.bus.events.response.ColorChangedEvent;
import net.cnoga.paint.core.bus.events.response.FileOpenedEvent;
import net.cnoga.paint.core.bus.events.response.SelectionPastedEvent;
import net.cnoga.paint.core.bus.events.response.ToolChangedEvent;
import net.cnoga.paint.core.bus.events.response.WorkspaceSavedAsEvent;
import net.cnoga.paint.core.bus.events.response.WorkspaceSavedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Background file logger that asynchronously writes timestamped application events to a session log
 * file. Log entries are queued to avoid blocking UI or event threads.
 *
 * <p>Console output continues through Log4j; this class only handles session file output.</p>
 *
 * <p>Call {@link #shutdown()} during application exit to stop the logging thread.</p>
 */
@EventBusSubscriber
public class LoggerBrew extends EventBusPublisher implements Runnable {

  private static final Logger log = LogManager.getLogger(LoggerBrew.class);

  private static final DateTimeFormatter TS_FORMAT =
    DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS");

  private static final Path LOG_DIR = Paths.get("logs");

  private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
  private final File logFile;
  private volatile boolean running = true;

  /**
   * Creates a LoggerBrew instance and starts the background logging thread.
   */
  public LoggerBrew() {
    bus.register(this);

    // Ensure log directory exists
    try {
      Files.createDirectories(LOG_DIR);
    } catch (IOException e) {
      log.error("Failed to create log directory: {}", LOG_DIR, e);
    }

    // Create a session log file (e.g., logs/session_2025-10-06_1623.log)
    String filename = "session_" +
      LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd_HHmm")) + ".log";
    this.logFile = LOG_DIR.resolve(filename).toFile();

    Thread thread = new Thread(this, "LoggerBrew-Thread");
    thread.setDaemon(true);
    thread.start();

    log.info("LoggerBrew initialized. Logging to {}", logFile.getAbsolutePath());
  }

  /**
   * Main background loop that writes queued log messages to the session file.
   */
  @Override
  public void run() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
      while (running || !logQueue.isEmpty()) {
        String message = logQueue.poll(2, TimeUnit.SECONDS);
        if (message != null) {
          writer.write(message);
          writer.newLine();
          writer.flush();
        }
      }
    } catch (IOException | InterruptedException e) {
      log.error("LoggerBrew background thread error", e);
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Stops the logging thread gracefully.
   */
  public void shutdown() {
    running = false;
    log.info("LoggerBrew shutting down...");
  }

  /**
   * Adds a message to the background logging queue with a timestamp.
   *
   * @param message the message to log
   */
  private void enqueue(String message) {
    String timestamp = "[" + LocalDateTime.now().format(TS_FORMAT) + "] ";
    logQueue.offer(timestamp + message);
  }

  /**
   * Logs when a file is opened.
   *
   * @param evt the file opened event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onFileOpened(FileOpenedEvent evt) {
    String msg = "Opened file: " + evt.file().getAbsolutePath();
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when the active color changes.
   *
   * @param evt the color change event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onColorChanged(ColorChangedEvent evt) {
    String msg = "Color changed to " + evt.color();
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when a workspace is saved.
   *
   * @param evt the workspace saved event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onWorkspaceSaved(WorkspaceSavedEvent evt) {
    String msg = "Workspace '" + evt.workspace().getName() + "' saved successfully.";
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when a workspace is saved as a new file.
   *
   * @param evt the workspace saved-as event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onWorkspaceSavedAs(WorkspaceSavedAsEvent evt) {
    String msg = "Workspace '" + evt.workspace().getName() + "' saved as new file.";
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when the active tool changes.
   *
   * @param evt the tool change event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onToolChanged(ToolChangedEvent evt) {
    String msg = "Tool changed: " + evt.tool().getName();
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when a selection is pasted.
   *
   * @param evt the selection paste event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onSelectionPasted(SelectionPastedEvent evt) {
    String msg = String.format("Selection pasted at (%.2f, %.2f)", evt.x(), evt.y());
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when a new workspace is created.
   *
   * @param evt the new workspace request event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onNewWorkspace(NewWorkspaceRequest evt) {
    String msg = "New workspace created (" + evt.width() + "x" + evt.height() + ")";
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when a workspace is cleared.
   *
   * @param evt the clear workspace request event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onClearWorkspace(ClearWorkspaceRequest evt) {
    String msg = "Workspace cleared.";
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when the current workspace is closed.
   *
   * @param evt the close workspace request event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onCloseCurrentWorkspace(CloseCurrentWorkspaceRequest evt) {
    String msg = "Current workspace closed.";
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when a save request is issued for the active workspace.
   *
   * @param evt the workspace save request event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onWorkspaceSaveRequest(WorkspaceSaveRequest evt) {
    String msg = "Save request issued for active workspace.";
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when a paste request is issued.
   *
   * @param evt the paste selection request event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onPasteSelection(PasteSelectionRequest evt) {
    String msg = "Paste selection requested.";
    log.info(msg);
    enqueue(msg);
  }

  /**
   * Logs when workspace focus changes.
   *
   * @param evt the focus workspace request event
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onFocusWorkspace(FocusWorkspaceRequest evt) {
    String msg = "Focused workspace: " + evt.workspace().getName();
    log.info(msg);
    enqueue(msg);
  }
}

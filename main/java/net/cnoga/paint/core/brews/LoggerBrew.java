package net.cnoga.paint.core.brews;

import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.*;
import net.cnoga.paint.core.bus.events.response.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * LoggerBrew
 *
 * A background logger that listens for system events on the EventBus and asynchronously writes them
 * to a timestamped session log file. Console output is still handled by Log4j, but file persistence
 * happens on a separate thread to avoid blocking the JavaFX or event threads.
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

  public void shutdown() {
    running = false;
    log.info("LoggerBrew shutting down...");
  }

  /* =============================================================
   *                       EVENT LOGGING
   * ============================================================= */

  private void enqueue(String message) {
    String timestamp = "[" + LocalDateTime.now().format(TS_FORMAT) + "] ";
    logQueue.offer(timestamp + message);
  }

  @SubscribeEvent
  private void onFileOpened(FileOpenedEvent evt) {
    String msg = "Opened file: " + evt.file().getAbsolutePath();
    log.info(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onColorChanged(ColorChangedEvent evt) {
    String msg = "Color changed to " + evt.color();
    log.debug(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onWorkspaceSaved(WorkspaceSavedEvent evt) {
    String msg = "Workspace '" + evt.workspace().getName() + "' saved successfully.";
    log.info(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onWorkspaceSavedAs(WorkspaceSavedAsEvent evt) {
    String msg = "Workspace '" + evt.workspace().getName() + "' saved as new file.";
    log.info(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onToolChanged(ToolChangedEvent evt) {
    String msg = "Tool changed: " + evt.tool().getName();
    log.debug(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onSelectionPasted(SelectionPastedEvent evt) {
    String msg = String.format("Selection pasted at (%.2f, %.2f)", evt.x(), evt.y());
    log.debug(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onNewWorkspace(NewWorkspaceRequest evt) {
    String msg = "New workspace created (" + evt.width() + "x" + evt.height() + ")";
    log.info(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onClearWorkspace(ClearWorkspaceRequest evt) {
    String msg = "Workspace cleared.";
    log.info(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onCloseCurrentWorkspace(CloseCurrentWorkspaceRequest evt) {
    String msg = "Current workspace closed.";
    log.info(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onWorkspaceSaveRequest(WorkspaceSaveRequest evt) {
    String msg = "Save request issued for active workspace.";
    log.info(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onPasteSelection(PasteSelectionRequest evt) {
    String msg = "Paste selection requested.";
    log.debug(msg);
    enqueue(msg);
  }

  @SubscribeEvent
  private void onFocusWorkspace(FocusWorkspaceRequest evt) {
    String msg = "Focused workspace: " + evt.workspace().getName();
    log.info(msg);
    enqueue(msg);
  }
}

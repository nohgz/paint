package net.cnoga.paint.core.brews;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.CloseProgramRequest;
import net.cnoga.paint.core.bus.events.request.ForceCloseProgramRequest;
import net.cnoga.paint.core.bus.events.request.SetAutosaveIntervalRequest;
import net.cnoga.paint.core.bus.events.request.ToggleAutosaveRequest;
import net.cnoga.paint.core.bus.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.core.bus.events.response.AutosaveTimeChangedEvent;
import net.cnoga.paint.core.bus.events.response.WorkspaceSavedAsEvent;
import net.cnoga.paint.core.bus.events.response.WorkspaceSavedEvent;

/**
 * Manages automatic workspace saving at fixed intervals and posts events.
 * <p>
 * Supports start/stop, countdown notifications, and optional system tray alerts.
 */
@EventBusSubscriber
public class AutosaveBrew extends EventBusPublisher {

  private final ScheduledExecutorService scheduler;
  private boolean isEnabled;
  private int intervalSeconds;
  /**
   * Scheduled task handle for the autosave action.
   */
  private ScheduledFuture<?> autosaveHandle;

  /**
   * Scheduled task handle for the countdown timer.
   */
  private ScheduledFuture<?> countdownHandle;

  /**
   * Remaining time until the next autosave in seconds.
   */
  private volatile int timeLeft;

  /**
   * System tray icon used for notifications. May be null if tray unsupported.
   */
  private TrayIcon trayIcon;

  /**
   * Constructs an {@code AutosaveBrew} with default settings. Registers the instance to the event
   * bus and initializes the system tray icon.
   */
  public AutosaveBrew() {
    bus.register(this);
    this.isEnabled = false;
    this.intervalSeconds = 60;
    this.scheduler = Executors.newSingleThreadScheduledExecutor();
    setupSystemTray();
  }

  /**
   * Toggles autosave on or off in response to a {@link ToggleAutosaveRequest}.
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onToggleAutosave(ToggleAutosaveRequest req) {
    if (isEnabled) {
      stopAutosave();
    } else {
      startAutosave();
    }
  }

  /**
   * Sets the autosave interval from a {@link SetAutosaveIntervalRequest}.
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onSetInterval(SetAutosaveIntervalRequest req) {
    this.intervalSeconds = req.minutes() * 60;
    if (isEnabled) {
      restartAutosave();
    }
  }

  /**
   * Restarts autosave when the workspace is saved.
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onSave(WorkspaceSavedEvent evt) {
    restartAutosave();
  }


  /**
   * Restarts autosave when the workspace is saved as.
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onSavedAs(WorkspaceSavedAsEvent evt) {
    restartAutosave();
  }

  /**
   * Stops autosave when the program closes.
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onProgramClose(CloseProgramRequest req) {
    stopAutosave();
  }

  /**
   * Stops autosave when the program force closes.
   */
  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onProgramForceClose(ForceCloseProgramRequest req) {
    stopAutosave();
  }

  /**
   * Stops and immediately restarts the autosave process.
   */
  private void restartAutosave() {
    stopAutosave();
    startAutosave();
  }

  /**
   * Starts the autosave and countdown tasks.
   */
  private void startAutosave() {
    isEnabled = true;
    timeLeft = intervalSeconds;

    // schedule autosave task
    autosaveHandle = scheduler.scheduleAtFixedRate(() -> {
      Platform.runLater(() -> bus.post(new WorkspaceSaveRequest()));
      publishAutosaveNotification();
      timeLeft = intervalSeconds;
    }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);

    // schedule countdown
    countdownHandle = scheduler.scheduleAtFixedRate(() -> {
      timeLeft--;
      bus.post(new AutosaveTimeChangedEvent(timeLeft));
      if (timeLeft <= 0) {
        timeLeft = intervalSeconds;
      }
    }, 1, 1, TimeUnit.SECONDS);
  }

  /**
   * Stops the autosave and countdown tasks.
   */
  private void stopAutosave() {
    isEnabled = false;
    if (autosaveHandle != null) {
      autosaveHandle.cancel(false);
    }
    if (countdownHandle != null) {
      countdownHandle.cancel(false);
    }
  }

  /**
   * Initializes the system tray icon for autosave notifications.
   * <p>
   * If the system tray is not supported, notifications will be disabled.
   * </p>
   */
  private void setupSystemTray() {
    if (!SystemTray.isSupported()) {
      System.out.println("[AutosaveBrew] System tray not supported; notifications disabled.");
      return;
    }

    SystemTray tray = SystemTray.getSystemTray();
    try {
      // simple placeholder icon
      BufferedImage iconImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = iconImage.createGraphics();
      g.setColor(Color.GRAY);
      g.fillRect(0, 0, 16, 16);
      g.setColor(Color.WHITE);
      g.drawString("A", 4, 12);
      g.dispose();

      trayIcon = new TrayIcon(iconImage, "Autosave");
      trayIcon.setImageAutoSize(true);
      trayIcon.setToolTip("Autosave system active");
      tray.add(trayIcon);

    } catch (Exception e) {
      System.out.println("[AutosaveBrew] Could not initialize system tray: " + e.getMessage());
    }
  }

  /**
   * Displays a system notification via the tray icon.
   * <p>
   * Does nothing if the system tray is not available.
   * </p>
   */
  private void publishAutosaveNotification() {
    if (trayIcon == null) {
      return;
    }
    try {
      trayIcon.displayMessage("Pain(t)", "Your workspace has been saved.", TrayIcon.MessageType.INFO);
    } catch (Exception e) {
      System.out.println("[AutosaveBrew] Failed to display autosave notification: " + e.getMessage());
    }
  }
}

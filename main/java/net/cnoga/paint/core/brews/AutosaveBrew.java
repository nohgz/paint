package net.cnoga.paint.core.brews;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.*;
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

@EventBusSubscriber
public class AutosaveBrew extends EventBusPublisher {

  private boolean isOn;
  private int intervalSeconds;
  private ScheduledExecutorService scheduler;
  private ScheduledFuture<?> autosaveHandle;
  private ScheduledFuture<?> countdownHandle;
  private volatile int timeLeft;
  private TrayIcon trayIcon;

  public AutosaveBrew() {
    bus.register(this);
    this.isOn = false;
    this.intervalSeconds = 60;
    this.scheduler = Executors.newSingleThreadScheduledExecutor();
    setupSystemTray();
  }

  // --- Event handlers --------------------------------------------------------

  @SubscribeEvent
  private void onToggleAutosave(ToggleAutosaveRequest req) {
    if (isOn) {
      stopAutosave();
    } else {
      startAutosave();
    }
  }

  @SubscribeEvent
  private void onSetInterval(SetAutosaveIntervalRequest req) {
    this.intervalSeconds = req.minutes() * 10;
    if (isOn) restartAutosave();
  }

  @SubscribeEvent
  private void onSave(WorkspaceSavedEvent evt) {
    restartAutosave();
  }

  @SubscribeEvent
  private void onSavedAs(WorkspaceSavedAsEvent evt) {
    restartAutosave();
  }

  @SubscribeEvent
  private void onProgramClose(CloseProgramRequest req) {
    stopAutosave();
  }

  @SubscribeEvent
  private void onProgramForceClose(ForceCloseProgramRequest req) {
    stopAutosave();
  }

  // --- Core logic ------------------------------------------------------------

  private void restartAutosave() {
    stopAutosave();
    startAutosave();
  }

  private void startAutosave() {
    isOn = true;
    timeLeft = intervalSeconds;

    // schedule autosave task
    autosaveHandle = scheduler.scheduleAtFixedRate(() -> {
      Platform.runLater(() -> {
        bus.post(new WorkspaceSaveRequest());
      });
      publishSystemNotification("Autosave complete", "Your workspace has been saved.");
      timeLeft = intervalSeconds;
    }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);

    // schedule countdown
    countdownHandle = scheduler.scheduleAtFixedRate(() -> {
      timeLeft--;
      bus.post(new AutosaveTimeChangedEvent(timeLeft));
      if (timeLeft <= 0) timeLeft = intervalSeconds;
    }, 1, 1, TimeUnit.SECONDS);
  }

  private void stopAutosave() {
    isOn = false;
    if (autosaveHandle != null) autosaveHandle.cancel(false);
    if (countdownHandle != null) countdownHandle.cancel(false);
  }

  // --- System tray logic -----------------------------------------------------

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

  private void publishSystemNotification(String title, String message) {
    if (trayIcon == null) return;
    try {
      trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
    } catch (Exception e) {
      System.out.println("[AutosaveBrew] Failed to display system notification: " + e.getMessage());
    }
  }
}

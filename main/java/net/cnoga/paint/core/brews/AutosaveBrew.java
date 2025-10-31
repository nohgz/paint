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

  private boolean isEnabled;
  private int intervalSeconds = 60;
  private volatile int timeLeft;

  private ScheduledExecutorService autosaveExecutor;
  private ScheduledExecutorService countdownExecutor;
  private ScheduledFuture<?> autosaveHandle;
  private ScheduledFuture<?> countdownHandle;

  private TrayIcon trayIcon;

  public AutosaveBrew() {
    bus.register(this);
    isEnabled = false;
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onToggleAutosave(ToggleAutosaveRequest req) {
    if (isEnabled) stopAutosave();
    else startAutosave();
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onSetInterval(SetAutosaveIntervalRequest req) {
    intervalSeconds = req.minutes() * 60;
    if (isEnabled) restartAutosave();
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onSave(WorkspaceSavedEvent evt) { restartAutosave(); }

  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onSavedAs(WorkspaceSavedAsEvent evt) { restartAutosave(); }

  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onProgramClose(CloseProgramRequest req) { stopAutosave(); }

  @SubscribeEvent
  @SuppressWarnings("unused")
  private void onProgramForceClose(ForceCloseProgramRequest req) { stopAutosave(); }

  private void restartAutosave() {
    stopAutosave();
    startAutosave();
  }

  public void startAutosave() {
    if (isEnabled) return;
    isEnabled = true;
    timeLeft = intervalSeconds;

    setupSystemTray();

    // Create new executors for this cycle
    autosaveExecutor = Executors.newSingleThreadScheduledExecutor();
    countdownExecutor = Executors.newSingleThreadScheduledExecutor();

    // Autosave task
    autosaveHandle = autosaveExecutor.scheduleAtFixedRate(() -> {
      if (!isEnabled) return;
      Platform.runLater(() -> bus.post(new WorkspaceSaveRequest()));
      publishAutosaveNotification();
      timeLeft = intervalSeconds;
    }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);

    // Countdown task
    countdownHandle = countdownExecutor.scheduleAtFixedRate(() -> {
      if (!isEnabled) return;
      timeLeft--;
      bus.post(new AutosaveTimeChangedEvent(timeLeft));
      if (timeLeft <= 0) timeLeft = intervalSeconds;
    }, 1, 1, TimeUnit.SECONDS);
  }

  public void stopAutosave() {
    isEnabled = false;

    if (autosaveHandle != null) {
      autosaveHandle.cancel(true);
      autosaveHandle = null;
    }
    if (countdownHandle != null) {
      countdownHandle.cancel(true);
      countdownHandle = null;
    }

    if (autosaveExecutor != null) {
      autosaveExecutor.shutdownNow();
      autosaveExecutor = null;
    }
    if (countdownExecutor != null) {
      countdownExecutor.shutdownNow();
      countdownExecutor = null;
    }

    if (trayIcon != null) {
      SystemTray.getSystemTray().remove(trayIcon);
      trayIcon = null;
    }
  }

  private void setupSystemTray() {
    if (!SystemTray.isSupported()) return;

    SystemTray tray = SystemTray.getSystemTray();
    try {
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
      trayIcon = null;
    }
  }

  private void publishAutosaveNotification() {
    if (trayIcon == null) return;
    try {
      trayIcon.displayMessage("Pain(t)", "Your workspace has been saved.", TrayIcon.MessageType.INFO);
    } catch (Exception e) {
      System.out.println("[AutosaveBrew] Failed to display autosave notification: " + e.getMessage());
    }
  }
}
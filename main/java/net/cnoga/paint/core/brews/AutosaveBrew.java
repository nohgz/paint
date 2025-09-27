package net.cnoga.paint.core.brews;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
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

  public AutosaveBrew() {
    bus.register(this);
    this.isOn = false;
    this.intervalSeconds = 60 ;
    this.scheduler = Executors.newSingleThreadScheduledExecutor();
  }

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
    this.intervalSeconds = req.minutes() * 60;
    if (isOn) {
      restartAutosave();
    }
  }

  @SubscribeEvent
  private void onSave(WorkspaceSavedEvent evt) {
    restartAutosave();
  }


  @SubscribeEvent
  private void onSavedAs(WorkspaceSavedAsEvent evt) {
    restartAutosave();
  }

  private void restartAutosave() {
    stopAutosave();
    startAutosave();
  }

  private void startAutosave() {
    isOn = true;
    timeLeft = intervalSeconds;

    // schedule autosave
    autosaveHandle = scheduler.scheduleAtFixedRate(() -> {
      bus.post(new WorkspaceSaveRequest());
      timeLeft = intervalSeconds; // reset after each save
    }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);

    // schedule countdown print
    countdownHandle = scheduler.scheduleAtFixedRate(() -> {
      timeLeft--;
      bus.post(new AutosaveTimeChangedEvent(timeLeft));
      if (timeLeft <= 0) {
        timeLeft = intervalSeconds;
      }
    }, 1, 1, TimeUnit.SECONDS);
  }

  @SubscribeEvent
  private void onProgramClose(CloseProgramRequest req) {
    stopAutosave();
  }

  @SubscribeEvent
  private void onProgramForceClose(ForceCloseProgramRequest req) {
    stopAutosave();
  }

  private void stopAutosave() {
    isOn = false;
    if (autosaveHandle != null) {
      autosaveHandle.cancel(false);
    }
    if (countdownHandle != null) {
      countdownHandle.cancel(false);
    }
  }
}
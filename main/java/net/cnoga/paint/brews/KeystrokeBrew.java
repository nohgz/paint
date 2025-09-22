package net.cnoga.paint.brews;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.events.request.CloseCurrentWorkspaceRequest;
import net.cnoga.paint.events.request.CloseProgramRequest;
import net.cnoga.paint.events.request.FileOpenRequest;
import net.cnoga.paint.events.request.ForceCloseProgramRequest;
import net.cnoga.paint.events.request.RedoRequest;
import net.cnoga.paint.events.request.ResizeWorkspaceRequest;
import net.cnoga.paint.events.request.ShowNewWorkspacePopupRequest;
import net.cnoga.paint.events.request.UndoRequest;
import net.cnoga.paint.events.request.WorkspaceSaveRequest;
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.tool.PaintTools;

/**
 * Service for handling global keyboard shortcuts within the application.
 *
 * <p>Registers an event filter on the primary {@link Scene} to listen for
 * keystrokes and translate them into high-level events posted on the event bus. This allows
 * decoupling of UI input (keyboard shortcuts) from application logic.</p>
 */
@EventBusSubscriber
public class KeystrokeBrew extends EventBusPublisher {

  private final Scene primaryScene;

  /**
   * Creates a keystroke service bound to the given primary scene.
   *
   * @param primaryScene the scene to attach global key handlers to
   */
  public KeystrokeBrew(Scene primaryScene) {
    bus.register(this);
    this.primaryScene = primaryScene;
    initStrokes();
  }

  /**
   * Initializes all keyboard shortcuts by attaching an event filter to the primary scene.
   */
  private void initStrokes() {
    primaryScene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {

      if (keyEvent.isControlDown() && keyEvent.isShiftDown()) {
        switch (keyEvent.getCode()) {
          case Q -> {
            bus.post(new ForceCloseProgramRequest());
            keyEvent.consume();
          }
          case Z -> {
            bus.post(new RedoRequest());
            keyEvent.consume();
          }
        }
        return;
      }

      if (keyEvent.isControlDown()) {
        switch (keyEvent.getCode()) {
          case S -> {
            bus.post(new WorkspaceSaveRequest());
            keyEvent.consume();
          }
          case R -> {
            bus.post(new ResizeWorkspaceRequest());
            keyEvent.consume();
          }
          case Z -> {
            bus.post(new UndoRequest()); // redos are in ctrl+shift+z
            keyEvent.consume();
          }
          case Y -> {
            bus.post(new RedoRequest());
            keyEvent.consume();
          }
          case N -> {
            bus.post(new ShowNewWorkspacePopupRequest());
            keyEvent.consume();
          }
          case O -> {
            bus.post(new FileOpenRequest());
            keyEvent.consume();
          }
          case W -> {
            bus.post(new CloseCurrentWorkspaceRequest());
            keyEvent.consume();
          }
          case Q -> {
            bus.post(new CloseProgramRequest());
            keyEvent.consume();
          }
        }
        return;
      }

      if (keyEvent.isAltDown()) {
        switch (keyEvent.getCode()) {
          case Q -> {
            bus.post(new ToolChangedEvent(PaintTools.LINE));
            keyEvent.consume();
          }
          case W -> {
            bus.post(new ToolChangedEvent(PaintTools.SHAPES));
            keyEvent.consume();
          }
          case E -> {
            bus.post(new ToolChangedEvent(PaintTools.BRUSH));
            keyEvent.consume();
          }
          case R -> {
            bus.post(new ToolChangedEvent(PaintTools.PAN));
            keyEvent.consume();
          }
          case A -> {
            bus.post(new ToolChangedEvent(PaintTools.ERASER));
            keyEvent.consume();
          }
          case S -> {
            bus.post(new ToolChangedEvent(PaintTools.DROPPER));
            keyEvent.consume();
          }
        }
        return;
      }

      // Plain strokes
      switch (keyEvent.getCode()) {
        case SPACE -> {
          bus.post(new ToolChangedEvent(PaintTools.PAN));
          keyEvent.consume();
        }
      }
    });
  }
}

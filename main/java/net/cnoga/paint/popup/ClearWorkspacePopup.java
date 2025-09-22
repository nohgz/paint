package net.cnoga.paint.popup;

import javafx.scene.layout.Pane;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ClearWorkspaceRequest;
import net.cnoga.paint.events.request.SaveStateRequest;
import net.cnoga.paint.events.request.ShowClearWorkspacePopupRequest;
import net.cnoga.paint.events.request.UndoRequest;

@EventBusSubscriber
public class ClearWorkspacePopup extends AbstractInputPopup {

  public ClearWorkspacePopup() {
    super("Clear Canvas?");
  }

  @Override
  protected Pane buildContent() {
    return new Pane(); // doesn't really do anything atm. maybe put a snapshot of the workspace or something?
  }

  @Override
  protected void onConfirm() {
    bus.post(new ClearWorkspaceRequest());
  }

  @Override
  protected void onCancel() {
    bus.post(new UndoRequest());
    super.onCancel();
  }

  @SubscribeEvent
  protected void onOpen(ShowClearWorkspacePopupRequest req) {
    bus.post(new SaveStateRequest());
    super.show();
  }
}

package net.cnoga.paint.client.popup;

import javafx.scene.layout.Pane;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.request.ClearWorkspaceRequest;
import net.cnoga.paint.core.bus.events.request.SaveStateRequest;
import net.cnoga.paint.core.bus.events.request.ShowClearWorkspacePopupRequest;
import net.cnoga.paint.core.bus.events.request.UndoRequest;

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
  private void onOpen(ShowClearWorkspacePopupRequest req) {
    bus.post(new SaveStateRequest());
    super.show();
  }
}

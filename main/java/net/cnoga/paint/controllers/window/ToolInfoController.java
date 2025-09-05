package net.cnoga.paint.controllers.window;

import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ToolColorChangeRequest;
import net.cnoga.paint.events.request.ToolWidthChangeRequest;
import net.cnoga.paint.events.response.ToolChangedEvent;

/**
 * Controller for the tool information panel.
 *
 * <p>This panel displays information about the currently selected tool,
 * including its icon, caption, and configurable options such as color
 * and stroke width. It also provides interactive controls that allow
 * the user to customize the tool's behavior.</p>
 *
 * <p>The controller listens for {@link ToolChangedEvent} messages on the
 * event bus and updates its UI accordingly. It also publishes requests
 * (e.g., {@link ToolColorChangeRequest}, {@link ToolWidthChangeRequest})
 * when the user modifies tool parameters.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Displaying the currently selected tool's icon and caption.</li>
 *   <li>Providing a color picker for tool color customization.</li>
 *   <li>Providing a slider for tool width customization (if applicable).</li>
 *   <li>Responding to tool change events and updating the UI dynamically.</li>
 *   <li>Publishing configuration changes via the event bus.</li>
 * </ul>
 */
@EventBusSubscriber
public class ToolInfoController extends EventBusPublisher {

  public ColorPicker toolColorPicker;
  public ImageView toolInfoIcon;
  public HBox toolSpecificThings;

  public ToolInfoController() {
    bus.register(this);
  }
  public Label toolInfoCaption;

  public Slider toolWidthSlider = new Slider(1, 25, 1);

  @SubscribeEvent
  private void updateToolInformation(ToolChangedEvent event) {
    toolInfoIcon.setImage(new Image(event.tool().getIconPath()));

    toolSpecificThings.getChildren().clear();

    if (!Objects.equals(event.tool().getName(), "Pan")) {
      toolSpecificThings.getChildren().add(toolWidthSlider);
    }
  }

  @FXML
  private void initialize() {
    toolColorPicker.setValue(Color.BLACK);
    toolColorPicker.setOnAction(e -> {
      Color color = toolColorPicker.getValue();
      bus.post(new ToolColorChangeRequest(color));
    });

    // Slider setup
    toolWidthSlider.setMajorTickUnit(1);
    toolWidthSlider.setMinorTickCount(0);
    toolWidthSlider.setSnapToTicks(true);
    toolWidthSlider.setShowTickMarks(true);
    toolWidthSlider.setShowTickLabels(true);

    // Tooltip for current value
    Tooltip valueTooltip = new Tooltip(String.valueOf((int) toolWidthSlider.getValue()));
    Tooltip.install(toolWidthSlider, valueTooltip);

    // Update tooltip while dragging
    toolWidthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
      valueTooltip.setText(String.valueOf(newVal.intValue()));
    });

    // Only print value when user releases the mouse
    toolWidthSlider.setOnMouseReleased(e -> {
      bus.post(new ToolWidthChangeRequest((int) toolWidthSlider.getValue()));
    });
  }
}

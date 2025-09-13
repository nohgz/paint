package net.cnoga.paint.controllers.window;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import net.cnoga.paint.bus.EventBusPublisher;
import net.cnoga.paint.bus.EventBusSubscriber;
import net.cnoga.paint.bus.SubscribeEvent;
import net.cnoga.paint.events.request.ColorChangedEvent;
import net.cnoga.paint.events.request.WidthChangedEvent;
import net.cnoga.paint.events.response.ShapeChangedEvent;
import net.cnoga.paint.events.response.ToolChangedEvent;
import net.cnoga.paint.tool.capabilities.WidthCapability;
import net.cnoga.paint.util.ShapeType;
import net.cnoga.paint.tool.ShapesTool;

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
 * (e.g., {@link ColorChangedEvent}, {@link WidthChangedEvent})
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

  public Label numberWidth = new Label("1");

  public Slider toolWidthSlider = new Slider(1, 25, 1);

  @SubscribeEvent
  private void updateWidthText(WidthChangedEvent evt) {
    numberWidth.setText(evt.width().toString());
  }

  @SubscribeEvent
  private void updateToolInformation(ToolChangedEvent evt) {
    toolInfoIcon.setImage(new Image(evt.tool().getIconPath()));

    toolSpecificThings.getChildren().clear();

    if (evt.tool() instanceof WidthCapability) {
      toolSpecificThings.getChildren().add(new Label("Line Width: "));
      toolSpecificThings.getChildren().add(toolWidthSlider);
      toolSpecificThings.getChildren().add(numberWidth);
      toolSpecificThings.getChildren().add(new Separator(Orientation.VERTICAL));
    }

    if (evt.tool() instanceof ShapesTool) {
      toolSpecificThings.getChildren().add(new Label("Shape: "));
      ComboBox<ShapeType> shapeDropdown = new ComboBox<>();
      shapeDropdown.setItems(FXCollections.observableArrayList(ShapeType.values()));

      // optionally set the current shape
      ShapesTool shapesTool = (ShapesTool) evt.tool();
      shapeDropdown.setValue(shapesTool.getCurrentShape());

      shapeDropdown.setOnAction(e -> {
        ShapeType selected = shapeDropdown.getValue();
        // post event to update the ShapesTool
        bus.post(new ShapeChangedEvent(selected, shapesTool.getPolygonSides()));
      });

      toolSpecificThings.getChildren().add(shapeDropdown);
      toolSpecificThings.getChildren().add(new Separator(Orientation.VERTICAL));
    }
  }

  @SubscribeEvent
  private void updateColorPicker(ColorChangedEvent evt) {
    toolColorPicker.setValue(evt.color());
  }

  @FXML
  private void initialize() {
    toolColorPicker.setValue(Color.BLACK);
    toolColorPicker.setOnAction(e -> {
      Color color = toolColorPicker.getValue();
      bus.post(new ColorChangedEvent(color));
    });

    // Slider setup
    toolWidthSlider.setMajorTickUnit(1);
    toolWidthSlider.setMinorTickCount(0);
    toolWidthSlider.setSnapToTicks(true);
    toolWidthSlider.setShowTickMarks(true);
//    toolWidthSlider.setShowTickLabels(true);

    Tooltip valueTooltip = new Tooltip(String.valueOf((int) toolWidthSlider.getValue()));
    Tooltip.install(toolWidthSlider, valueTooltip);
    toolWidthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
      valueTooltip.setText(String.valueOf(newVal.intValue()));
    });

    toolWidthSlider.setOnMouseReleased(e -> {
      bus.post(new WidthChangedEvent((int) toolWidthSlider.getValue()));
    });
  }
}

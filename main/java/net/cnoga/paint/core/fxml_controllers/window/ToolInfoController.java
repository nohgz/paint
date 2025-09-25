package net.cnoga.paint.core.fxml_controllers.window;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.EventBusSubscriber;
import net.cnoga.paint.core.bus.SubscribeEvent;
import net.cnoga.paint.core.bus.events.response.ColorChangedEvent;
import net.cnoga.paint.core.bus.events.response.ShapeChangedEvent;
import net.cnoga.paint.core.bus.events.response.ToolChangedEvent;
import net.cnoga.paint.core.bus.events.response.WidthChangedEvent;
import net.cnoga.paint.core.tool.ShapesTool;
import net.cnoga.paint.core.tool.capabilities.WidthCapability;
import net.cnoga.paint.core.util.ShapeConfig;
import net.cnoga.paint.core.util.ShapeType;

@EventBusSubscriber
public class ToolInfoController extends EventBusPublisher {

  public ColorPicker toolColorPicker;
  public ImageView toolInfoIcon;
  public HBox toolSpecificThings;
  public Label toolInfoCaption;

  // Brush size controls (global)
  private Spinner<Integer> brushSizeSpinner;

  // Shape controls (global)
  private ComboBox<ShapeType> shapeDropdown;
  private Spinner<Integer> sidesSpinner;
  private CheckBox rightTriangleCheckbox;


  public ToolInfoController() {
    bus.register(this);
  }

  @SubscribeEvent
  private void updateToolInformation(ToolChangedEvent evt) {
    toolInfoIcon.setImage(new Image(evt.tool().getIconPath()));
    toolSpecificThings.getChildren().clear();

    // Always show brush size if tool supports it
    if (evt.tool() instanceof WidthCapability) {
      toolSpecificThings.getChildren().add(new Label("Brush Size: "));
      toolSpecificThings.getChildren().addAll(brushSizeSpinner);
      toolSpecificThings.getChildren().add(new Separator(Orientation.VERTICAL));
    }

    // ShapesTool specific controls
    if (evt.tool() instanceof ShapesTool shapesTool) {

      toolSpecificThings.getChildren().add(new Label("Shape: "));
      shapeDropdown.setValue(shapesTool.getShapeConfig().type());
      toolSpecificThings.getChildren().add(shapeDropdown);
      toolSpecificThings.getChildren().add(new Separator(Orientation.VERTICAL));

      ShapeConfig currentConfig = ShapeConfig.defaultFor(shapesTool.getShapeConfig().type());
      updateShapeControls(currentConfig);
    }
  }

  /**
   * Initialize brush size spinner once, keep global
   */
  private void initBrushSizeControls() {
    brushSizeSpinner = new Spinner<>(1, 100, 1, 1);
    brushSizeSpinner.setEditable(true);
    brushSizeSpinner.setPrefWidth(60); // small width
    brushSizeSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
      bus.post(new WidthChangedEvent(brushSizeSpinner.getValue()))
    );
  }

  /**
   * Initialize shape dropdown once, keep global
   */
  private void initShapeControls() {
    shapeDropdown = new ComboBox<>();
    shapeDropdown.setItems(FXCollections.observableArrayList(ShapeType.values()));
    shapeDropdown.setOnAction(e -> {
      ShapeType selected = shapeDropdown.getValue();
      ShapeConfig config = ShapeConfig.defaultFor(selected);
      bus.post(new ShapeChangedEvent(config));
      updateShapeControls(config);
    });
  }

  /**
   * Add contextual shape controls based on type
   */
  private void updateShapeControls(ShapeConfig config) {
    // Remove any old shape-specific controls, but leave brush + shape dropdown
    toolSpecificThings.getChildren().removeIf(node ->
      node instanceof Spinner && node != brushSizeSpinner ||
        node instanceof CheckBox ||
        (node instanceof Label && !"Shape: ".equals(((Label) node).getText())
          && !"Brush Size: ".equals(((Label) node).getText()))
    );

    if (config.type() == ShapeType.N_GON) {
      Label sidesLabel = new Label("Sides: ");
      sidesSpinner = new Spinner<>(3, 50, config.sides(), 1);
      sidesSpinner.setEditable(true);
      sidesSpinner.setPrefWidth(60);
      sidesSpinner.valueProperty().addListener((obs, oldVal, newVal) -> postShapeChange());

      toolSpecificThings.getChildren().addAll(sidesLabel, sidesSpinner);
    }

    if (config.type() == ShapeType.TRIANGLE) {
      rightTriangleCheckbox = new CheckBox("Right Triangle");
      rightTriangleCheckbox.setSelected(config.isRightTriangle());
      rightTriangleCheckbox.setOnAction(e -> postShapeChange());
      toolSpecificThings.getChildren().add(rightTriangleCheckbox);
    }
  }

  private void postShapeChange() {
    ShapeType currentShape = shapeDropdown.getValue();
    int sides = (sidesSpinner != null) ? sidesSpinner.getValue() : 0;
    boolean isRight = (rightTriangleCheckbox != null && rightTriangleCheckbox.isSelected());

    ShapeConfig config = new ShapeConfig(currentShape, sides, isRight);
    bus.post(new ShapeChangedEvent(config));
  }

  @SubscribeEvent
  private void updateColorPicker(ColorChangedEvent evt) {
    toolColorPicker.setValue(evt.color());
  }

  @FXML
  private void initialize() {
    toolColorPicker.setValue(Color.BLACK);
    toolColorPicker.setOnAction(e -> bus.post(new ColorChangedEvent(toolColorPicker.getValue())));

    initBrushSizeControls();
    initShapeControls();
  }
}
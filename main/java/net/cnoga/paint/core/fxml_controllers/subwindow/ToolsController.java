package net.cnoga.paint.core.fxml_controllers.subwindow;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import net.cnoga.paint.core.bus.EventBusPublisher;
import net.cnoga.paint.core.bus.events.response.ToolChangedEvent;
import net.cnoga.paint.core.tool.PaintTools;
import net.cnoga.paint.core.tool.Tool;

/**
 * JavaFX controller for the tool palette in the main workspace.
 *
 * <p>This controller builds and manages the grid of available drawing tools,
 * each represented by a toggle button with an icon. Selecting a tool posts a
 * {@link ToolChangedEvent} to the application {@link net.cnoga.paint.core.bus.EventBus}, allowing
 * the workspace to update its interaction mode accordingly.</p>
 *
 *
 * <h3>Layout:</h3>
 * <ul>
 *   <li>Two equal-width columns.</li>
 *   <li>Six equal-height rows (for up to 12 tools).</li>
 *   <li>Each cell contains a toggle button with the tool's icon.</li>
 * </ul>
 *
 * @author cnoga
 * @version 1.0
 */
public class ToolsController extends EventBusPublisher {

  private final ToggleGroup toolGroup = new ToggleGroup();
  @FXML
  private GridPane toolGrid;

  @FXML
  public void initialize() {
    setupGrid();
    populateTools();
  }

  private void setupGrid() {
    // Two equal-width columns
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(50);
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(50);
    toolGrid.getColumnConstraints().addAll(col1, col2);

    // Six equal-height rows
    for (int i = 0; i < 6; i++) {
      RowConstraints row = new RowConstraints();
      row.setPercentHeight(100.0 / 6);
      toolGrid.getRowConstraints().add(row);
    }
  }

  private void populateTools() {
    int col = 0, row = 0;

    for (Tool tool : PaintTools.ALL_TOOLS) {
      ToggleButton btn = new ToggleButton();

      ImageView icon = new ImageView(tool.getIconPath());
      icon.setFitHeight(20);
      icon.setFitWidth(20);

      btn.setGraphic(icon);
      btn.setToggleGroup(toolGroup);

      Tooltip tooltip = new Tooltip(tool.getName());
      Tooltip.install(btn, tooltip);

      btn.setOnAction(e -> {
        if (btn.isSelected()) {
          bus.post(new ToolChangedEvent(tool));
        }
      });

      toolGrid.add(btn, col, row);
      col++;
      if (col >= 2) {
        col = 0;
        row++;
      }
    }
  }
}

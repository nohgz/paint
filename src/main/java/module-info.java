/**
 * Defines the main module for the Pain(t) application.
 *
 * <p>This module provides the entry point and core infrastructure for the Paint app,
 * including the event bus, Brew services, FXML controllers, tool system, and general utilities.</p>
 *
 * <h2>Exported Packages</h2>
 * <ul>
 *   <li>{@code net.cnoga.paint} -- Application entry point and initialization.</li>
 *   <li>{@code net.cnoga.paint.core.bus} -- Event-driven communication layer.</li>
 *   <li>{@code net.cnoga.paint.core.brews} -- Application-level services reacting to events.</li>
 *   <li>{@code net.cnoga.paint.core.fxml_controllers} -- FXML-bound UI controllers.</li>
 *   <li>{@code net.cnoga.paint.core.tool} -- Drawing and editing tools.</li>
 *   <li>{@code net.cnoga.paint.core.util} -- Shared utility classes and helpers.</li>
 * </ul>
 *
 *
 * <p>Depends on JavaFX (Controls, FXML, Swing), Apache Commons Imaging for image processing,
 * Log4j for logging, and the built-in HTTP server module.</p>
 */
module net.cnoga.paint {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.swing;
  requires org.apache.commons.imaging;
  requires jdk.httpserver;
  requires log4j.api;

  opens net.cnoga.paint to javafx.fxml;
  opens net.cnoga.paint.core.bus to javafx.fxml;
  opens net.cnoga.paint.core.fxml_controllers.window to javafx.fxml;
  opens net.cnoga.paint.core.fxml_controllers.subwindow to javafx.fxml;
  opens net.cnoga.paint.core.fxml_controllers to javafx.fxml;

  exports net.cnoga.paint;
  exports net.cnoga.paint.core.bus;
  exports net.cnoga.paint.core.brews;
}
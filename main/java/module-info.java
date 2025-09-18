module net.cnoga.paint {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.swing;
  requires org.apache.commons.imaging;

  opens net.cnoga.paint to javafx.fxml;
  opens net.cnoga.paint.bus to javafx.fxml;
  opens net.cnoga.paint.fxml_controllers.window to javafx.fxml;
  opens net.cnoga.paint.fxml_controllers.subwindow to javafx.fxml;

  exports net.cnoga.paint;
  exports net.cnoga.paint.fxml_controllers.window;
  exports net.cnoga.paint.fxml_controllers.subwindow;
  exports net.cnoga.paint.fxml_controllers;
  opens net.cnoga.paint.fxml_controllers to javafx.fxml;
}
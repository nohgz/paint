module net.cnoga.paint {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.swing;

  opens net.cnoga.paint to javafx.fxml;
  exports net.cnoga.paint;
  exports net.cnoga.paint.controllers;
  opens net.cnoga.paint.controllers to javafx.fxml;
}
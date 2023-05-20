module hse.java.elevators {
    requires javafx.controls;
    requires javafx.fxml;


    opens hse.java.elevators to javafx.fxml;
    exports hse.java.elevators;
    exports hse.java.elevators.controller;
    opens hse.java.elevators.controller to javafx.fxml;
    exports hse.java.elevators.model;
    opens hse.java.elevators.model to javafx.fxml;
    exports hse.java.elevators.entity;
    opens hse.java.elevators.entity to javafx.fxml;
}
package hse.java.elevators.controller;

import hse.java.elevators.model.Building;
import hse.java.elevators.entity.Ride;
import hse.java.elevators.model.RidesGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ElevatorsMainPageController {

    @FXML
    private TextArea log;
    @FXML
    private TextField elevatorsNumberInput;
    @FXML
    private TextField generationFrequencyInput;
    @FXML
    private TextField maxFloorInput;
    @FXML
    private TextField minFloorInput;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    private RidesGenerator ridesGenerator;
    private Building building;

    @FXML
    void startButtonClicked() {
        log.setText("");
        log.setPrefColumnCount(1000);
        log.setPrefRowCount(Integer.MAX_VALUE);
        afterStart();

        int minFloor;
        int maxFloor;
        int elevatorsNumber;
        int generationFrequency;

        try {
            minFloor = Integer.parseInt(minFloorInput.getText());
        } catch (NumberFormatException e) {
            log.setText("Error: Wrong min floor format");
            beforeStart();
            return;
        }

        try {
            maxFloor = Integer.parseInt(maxFloorInput.getText());
        } catch (NumberFormatException e) {
            log.setText("Error: Wrong max floor format");
            beforeStart();
            return;
        }

        try {
            elevatorsNumber = Integer.parseInt(elevatorsNumberInput.getText());
        } catch (NumberFormatException e) {
            log.setText("Error: Wrong elevators number format");
            beforeStart();
            return;
        }

        try {
            generationFrequency = Integer.parseInt(generationFrequencyInput.getText());
        } catch (NumberFormatException e) {
            log.setText("Error: Wrong generation frequency format");
            beforeStart();
            return;
        }

        if (minFloor >= maxFloor) {
            log.setText("Error: Min floor > Max floor");
            beforeStart();
            return;
        }

        if (generationFrequency < 1000) {
            log.setText("Error: Generation frequency must be >= 1000");
            beforeStart();
            return;
        }

        if (elevatorsNumber < 1 || elevatorsNumber > 12) {
            log.setText("Error: Elevators number should be between 1 and 12 inclusive");
            beforeStart();
            return;
        }

        BlockingQueue<Ride> requests = new LinkedBlockingQueue<>();

        ridesGenerator = new RidesGenerator(requests, minFloor, maxFloor, generationFrequency);
        ridesGenerator.start();

        building = new Building(requests, elevatorsNumber, log);
        building.start();
    }

    @FXML
    void stopButtonClicked() {
        log.appendText("Shutting down...\n");

        ridesGenerator.stopGeneration();
        building.stopWorking();

        beforeStart();
    }

    private void afterStart() {
        startButton.setVisible(false);
        stopButton.setVisible(true);

        elevatorsNumberInput.setDisable(true);
        generationFrequencyInput.setDisable(true);
        minFloorInput.setDisable(true);
        maxFloorInput.setDisable(true);
    }

    private void beforeStart() {
        startButton.setVisible(true);
        stopButton.setVisible(false);

        elevatorsNumberInput.setDisable(false);
        generationFrequencyInput.setDisable(false);
        minFloorInput.setDisable(false);
        maxFloorInput.setDisable(false);
    }
}
package com.example.student_registration_system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class MainController {

    @FXML
    private Button manageStudentsButton;

    @FXML
    private Button manageGroupsButton;

    @FXML
    private Button manageAttendanceButton;

    @FXML
    private Button generateReportButton;

    @FXML
    private void handleManageStudents() {
        switchScene("StudentView.fxml");
    }

    @FXML
    private void handleManageGroups() {
        switchScene("GroupView.fxml");
    }

    @FXML
    private void handleManageAttendance() {
        switchScene("AttendanceView.fxml");
    }

    @FXML
    private void handleGenerateReport() {
        switchScene("ReportView.fxml");
    }

    private void switchScene(String fxmlFile) {
        try {
            Stage stage = (Stage) manageStudentsButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/student_registration_system/" + fxmlFile));
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
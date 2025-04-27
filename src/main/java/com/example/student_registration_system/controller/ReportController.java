package com.example.student_registration_system.controller;

import com.example.student_registration_system.model.AttendanceRecord;
import com.example.student_registration_system.model.Student;
import com.example.student_registration_system.service.ReportService;
import com.example.student_registration_system.service.ReportServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.time.LocalDate;
import java.util.List;

public class ReportController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    private ReportService reportService = new ReportServiceImpl();

    @FXML
    private void handleGenerateReport() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Invalid Input", "Please select both a start and end date.");
            return;
        }

        // Get the list of students and attendance records for the specified date range
        List<Student> students = reportService.getStudentsWithAttendance(startDate, endDate);
        List<AttendanceRecord> attendanceRecords = reportService.getAttendanceRecords(startDate, endDate);

        if (students.isEmpty() || attendanceRecords.isEmpty()) {
            showAlert("No Data", "No attendance records found for the selected date range.");
            return;
        }

        // Save the report as a PDF file
        String filePath = "AttendanceReport_" + startDate + "_to_" + endDate + ".pdf";
        reportService.saveReportAsPDF(students, attendanceRecords, filePath);

        showAlert("Report Generated", "The report has been saved to: " + filePath);
    }

    @FXML
    private void handleBackToMain() {
        switchScene("MainView.fxml");
    }

    private void switchScene(String fxmlFile) {
        try {
            Stage stage = (Stage) startDatePicker.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/student_registration_system/" + fxmlFile));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
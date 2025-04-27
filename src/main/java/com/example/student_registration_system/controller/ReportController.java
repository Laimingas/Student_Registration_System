package com.example.student_registration_system.controller;

import com.example.student_registration_system.model.AttendanceRecord;
import com.example.student_registration_system.model.Student;
import com.example.student_registration_system.service.*;
import com.example.student_registration_system.model.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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

    @FXML
    private ComboBox<Group> groupPicker; // Added group selection ComboBox

    private ReportService reportService = new ReportServiceImpl();
    private GroupServiceImpl groupService = new GroupServiceImpl(); // Assume this retrieves groups
    private AttendanceService attendanceService = AttendanceServiceImpl.getInstance();
    private ObservableList<Group> groupData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Load groups into the ComboBox
        loadGroupData();
        attendanceService.loadAttendanceData();
    }

    private void loadGroupData() {
        Group noneOption = new Group("None", null); // "None" option for all groups
        groupData.add(noneOption);
        groupData.addAll(groupService.getAllGroups()); // Load all groups
        groupPicker.setItems(groupData);
        groupPicker.getSelectionModel().select(noneOption); // Default to "None"
    }


    @FXML
    private void handleGenerateReport() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Group selectedGroup = groupPicker.getSelectionModel().getSelectedItem();

        if (startDate == null || endDate == null) {
            showAlert("Invalid Input", "Please select both a start and end date.");
            return;
        }

        if (selectedGroup == null) {
            showAlert("Invalid Input", "Please select a group.");
            return;
        }

        // Filter students and attendance records based on the selected group
        List<Student> students;
        List<AttendanceRecord> attendanceRecords;
        if ("None".equals(selectedGroup.getName())) {
            students = reportService.getStudentsWithAttendance(startDate, endDate);
            attendanceRecords = reportService.getAttendanceRecords(startDate, endDate);
        } else {
            students = groupService.getStudentsInGroup(selectedGroup.getId());
            attendanceRecords = reportService.getAttendanceRecordsByGroup(selectedGroup.getId(), startDate, endDate);
        }

        if (students.isEmpty() || attendanceRecords.isEmpty()) {
            showAlert("No Data", "No attendance records found for the selected group and date range.");
            System.out.println("Selected Group Students: " + students); // Debug print
            attendanceRecords = reportService.getAttendanceRecordsByGroup(selectedGroup.getId(), startDate, endDate);
            System.out.println("Filtered Attendance Records for Group: " + attendanceRecords); // Debug print
            return;
        }

        List<LocalDate> dates = attendanceService.getDatesInRange(attendanceRecords, startDate, endDate);

        // Save the report as a PDF file
        String filePath = "AttendanceReport_" + selectedGroup.getName() + "_" + startDate + "_to_" + endDate + ".pdf";
        //reportService.saveReportAsPDF(students, attendanceRecords, filePath);
        reportService.saveReportAsPDF(students, attendanceRecords, filePath, selectedGroup.getName(), dates);

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
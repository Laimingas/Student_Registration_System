package com.example.student_registration_system.controller;

import com.example.student_registration_system.model.AttendanceRecord;
import com.example.student_registration_system.model.Group;
import com.example.student_registration_system.model.Student;
import com.example.student_registration_system.service.AttendanceService;
import com.example.student_registration_system.service.AttendanceServiceImpl;
import com.example.student_registration_system.service.GroupService;
import com.example.student_registration_system.service.GroupServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class AttendanceController {

    @FXML
    private ComboBox<Group> groupPicker;

    @FXML
    private ComboBox<Student> studentPicker;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private CheckBox showFilledOnly;

    @FXML
    private TableView<AttendanceRecord> attendanceTable;

    @FXML
    private TableColumn<AttendanceRecord, String> studentIdColumn;

    private GroupService groupService = new GroupServiceImpl();
    private AttendanceService attendanceService = new AttendanceServiceImpl();
    private ObservableList<Group> groupData = FXCollections.observableArrayList();
    private ObservableList<Student> studentData = FXCollections.observableArrayList();
    private ObservableList<AttendanceRecord> attendanceData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Populate group picker
        groupPicker.setItems(groupData);
        loadGroupData();

        // Populate student picker dynamically when a group is selected
        groupPicker.setOnAction(event -> handleGroupSelection());

        // Configure student picker to display student IDs properly
        studentPicker.setCellFactory(lv -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                setText(empty ? null : student.getId()); // Display student ID
            }
        });

        // Configure table columns
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        // Load attendance data on initialization
        attendanceService.loadAttendanceData();
    }

    @FXML
    private void handleGroupSelection() {
        Group selectedGroup = groupPicker.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            // Populate the student picker with students from the selected group
            studentData.setAll(selectedGroup.getStudents());
            studentPicker.setItems(studentData);
        }
    }

    @FXML
    private void handleShowAttendance() {
        Group selectedGroup = groupPicker.getSelectionModel().getSelectedItem();
        Student selectedStudent = studentPicker.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        boolean showOnlyFilled = showFilledOnly.isSelected();

        if (startDate == null || endDate == null) {
            showAlert("Invalid Input", "Please select a valid start and end date.");
            return;
        }

        if (selectedGroup == null) {
            showAlert("Invalid Input", "Please select a group.");
            return;
        }

        List<Student> students = selectedGroup.getStudents();
        if (selectedStudent != null) {
            students = students.stream()
                    .filter(student -> student.equals(selectedStudent))
                    .collect(Collectors.toList());
        }

        if (students.isEmpty()) {
            showAlert("No Data", "No students found for the selected criteria.");
            return;
        }

        attendanceTable.getColumns().clear();
        attendanceData.clear();

        // Add Student ID column
        attendanceTable.getColumns().add(studentIdColumn);

        // Add date columns dynamically
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            final LocalDate currentDate = date; // Create an effectively final variable
            if (showOnlyFilled && attendanceService.getFilledDates().stream().noneMatch(d -> d.equals(currentDate))) {
                continue; // Skip unmarked dates if "Show Only Filled Days" is selected
            }

            TableColumn<AttendanceRecord, String> dateColumn = new TableColumn<>(currentDate.toString());
            dateColumn.setCellValueFactory(cellData -> cellData.getValue().attendanceProperty(currentDate));
            attendanceTable.getColumns().add(dateColumn);
        }

        // Populate attendance data
        for (Student student : students) {
            AttendanceRecord record = new AttendanceRecord(student.getId());
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                final LocalDate currentDate = date; // Create an effectively final variable
                String attendance = attendanceService.getAttendanceByStudent(student.getId()).stream()
                        .filter(r -> currentDate.equals(r.getDate()))
                        .map(r -> r.getAttendance(currentDate))
                        .findFirst()
                        .orElse("");
                record.setAttendance(currentDate, attendance); // Use currentDate
            }
            attendanceData.add(record);
        }

        attendanceTable.setItems(attendanceData);
    }

    @FXML
    private void handleMarkAttendance() {
        AttendanceRecord selectedRecord = attendanceTable.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            String selectedColumn = attendanceTable.getSelectionModel().getSelectedCells().get(0).getTableColumn().getText();
            if (!selectedColumn.equals("Student ID")) {
                LocalDate date = LocalDate.parse(selectedColumn);
                String currentAttendance = selectedRecord.getAttendance(date);
                if ("X".equals(currentAttendance)) {
                    selectedRecord.setAttendance(date, "");  // Unmark attendance
                    attendanceService.markAttendance(selectedRecord.getStudentId(), date, false);
                } else {
                    selectedRecord.setAttendance(date, "X"); // Mark attendance
                    attendanceService.markAttendance(selectedRecord.getStudentId(), date, true);
                }
                attendanceTable.refresh();
                attendanceService.saveAttendanceData();  // Save attendance data to file
            }
        }
    }

    @FXML
    private void handleBackToMain() {
        switchScene("MainView.fxml");
    }

    private void switchScene(String fxmlFile) {
        try {
            Stage stage = (Stage) studentPicker.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/student_registration_system/" + fxmlFile));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleShowFilledDays() {
        handleShowAttendance(); // Re-trigger attendance display with "Show Only Filled Days" logic
    }

    private void loadGroupData() {
        groupData.setAll(groupService.getAllGroups());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
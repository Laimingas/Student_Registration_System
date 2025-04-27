package com.example.student_registration_system.controller;

import com.example.student_registration_system.model.Student;
import com.example.student_registration_system.service.StudentService;
import com.example.student_registration_system.service.StudentServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class StudentController {

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField deleteStudentIdField;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> studentIdColumn;

    @FXML
    private TableColumn<Student, String> nameColumn;

    @FXML
    private TableColumn<Student, String> emailColumn;

    private StudentService studentService = new StudentServiceImpl();
    private ObservableList<Student> studentData = FXCollections.observableArrayList();

    private Student selectedStudent = null;

    @FXML
    private void initialize() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        studentTable.setItems(studentData);
        loadStudentData();

        studentTable.setOnMouseClicked(this::handleTableClick);
    }

    @FXML
    private void handleAddStudent() {
        String id = studentIdField.getText();
        String name = nameField.getText();
        String email = emailField.getText();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
            showAlert("Invalid Input", "All fields must be filled!");
            return;
        }

        if (selectedStudent != null) {
            selectedStudent.setId(id);
            selectedStudent.setName(name);
            selectedStudent.setEmail(email);
            studentService.updateStudent(selectedStudent);
            studentTable.refresh();
        } else {
            if (studentService.getStudentById(id) != null) {
                showAlert("Error", "A student with this ID already exists. Please use a different ID.");
                return;
            }

            Student newStudent = new Student(id, name, email);
            studentService.addStudent(newStudent);
            studentData.add(newStudent);
            newStudent.displayInfo();
        }

        clearInputFields();
        selectedStudent = null;
    }

    @FXML
    private void handleDeleteStudent() {
        String id = deleteStudentIdField.getText();
        Student student = studentService.getStudentById(id);
        if (student != null) {
            studentService.deleteStudent(id);
            studentData.removeIf(s -> s.getId().equals(id));
            student.displayInfo();
        }
        deleteStudentIdField.clear();
    }

    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            studentService.importStudentsFromCSV(file);
            loadStudentData();
        }
    }

    @FXML
    private void handleBackToMain() {
        switchScene("MainView.fxml");
    }

    private void handleTableClick(MouseEvent event) {
        selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            studentIdField.setText(selectedStudent.getId());
            nameField.setText(selectedStudent.getName());
            emailField.setText(selectedStudent.getEmail());
        }
    }

    private void clearInputFields() {
        studentIdField.clear();
        nameField.clear();
        emailField.clear();
    }

    private void switchScene(String fxmlFile) {
        try {
            Stage stage = (Stage) studentIdField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/student_registration_system/" + fxmlFile));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStudentData() {
        studentData.setAll(studentService.getAllStudents());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
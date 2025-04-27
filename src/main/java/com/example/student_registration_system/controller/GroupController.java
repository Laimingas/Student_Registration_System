package com.example.student_registration_system.controller;

import com.example.student_registration_system.model.Group;
import com.example.student_registration_system.model.Student;
import com.example.student_registration_system.service.GroupService;
import com.example.student_registration_system.service.StudentService;
import com.example.student_registration_system.service.GroupServiceImpl;
import com.example.student_registration_system.service.StudentServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class GroupController {

    @FXML
    private TextField groupNameField;

    @FXML
    private ComboBox<Group> groupPicker;

    @FXML
    private ComboBox<Student> studentIdPicker;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> studentIdColumn;

    private GroupService groupService = new GroupServiceImpl();
    private StudentService studentService = new StudentServiceImpl();
    private ObservableList<Group> groupData = FXCollections.observableArrayList();
    private ObservableList<Student> studentData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        studentTable.setItems(studentData);
        groupPicker.setItems(groupData);
        studentIdPicker.setItems(FXCollections.observableArrayList(studentService.getAllStudents()));
        loadGroupData();
    }

    @FXML
    private void handleAddGroup() {
        String name = groupNameField.getText();
        String id = String.valueOf(groupData.size() + 1);  // Generate a simple ID

        Group group = new Group(id, name);
        groupService.addGroup(group);
        groupData.add(group);

        groupNameField.clear();
    }

    @FXML
    private void handleDeleteGroup() {
        Group selectedGroup = groupPicker.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            groupService.deleteGroup(selectedGroup.getId());
            groupData.remove(selectedGroup);
            studentData.clear();  // Clear student table when group is deleted
        }
    }

    @FXML
    private void handleGroupSelection() {
        Group selectedGroup = groupPicker.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            studentIdColumn.setText("Students in " + selectedGroup.getName());
            studentData.setAll(selectedGroup.getStudents());

            // Display detailed information about students in the console
            System.out.println("Details of students in group: " + selectedGroup.getName());
            for (Student student : selectedGroup.getStudents()) {
                System.out.println(student.getDetails());
            }
        } else {
            studentData.clear();
        }
    }

    @FXML
    private void handleAddStudentToGroup() {
        Student selectedStudent = studentIdPicker.getSelectionModel().getSelectedItem();
        Group selectedGroup = groupPicker.getSelectionModel().getSelectedItem();

        if (selectedGroup != null && selectedStudent != null) {
            if (selectedGroup.getStudents().contains(selectedStudent)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("This student is already in the group.");
                alert.showAndWait();
                return;
            }

            groupService.addStudentToGroup(selectedStudent.getId(), selectedGroup.getId());
            studentData.setAll(selectedGroup.getStudents());  // Refresh the student table
            studentIdPicker.getSelectionModel().clearSelection();

            // Display detailed information about the added student
            selectedStudent.displayInfo();
        }
    }

    @FXML
    private void handleBackToMain() {
        switchScene("MainView.fxml");
    }

    private void switchScene(String fxmlFile) {
        try {
            Stage stage = (Stage) groupNameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/student_registration_system/" + fxmlFile));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGroupData() {
        groupData.setAll(groupService.getAllGroups());
    }
}
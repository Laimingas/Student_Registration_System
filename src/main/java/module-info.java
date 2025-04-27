module com.example.student_registration_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires itextpdf;
    requires java.sql; // This is required by iText
    requires javafx.graphics;
    requires javafx.base;
    requires  org.apache.pdfbox;

    opens com.example.student_registration_system to javafx.fxml;
    exports com.example.student_registration_system;
    opens com.example.student_registration_system.controller to javafx.fxml; // Open the controller package
    opens com.example.student_registration_system.model to javafx.base;
    exports com.example.student_registration_system.controller;
    exports com.example.student_registration_system.model;
}
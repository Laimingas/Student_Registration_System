package com.example.student_registration_system.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AttendanceRecord implements Serializable {
    private String studentId; // Replace StringProperty with standard String
    private Map<LocalDate, String> attendanceMap; // Use String instead of StringProperty
    private LocalDate date; // Keep LocalDate as is
    private boolean present;

    // Transient properties for JavaFX bindings (not serialized)
    private transient StringProperty studentIdProperty;
    private transient Map<LocalDate, StringProperty> transientAttendanceMap;

    public AttendanceRecord(String studentId) {
        this.studentId = studentId;
        this.attendanceMap = new HashMap<>();
        initTransientFields();
    }

    public AttendanceRecord(String studentId, LocalDate date, boolean present) {
        this.studentId = studentId;
        this.attendanceMap = new HashMap<>();
        this.date = date;
        this.present = present;
        setAttendance(date, present ? "X" : "");
        initTransientFields();
    }

    // Initialize transient fields for JavaFX bindings
    private void initTransientFields() {
        this.studentIdProperty = new SimpleStringProperty(studentId);
        this.transientAttendanceMap = new HashMap<>();
        for (Map.Entry<LocalDate, String> entry : attendanceMap.entrySet()) {
            transientAttendanceMap.put(entry.getKey(), new SimpleStringProperty(entry.getValue()));
        }
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
        if (studentIdProperty != null) {
            studentIdProperty.set(studentId);
        }
    }

    public StringProperty studentIdProperty() {
        if (studentIdProperty == null) {
            studentIdProperty = new SimpleStringProperty(studentId);
        }
        return studentIdProperty;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAttendance(LocalDate date) {
        return attendanceMap.getOrDefault(date, "");
    }

    public void setAttendance(LocalDate date, String attendance) {
        attendanceMap.put(date, attendance);
        if (transientAttendanceMap != null) {
            transientAttendanceMap.computeIfAbsent(date, k -> new SimpleStringProperty()).set(attendance);
        }
    }

    public StringProperty attendanceProperty(LocalDate date) {
        if (transientAttendanceMap == null) {
            transientAttendanceMap = new HashMap<>();
        }
        return transientAttendanceMap.computeIfAbsent(date, k -> new SimpleStringProperty(getAttendance(date)));
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isPresent() {
        return present;
    }

    // Restore transient fields after deserialization
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject(); // Default deserialization
        initTransientFields(); // Reinitialize transient fields
    }
}


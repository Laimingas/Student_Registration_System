package com.example.student_registration_system.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AttendanceRecord implements Serializable {
    private final StringProperty studentId;
    private final Map<LocalDate, StringProperty> attendanceMap;
    private LocalDate date; // Ensure this is a LocalDate

    public AttendanceRecord(String studentId) {
        this.studentId = new SimpleStringProperty(studentId);
        this.attendanceMap = new HashMap<>();
    }

    public AttendanceRecord(String studentId, LocalDate date, boolean present) {
        this.studentId = new SimpleStringProperty(studentId);
        this.attendanceMap = new HashMap<>();
        this.date = date;
        setAttendance(date, present ? "X" : "");
    }

    public String getStudentId() {
        return studentId.get();
    }

    public StringProperty studentIdProperty() {
        return studentId;
    }

    public LocalDate getDate() {
        return date; // Ensure this returns LocalDate
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAttendance(LocalDate date) {
        return attendanceMap.getOrDefault(date, new SimpleStringProperty("")).get();
    }

    public void setAttendance(LocalDate date, String attendance) {
        attendanceMap.computeIfAbsent(date, k -> new SimpleStringProperty()).set(attendance);
    }

    public StringProperty attendanceProperty(LocalDate date) {
        return attendanceMap.computeIfAbsent(date, k -> new SimpleStringProperty());
    }
}


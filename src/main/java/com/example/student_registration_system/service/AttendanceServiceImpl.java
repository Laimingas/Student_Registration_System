package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AttendanceRecord;
import com.example.student_registration_system.service.AttendanceService;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceServiceImpl implements AttendanceService {

    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();
    private static final String FILE_PATH = "attendance_data.txt";

    @Override
    public void markAttendance(String studentId, LocalDate date, boolean present) {
        attendanceRecords.add(new AttendanceRecord(studentId, date, present));
        saveAttendanceData();
    }

    @Override
    public List<AttendanceRecord> getAllAttendanceRecords() {
        return new ArrayList<>(attendanceRecords); // Return a copy to ensure immutability
    }

    // Logic to add an attendance record (for context)
    public void addAttendanceRecord(AttendanceRecord record) {
        attendanceRecords.add(record);
    }

    @Override
    public List<AttendanceRecord> getAttendanceByStudent(String studentId) {
        return attendanceRecords.stream()
                .filter(record -> record.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRecord> getAttendanceByGroup(String groupId) {
        // Implement logic for retrieving group attendance
        return new ArrayList<>();
    }

    @Override
    public List<LocalDate> getFilledDates() {
        return attendanceRecords.stream()
                .map(AttendanceRecord::getDate) // Ensure getDate() exists and is correct
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void saveAttendanceData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(attendanceRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadAttendanceData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            attendanceRecords = (List<AttendanceRecord>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
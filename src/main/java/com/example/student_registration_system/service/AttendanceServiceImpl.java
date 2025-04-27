package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AttendanceRecord;
import com.example.student_registration_system.service.AttendanceService;
import com.example.student_registration_system.model.Student;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AttendanceServiceImpl implements AttendanceService {

    private static AttendanceServiceImpl instance; // Singleton instance

    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();
    private static final String FILE_PATH = "attendance_data.txt";
    private GroupService groupService = new GroupServiceImpl();

    // Private constructor to prevent direct instantiation
    private AttendanceServiceImpl() {
        loadAttendanceData(); // Load data when the singleton is created
    }

    // Public method to get the singleton instance
    public static AttendanceServiceImpl getInstance() {
        if (instance == null) {
            instance = new AttendanceServiceImpl();
        }
        return instance;
    }

    @Override
    public List<LocalDate> getDatesInRange(List<AttendanceRecord> attendanceRecords, LocalDate startDate, LocalDate endDate) {
        return attendanceRecords.stream()
                .map(AttendanceRecord::getDate)
                .filter(date -> !date.isBefore(startDate) && !date.isAfter(endDate)) // Filter by the given range
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public void markAttendance(String studentId, LocalDate date, boolean present) {
        System.out.println("Marking attendance for Student ID: " + studentId + ", Date: " + date + ", Present: " + present);

        // Find if an attendance record already exists
        AttendanceRecord existingRecord = attendanceRecords.stream()
                .filter(record -> record.getStudentId().equals(studentId) && record.getDate().equals(date))
                .findFirst()
                .orElse(null);

        if (existingRecord != null) {
            // Update the existing record
            existingRecord.setPresent(present);
            System.out.println("Updated Attendance Record: " + existingRecord);
        } else {
            // Create a new record with the given present status
            AttendanceRecord newRecord = new AttendanceRecord(studentId, date, present);
            attendanceRecords.add(newRecord);
            System.out.println("Added Attendance Record: " + newRecord);
        }

        // Debug the current state of the list
        System.out.println("Current Attendance Records (" + attendanceRecords.size() + "):");
        attendanceRecords.forEach(record ->
                System.out.println("Student ID: " + record.getStudentId() +
                        ", Date: " + record.getDate() +
                        ", Present: " + record.isPresent())
        );

        // Save the data after marking attendance
        saveAttendanceData();
    }

    @Override
    public List<AttendanceRecord> getAllAttendanceRecords() {
        return attendanceRecords; // Return the shared list
    }

    @Override
    public List<AttendanceRecord> getAttendanceByStudent(String studentId) {
        return attendanceRecords.stream()
                .filter(record -> record.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRecord> getAttendanceByGroup(String groupId) {
        // Assuming you have a way to fetch the list of students in a group
        List<String> studentIdsInGroup = groupService.getGroupById(groupId).getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList());

        return attendanceRecords.stream()
                .filter(record -> studentIdsInGroup.contains(record.getStudentId()))
                .collect(Collectors.toList());
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
            System.out.println("Attendance data saved: " + attendanceRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadAttendanceData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            attendanceRecords = (List<AttendanceRecord>) ois.readObject();
            System.out.println("Attendance data loaded: " + attendanceRecords);
        } catch (IOException | ClassNotFoundException e) {
            attendanceRecords = new ArrayList<>();
            System.out.println("Failed to load attendance data. Initialized empty list.");
            e.printStackTrace();
        }
    }
}
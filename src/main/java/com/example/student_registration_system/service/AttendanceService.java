package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AttendanceRecord;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    void markAttendance(String studentId, LocalDate date, boolean present);
    List<AttendanceRecord> getAttendanceByStudent(String studentId);
    List<AttendanceRecord> getAttendanceByGroup(String groupId);
    List<LocalDate> getFilledDates();
    void saveAttendanceData();
    void loadAttendanceData();
    List<AttendanceRecord> getAllAttendanceRecords();
    public List<LocalDate> getDatesInRange(List<AttendanceRecord> attendanceRecords, LocalDate startDate, LocalDate endDate);
}



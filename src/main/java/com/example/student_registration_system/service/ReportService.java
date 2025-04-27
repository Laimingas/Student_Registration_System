package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AttendanceRecord;
import com.example.student_registration_system.model.Student;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<Student> getStudentsWithAttendance(LocalDate startDate, LocalDate endDate);
    void saveReportAsPDF(List<Student> students, List<AttendanceRecord> attendanceRecords, String filePath, String groupName, List<LocalDate> dates);
    List<AttendanceRecord> getAttendanceRecords(LocalDate startDate, LocalDate endDate);
    public List<AttendanceRecord> getAttendanceRecordsByGroup(String groupId, LocalDate startDate, LocalDate endDate);
}
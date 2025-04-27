package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AttendanceRecord;
import com.example.student_registration_system.model.Student;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<Student> getStudentsWithAttendance(LocalDate startDate, LocalDate endDate);
    void saveReportAsPDF(List<Student> students, List<AttendanceRecord> attendanceRecords, String filePath);
    List<AttendanceRecord> getAttendanceRecords(LocalDate startDate, LocalDate endDate);
}
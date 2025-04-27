package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AttendanceRecord;
import com.example.student_registration_system.model.Student;
import com.example.student_registration_system.service.ReportService;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.time.LocalDate;
import java.util.List;

public class ReportServiceImpl implements ReportService {

    private AttendanceService attendanceService = new AttendanceServiceImpl(); // Replace with your actual service
    private StudentService studentService = new StudentServiceImpl();

    @Override
    public List<Student> getStudentsWithAttendance(LocalDate startDate, LocalDate endDate) {
        // Retrieve all students
        List<Student> allStudents = studentService.getAllStudents();

        // Filter students with attendance records within the specified date range
        return allStudents.stream()
                .filter(student -> attendanceService.getAttendanceByStudent(student.getId()).stream()
                        .anyMatch(record -> !record.getDate().isBefore(startDate) && !record.getDate().isAfter(endDate)))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRecord> getAttendanceRecords(LocalDate startDate, LocalDate endDate) {
        return attendanceService.getAllAttendanceRecords().stream()
                .filter(record -> !record.getDate().isBefore(startDate) && !record.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public void saveReportAsPDF(List<Student> students, List<AttendanceRecord> attendanceRecords, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Attendance Report");
            contentStream.endText();

            // Add Table Header
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(50, 720);
            contentStream.showText("Student ID | Name | Attendance");
            contentStream.endText();

            // Add Attendance Data
            int yOffset = 700;
            for (Student student : students) {
                String studentId = student.getId();
                String studentName = student.getName();
                String attendance = attendanceRecords.stream()
                        .filter(record -> record.getStudentId().equals(studentId))
                        .map(record -> record.getAttendance(record.getDate())) // Adjust this logic if needed
                        .collect(Collectors.joining(", "));

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, yOffset);
                contentStream.showText(studentId + " | " + studentName + " | " + attendance);
                contentStream.endText();

                yOffset -= 20; // Move down for the next row
            }

            contentStream.close();
            document.save(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
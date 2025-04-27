package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AttendanceRecord;
import com.example.student_registration_system.model.Group;
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

    private AttendanceService attendanceService = AttendanceServiceImpl.getInstance(); // Replace with your actual service
    private StudentService studentService = new StudentServiceImpl();
    private GroupService groupService = new GroupServiceImpl();

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
        List<AttendanceRecord> records = attendanceService.getAllAttendanceRecords().stream()
                .filter(record -> {
                    boolean isInDateRange = !record.getDate().isBefore(startDate) && !record.getDate().isAfter(endDate);
                    System.out.println("Checking Record: " + record + ", isInDateRange: " + isInDateRange);
                    return isInDateRange;
                })
                .collect(Collectors.toList());
        System.out.println("Filtered Attendance Records: " + records);
        return records;
    }

    @Override
    public void saveReportAsPDF(List<Student> students, List<AttendanceRecord> attendanceRecords, String filePath, String groupName, List<LocalDate> selectedDates) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Title Section
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            int yOffset = 750; // Start from the top of the page
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yOffset);
            contentStream.showText("Attendance Report for Group: " + groupName);
            contentStream.endText();

            yOffset -= 20; // Move down for the next line
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yOffset);
            contentStream.showText("Generated on: " + LocalDate.now());
            contentStream.endText();

            // Generate the full list of dates in the range
            LocalDate startDate = selectedDates.get(0);
            LocalDate endDate = selectedDates.get(selectedDates.size() - 1);
            List<LocalDate> fullDateRange = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

            // Table Header
            yOffset -= 40; // Add spacing before the table
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Maximum columns per row before wrapping
            int maxColumnsPerRow = 4; // Adjust based on page width and column size
            int columnWidth = 100;

            for (Student student : students) {
                // Add Student ID
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yOffset);
                contentStream.showText("Student ID: " + student.getId());
                contentStream.endText();
                yOffset -= 20;

                // Add Attendance Data (Wrap Columns)
                int currentColumn = 0; // Track column index for wrapping
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yOffset);

                for (LocalDate date : fullDateRange) {
                    // Check attendance for the current student on the current date
                    boolean attended = attendanceRecords.stream()
                            .anyMatch(record -> record.getStudentId().equals(student.getId()) &&
                                    record.getDate().equals(date) &&
                                    record.isPresent());

                    // Add attendance data (X for present, blank for absent)
                    contentStream.showText(date.toString() + ": " + (attended ? "X" : "-") + "   ");
                    currentColumn++;

                    // Wrap to the next row if the maximum columns are reached
                    if (currentColumn >= maxColumnsPerRow) {
                        contentStream.endText();
                        yOffset -= 20; // Move to the next line
                        currentColumn = 0; // Reset column index

                        // Check if a new page is needed
                        if (yOffset < 50) {
                            contentStream.close();
                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            yOffset = 750; // Reset yOffset for the new page
                        }

                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yOffset);
                    }
                }

                contentStream.endText();
                yOffset -= 20; // Move down for the next student

                // Handle page overflow for rows
                if (yOffset < 50) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yOffset = 750; // Reset yOffset for the new page
                }
            }

            contentStream.close();
            document.save(filePath);
            System.out.println("PDF saved to: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractGroupNameFromFilePath(String filePath) {
        // Assuming the file path includes the group name, e.g., "AttendanceReport_Math101_2025-04-27.pdf"
        String[] parts = filePath.split("_");
        if (parts.length > 1) {
            return parts[1]; // Extract the second part (group name)
        }
        return "Unknown Group"; // Fallback if group name is not found
    }

    @Override
    public List<AttendanceRecord> getAttendanceRecordsByGroup(String groupId, LocalDate startDate, LocalDate endDate) {
        List<String> studentIdsInGroup = groupService.getStudentsInGroup(groupId).stream()
                .map(Student::getId)
                .collect(Collectors.toList());

        System.out.println("Student IDs in Group: " + studentIdsInGroup);

        List<AttendanceRecord> records = attendanceService.getAllAttendanceRecords().stream()
                .filter(record -> {
                    boolean isInGroup = studentIdsInGroup.contains(record.getStudentId());
                    boolean isInDateRange = !record.getDate().isBefore(startDate) && !record.getDate().isAfter(endDate);
                    System.out.println("Checking Record: " + record + ", isInGroup: " + isInGroup + ", isInDateRange: " + isInDateRange);
                    return isInGroup && isInDateRange;
                })
                .collect(Collectors.toList());

        System.out.println("Filtered Attendance Records for Group: " + records);
        return records;
    }
}
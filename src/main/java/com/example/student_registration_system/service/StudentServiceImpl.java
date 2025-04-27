package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AbstractPerson;
import com.example.student_registration_system.model.Student;
import com.example.student_registration_system.service.StudentService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl implements StudentService {

    private static final List<Student> students = new ArrayList<>();

    @Override
    public void addStudent(Student student) {
        students.add(student);
    }

    @Override
    public void updateStudent(Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(student.getId())) {
                students.set(i, student);
                return;
            }
        }
    }

    @Override
    public void deleteStudent(String studentId) {
        students.removeIf(student -> student.getId().equals(studentId));
    }

    @Override
    public Student getStudentById(String studentId) {
        return students.stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    @Override
    public void importStudentsFromCSV(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 3) { // Ensure valid data
                    Student student = new Student(fields[0], fields[1], fields[2]);
                    students.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importStudentsFromExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                String id = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                String email = row.getCell(2).getStringCellValue();
                students.add(new Student(id, name, email));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportStudentsToCSV(File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Student student : students) {
                bw.write(String.format("%s,%s,%s%n",
                        student.getId(),
                        student.getName(),
                        student.getEmail()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportStudentsToExcel(File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");
            int rowIndex = 0;

            // Header row
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Email");

            // Data rows
            for (Student student : students) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(student.getId());
                row.createCell(1).setCellValue(student.getName());
                row.createCell(2).setCellValue(student.getEmail());
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
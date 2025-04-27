package com.example.student_registration_system.file;

import com.example.student_registration_system.model.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelFileHandler extends AbstractFileHandler<Student> {

    @Override
    public List<Student> importData(String filePath) throws Exception {
        List<Student> students = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                String id = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                String email = row.getCell(2).getStringCellValue();
                students.add(new Student(id, name, email)); // No group
            }
        }
        return students;
    }

    @Override
    public void exportData(String filePath, List<Student> data) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");
            int rowIndex = 0;

            // Header row
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Email");

            // Data rows
            for (Student student : data) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(student.getId());
                row.createCell(1).setCellValue(student.getName());
                row.createCell(2).setCellValue(student.getEmail());
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }
}
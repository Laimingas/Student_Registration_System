package com.example.student_registration_system.file;

import com.example.student_registration_system.model.Student;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFileHandler extends AbstractFileHandler<Student> {

    @Override
    public List<Student> importData(String filePath) throws Exception {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                // Assuming CSV format: id,name,email
                if (fields.length >= 3) {
                    Student student = new Student(fields[0], fields[1], fields[2]); // No group
                    students.add(student);
                }
            }
        }
        return students;
    }

    @Override
    public void exportData(String filePath, List<Student> data) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Student student : data) {
                bw.write(String.format("%s,%s,%s%n",
                        student.getId(),
                        student.getName(),
                        student.getEmail())); // No group
            }
        }
    }
}
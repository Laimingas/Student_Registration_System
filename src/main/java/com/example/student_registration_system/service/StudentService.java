package com.example.student_registration_system.service;

import com.example.student_registration_system.model.Student;

import java.io.File;
import java.util.List;

public interface StudentService {

    void addStudent(Student student);

    void updateStudent(Student student);

    void deleteStudent(String studentId);

    Student getStudentById(String studentId);

    List<Student> getAllStudents();

    void importStudentsFromCSV(File file);

    void importStudentsFromExcel(File file);
}


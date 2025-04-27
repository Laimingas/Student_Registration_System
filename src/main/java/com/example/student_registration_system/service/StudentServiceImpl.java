package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AbstractPerson;
import com.example.student_registration_system.model.Student;
import com.example.student_registration_system.service.StudentService;

import java.io.File;
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
        // TODO: Implement CSV import logic
    }

    @Override
    public void importStudentsFromExcel(File file) {
        // TODO: Implement Excel import logic
    }
}
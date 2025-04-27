package com.example.student_registration_system.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String id;
    private String name;
    private List<Student> students;

    public Group(String id, String name) {
        this.id = id;
        this.name = name;
        this.students = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    @Override
    public String toString() {
        return name;
    }
}
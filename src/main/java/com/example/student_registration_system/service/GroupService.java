package com.example.student_registration_system.service;

import com.example.student_registration_system.model.Group;
import com.example.student_registration_system.model.Student;

import java.util.List;

public interface GroupService {
    void addGroup(Group group);
    void updateGroup(Group group);
    void deleteGroup(String groupId);
    Group getGroupById(String groupId);
    List<Group> getAllGroups();
    void addStudentToGroup(String studentId, String groupId);
    List<Student> getStudentsInGroup(String groupId);
}
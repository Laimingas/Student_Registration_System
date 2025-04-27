package com.example.student_registration_system.service;

import com.example.student_registration_system.model.Group;
import com.example.student_registration_system.model.Student;
import com.example.student_registration_system.service.GroupService;
import com.example.student_registration_system.service.StudentService;

import java.util.ArrayList;
import java.util.List;

public class GroupServiceImpl implements GroupService {

    private static List<Group> groups = new ArrayList<>();
    private StudentService studentService = new StudentServiceImpl();

    @Override
    public void addGroup(Group group) {
        groups.add(group);
    }

    @Override
    public void updateGroup(Group group) {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getId().equals(group.getId())) {
                groups.set(i, group);
                return;
            }
        }
    }

    @Override
    public void deleteGroup(String groupId) {
        groups.removeIf(group -> group.getId().equals(groupId));
    }

    @Override
    public Group getGroupById(String groupId) {
        return groups.stream().filter(group -> group.getId().equals(groupId)).findFirst().orElse(null);
    }

    @Override
    public List<Group> getAllGroups() {
        return groups;
    }

    @Override
    public void addStudentToGroup(String studentId, String groupId) {
        Group group = getGroupById(groupId);
        if (group != null) {
            Student student = (Student) studentService.getStudentById(studentId);
            if (student != null) {
                group.getStudents().add(student);
            }
        }
    }

    @Override
    public List<Student> getStudentsInGroup(String groupId) {
        Group group = getGroupById(groupId);
        if (group != null) {
            return group.getStudents(); // Return the list of students in the group
        }
        return new ArrayList<>(); // Return an empty list if the group is not found
    }
}
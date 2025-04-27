package com.example.student_registration_system.service;

import com.example.student_registration_system.model.AbstractPerson;

import java.util.List;

public interface PersonService {
    void addPerson(AbstractPerson person);
    void updatePerson(AbstractPerson person);
    void deletePerson(String personId);
    AbstractPerson getPersonById(String personId);
    List<AbstractPerson> getAllPersons();
}
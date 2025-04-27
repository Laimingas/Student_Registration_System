package com.example.student_registration_system.model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {

    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty email;

    public Student(String id, String name, String email) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void displayInfo() {
        System.out.println("Student Info: ID = " + getId() + ", Name = " + getName() + ", Email = " + getEmail());
    }

    public String getDetails() {
        return "ID: " + getId() + ", Name: " + getName() + ", Email: " + getEmail();
    }

    @Override
    public String toString() {
        return getId();
    }

}
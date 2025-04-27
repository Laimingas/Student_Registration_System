package com.example.student_registration_system.file;

import java.util.List;

public abstract class AbstractFileHandler<T> {
    // Abstract method to import data
    public abstract List<T> importData(String filePath) throws Exception;

    // Abstract method to export data
    public abstract void exportData(String filePath, List<T> data) throws Exception;
}
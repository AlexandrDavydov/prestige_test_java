package com.prestige.models;

import com.prestige.adapters.DbAdapter;

import java.util.ArrayList;

public class TestData {
    private final ArrayList<Student> students = new ArrayList<>();
    private final DbAdapter dbAdapter = new DbAdapter();

    public TestData() {
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void deleteTestData(){
        for (Student student : students) {
            dbAdapter.deleteStudentByFullName(
                student.getLastName(),
                student.getFirstName(),
                student.getMiddleName());
        }
    }
}
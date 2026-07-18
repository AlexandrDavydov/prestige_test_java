package com.prestige.models;

import com.prestige.adapters.DbAdapter;

import java.util.ArrayList;

public class TestData {
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<Coach> coaches = new ArrayList<>();
    private final ArrayList<Card> cards = new ArrayList<>();

    private final DbAdapter dbAdapter = new DbAdapter();

    public TestData() {
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void addCoach(Coach coach) {
        this.coaches.add(coach);
    }

    public void deleteTestData(){
        for (Student student : students) {
            dbAdapter.deleteStudentByFullName(
                student.getLastName(),
                student.getFirstName(),
                student.getMiddleName());
        }
        for (Coach coach : coaches) {
            dbAdapter.deleteCoachByFullName(
                    coach.getLastName(),
                    coach.getFirstName(),
                    coach.getMiddleName());
        }
        for (Card card : cards) {
            dbAdapter.deleteCardByName(card.getName());
        }
    }
}
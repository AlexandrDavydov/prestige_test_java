package com.prestige.models;

import com.prestige.adapters.DbAdapter;

import java.util.ArrayList;

public class TestData {
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<Coach> coaches = new ArrayList<>();
    private final ArrayList<Card> cards = new ArrayList<>();
    private final ArrayList<LessonTemplate> lessonTemplates = new ArrayList<>();
    private final ArrayList<Lesson> lessons = new ArrayList<>();

    private final DbAdapter dbAdapter;

    public TestData(DbAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void addLessonTemplate(LessonTemplate lessonTemplate) {
        this.lessonTemplates.add(lessonTemplate);
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void addCoach(Coach coach) {
        this.coaches.add(coach);
    }

//TODO If entity has id delete it by id
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

        for (LessonTemplate lessonTemplate : lessonTemplates) {
            dbAdapter.deleteCoachById(lessonTemplate.getCoachId());
            dbAdapter.deleteStudents(lessonTemplate.getStudentsIds());
            dbAdapter.deleteLessonTemplateByName(lessonTemplate.getTemplateName());
        }

        for (Lesson lesson : lessons) {
            dbAdapter.deleteCoachById(lesson.getCoachId());
            dbAdapter.deleteStudents(lesson.getStudentIds());
            dbAdapter.deleteLessonByName(lesson.getLessonName());
        }
    }

}
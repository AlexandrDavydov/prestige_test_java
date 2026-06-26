package com.prestige.models;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private Long id;
    private String date;
    private String lessonName;
    private Long coachId;
    private String coachName;
    private String status;
    private List<Student> students;
    private String studentIds;

    public Lesson() {
        this.students = new ArrayList<>();
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(String studentIds) {
        this.studentIds = studentIds;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void addStudentId(String studentId) {
        if (this.studentIds == null || this.studentIds.isEmpty()) {
            this.studentIds = studentId;
        } else {
            this.studentIds += "," + studentId;
        }
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", lessonName='" + lessonName + '\'' +
                ", coachName='" + coachName + '\'' +
                ", status='" + status + '\'' +
                ", studentsCount=" + (students != null ? students.size() : 0) +
                '}';
    }
}
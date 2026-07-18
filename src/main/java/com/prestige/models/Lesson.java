package com.prestige.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private Long id;
    private String date;
    private String lessonName;
    private Long coachId;
    private String coachName;
    private String status;
    @Builder.Default
    private List<Student> students = new ArrayList<>();
    private String studentIds;

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
}

package com.prestige.utils;

import com.prestige.adapters.DbAdapter;
import com.prestige.models.Coach;
import com.prestige.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseLessonFactory {

    protected static DbAdapter createDbAdapter() {
        return DbAdapter.getInstance();
    }

    protected static String getStudents(int count) {
        DbAdapter dbAdapter = createDbAdapter();
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Student student = StudentFactory.createRandomStudent();
            student.setId(dbAdapter.addStudent(student));
            students.add(student);
        }
        return students.stream()
                .map(s -> String.valueOf(s.getId()))
                .collect(Collectors.joining(","));
    }

    protected static int getCoachId() {
        DbAdapter dbAdapter = createDbAdapter();
        Coach coach = CoachFactory.createRandomCoach();
        coach.setId(dbAdapter.addCoach(coach));
        return coach.getId();
    }

    protected static int getClearCoachId() {
        DbAdapter dbAdapter = createDbAdapter();
        Coach coach = CoachFactory.createRandomCoach();
        coach.setLessonsPaid(0);
        coach.setLessonsCount(0);
        coach.setId(dbAdapter.addCoach(coach));
        return coach.getId();
    }
}

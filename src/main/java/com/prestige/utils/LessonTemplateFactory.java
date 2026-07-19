package com.prestige.utils;

import com.github.javafaker.Faker;
import com.prestige.adapters.DbAdapter;
import com.prestige.models.Coach;
import com.prestige.models.LessonTemplate;
import com.prestige.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LessonTemplateFactory {
    private static final Faker faker = new Faker(new Locale("ru", "RU"));

    public static LessonTemplate createRandomTemplate() {
        String templateName = TestingUtils.capitalizeFirst(faker.lorem().characters(10));
        return LessonTemplate.builder()
                .templateName(templateName)
                .studentsIds(getStudents(faker.random().nextInt(4,15)))
                .coachId(getCoachId())
                .build();
    }
    private static String getStudents(int count){
        DbAdapter dbAdapter = new DbAdapter();
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

    private static int getCoachId(){
        DbAdapter dbAdapter = new DbAdapter();
        Coach coach = CoachFactory.createRandomCoach();
        coach.setId(dbAdapter.addCoach(coach));
        return coach.getId();
    }
}

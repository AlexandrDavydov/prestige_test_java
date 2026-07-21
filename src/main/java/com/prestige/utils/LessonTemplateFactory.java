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

public class LessonTemplateFactory extends BaseLessonFactory {
    private static final Faker faker = new Faker(new Locale("ru", "RU"));

    public static LessonTemplate createRandomLessonTemplate() {
        String templateName = TestingUtils.capitalizeFirst(faker.lorem().characters(10));
        return LessonTemplate.builder()
                .templateName(templateName)
                .studentsIds(getStudents(faker.random().nextInt(4,15)))
                .coachId(getCoachId())
                .build();
    }
}

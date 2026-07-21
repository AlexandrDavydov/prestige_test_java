package com.prestige.utils;

import com.github.javafaker.Faker;
import com.prestige.models.Lesson;
import com.prestige.models.LessonStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LessonFactory extends BaseLessonFactory {
    private static final Faker faker = new Faker(new Locale("ru", "RU"));

    public static Lesson createRandomLesson() {
        String templateName = TestingUtils.capitalizeFirst(faker.lorem().characters(10));
        return Lesson.builder()
                .lessonName(templateName)
                .date(getFormattedDate(faker.date().birthday(0, 1)))
                .coachId(getCoachId())
                .status(LessonStatus.PLANNED)
                .studentIds(getStudents(faker.random().nextInt(4, 15)))
                .build();
    }

    private static String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}

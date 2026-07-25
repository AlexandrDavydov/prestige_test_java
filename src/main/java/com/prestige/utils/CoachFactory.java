package com.prestige.utils;

import com.github.javafaker.Faker;
import com.prestige.models.Coach;
import org.dooger1.russiandatagenerator.factory.RussianDataFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CoachFactory {
    private static final Faker faker = new Faker(new Locale("ru", "RU"));

    public static Coach createRandomCoach() {
        int lessonsCount = faker.number().numberBetween(1, 30);
        int lessonsPaid = faker.number().numberBetween(10, lessonsCount);

        String[] fio = RussianDataFactory.generateFullName().split(" ");

        Coach coach = new Coach();
        coach.setLastName(fio[0]);
        coach.setFirstName(fio[1]);
        coach.setMiddleName(fio[2]);
        coach.setContacts(faker.phoneNumber().phoneNumber());
        coach.setBirthday(getFormattedBirthday(faker.date().birthday(6, 50)));
        coach.setLessonsCount(lessonsCount);
        coach.setLessonsPaid(lessonsPaid);
        coach.setStudentPayment((int) (Math.random() * 4500 + 500));
        coach.setAdditionalInfo("Дополнительная информация "+faker.lorem().characters(10));
        return coach;
    }

    public static Coach createCoachWithCustomName(String lastName, String firstName, String middleName) {
        Coach coach = new Coach();
        coach.setLastName(lastName);
        coach.setFirstName(firstName);
        coach.setMiddleName(middleName);
        coach.setContacts(faker.phoneNumber().phoneNumber());
        coach.setBirthday(getFormattedBirthday(faker.date().birthday(6, 50)));
        coach.setLessonsCount((int) (Math.random() * 30) + 1);
        coach.setLessonsPaid((int) (Math.random() * 20));
        coach.setStudentPayment((int) (Math.random() * 4500 + 500));
        return coach;
    }

    public static Coach createCoachWithCustomData(
            String lastName,
            String firstName,
            String middleName,
            String contacts,
            String birthday,
            int lessonsCount,
            int lessonsPaid,
            int studentPayment) {
        Coach coach = new Coach();
        coach.setLastName(lastName);
        coach.setFirstName(firstName);
        coach.setMiddleName(middleName);
        coach.setContacts(contacts);
        coach.setBirthday(birthday);
        coach.setLessonsCount(lessonsCount);
        coach.setLessonsPaid(lessonsPaid);
        coach.setStudentPayment(studentPayment);
        return coach;
    }

    public static String getFormattedBirthday(Date birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(birthday);
    }
}

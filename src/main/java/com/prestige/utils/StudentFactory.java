package com.prestige.utils;

import com.github.javafaker.Faker;
import com.prestige.models.Student;
import org.dooger1.russiandatagenerator.factory.RussianDataFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StudentFactory {
    private static final Faker faker = new Faker(new Locale("ru", "RU"));

    public static Student createRandomStudent() {
        String[] fio = RussianDataFactory.generateFullName().split(" ");
        return Student.builder()
                .lastName(fio[0])
                .firstName(fio[1])
                .middleName(fio[2])
                .contacts(faker.phoneNumber().phoneNumber())
                .birthday(getFormattedBirthday(faker.date().birthday(6, 50)))
                .lessonsCount((int) (Math.random() * 20) + 1)
                .additionalInfo("доп. инфо "+faker.random().nextInt(10))
                .build();
    }

    public static Student createStudentWithCustomName(String lastName, String firstName, String middleName) {
        return Student.builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .contacts(faker.phoneNumber().phoneNumber())
                .birthday(getFormattedBirthday(faker.date().birthday(6, 50)))
                .lessonsCount((int) (Math.random() * 20) + 1)
                .build();
    }

    public static Student createStudentWithCustomData(
            String lastName,
            String firstName,
            String middleName,
            String contacts,
            String birthday,
            int lessonsCount) {
        return Student.builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .contacts(contacts)
                .birthday(birthday)
                .lessonsCount(lessonsCount)
                .build();
    }

    public static String getFormattedBirthday(Date birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(birthday);
    }


}
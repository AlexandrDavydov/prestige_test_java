package com.prestige.utils;

import com.github.javafaker.Faker;
import com.prestige.models.Student;
import org.dooger1.russiandatagenerator.factory.RussianDataFactory;

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
                .birthday(faker.date().birthday(6, 50).toString())
                .lessonsCount((int) (Math.random() * 20) + 1)
                .build();
    }

    public static Student createStudentWithCustomName(String lastName, String firstName, String middleName) {
        return Student.builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .contacts(faker.phoneNumber().phoneNumber())
                .birthday(faker.date().birthday(6, 50).toString())
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
}
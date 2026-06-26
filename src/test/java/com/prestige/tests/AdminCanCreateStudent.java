package com.prestige.tests;

import com.github.javafaker.Faker;
import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.models.User;
import com.prestige.pages.AddStudentPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.LoginPage;
import com.prestige.pages.StudentsPage;
import org.dooger1.russiandatagenerator.factory.RussianDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Random;

import static dk.brics.automaton.StringUnionOperations.build;

@DisplayName("")
class StudentTest extends BaseTest {

    @Test
    @DisplayName("Админ может создать студента")
    void adminCanCreateStudent() {
        // Arrange
        User admin = User.admin();
        String[] fio = RussianDataFactory.generateFullName().split(" ");
        Faker faker = new Faker(new Locale("ru", "RU"));
        Student student = Student.builder()
                .lastName(fio[0])
                .firstName(fio[1])
                .middleName(fio[2])
                .contacts(faker.phoneNumber().phoneNumber())
                .birthday(faker.date().birthday(6, 50).toString())
                .lessonsCount((int) (Math.random() * 20) + 1)
                .build();

        // Act
        LoginPage loginPage = new LoginPage(page)
                .navigateTo();

        DashboardPage dashboard = loginPage
                .typeUsername(admin.getUsername())
                .typePassword(admin.getPassword())
                .clickLoginAndGoToDashboard();

        StudentsPage studentsPage = dashboard.goToStudents();
        studentsPage.waitForPageLoad();
        AddStudentPage addStudentPage = studentsPage.clickAddStudent();
        addStudentPage.waitForPageLoad();
        addStudentPage.submitStudent(student);
        studentsPage.isStudentExists(student.getFullName());

    }
}
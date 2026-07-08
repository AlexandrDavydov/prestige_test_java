package com.prestige.tests;

import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.models.User;
import com.prestige.pages.*;
import com.prestige.utils.StudentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateStudentTest extends BaseTest {
    Student studentData;

    @Test
    public void run() {
        uiTestFragments.login();
        createStudent(studentData);
        uiTestFragments.checkStudentExists(studentData);
    }

    @BeforeEach
    void beforeTest() {
        studentData = StudentFactory.createRandomStudent();
        testData.addStudent(studentData);
    }

    public void createStudent(Student studentData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        StudentsPage studentsPage = dashboardPage.goToStudents();
        studentsPage.waitForPageLoad();
        AddStudentPage addStudentPage = studentsPage.clickAddStudent();
        addStudentPage.waitForPageLoad();
        addStudentPage.submitStudent(studentData);
    }
}
package com.prestige.tests;

import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.pages.*;
import com.prestige.utils.StudentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateStudentTest extends BaseTest {
    Student studentData;

    @Test
    public void test_01_CreateStudent() {
        uiTestFragments.login();
        createStudentWithUi(studentData);
        uiTestFragments.checkStudentExists(studentData, true);
    }

    @BeforeEach
    void beforeTest() {
        studentData = StudentFactory.createRandomStudent();
        testData.addStudent(studentData);
    }

    public void createStudentWithUi(Student studentData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        StudentsPage studentsPage = dashboardPage.goToStudents();
        studentsPage.waitForPageLoad();
        AddStudentPage addStudentPage = studentsPage.clickAddStudent();
        addStudentPage.waitForPageLoad();
        addStudentPage.submitStudent(studentData);
    }
}
package com.prestige.tests;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.StudentsPage;
import com.prestige.utils.StudentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeletetudentTest extends BaseTest {
    Student createdStudentData;
    Student editStudentData;

    @Test
    public void run() {
        uiTestFragments.login();
        deleteStudent(createdStudentData);
        uiTestFragments.checkStudentExists(editStudentData, false);
    }

    @BeforeEach
    void beforeTest() {
        createdStudentData = StudentFactory.createRandomStudent();
        editStudentData = StudentFactory.createRandomStudent();
        new DbAdapter().addStudent(createdStudentData);
        testData.addStudent(createdStudentData);
        testData.addStudent(editStudentData);
    }

    public void deleteStudent(Student studentData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        StudentsPage studentsPage = dashboardPage.goToStudents();
        studentsPage.waitForPageLoad();
        studentsPage.deleteStudent(studentData.getFullName());
    }
}
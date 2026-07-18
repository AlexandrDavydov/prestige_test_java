package com.prestige.tests;

import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.pages.AddStudentPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.StudentsPage;
import com.prestige.utils.StudentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.LOCK_STUDENT;
import static com.prestige.tests.TestGroups.STUDENT;
@ResourceLock(LOCK_STUDENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_01_CreateStudentTest extends BaseTest {
    Student studentData;

    @Test
    @Tag(STUDENT)
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
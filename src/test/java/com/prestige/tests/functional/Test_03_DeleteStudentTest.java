package com.prestige.tests.functional;

import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.StudentsPage;
import com.prestige.utils.StudentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.prestige.tests.TestGroups.STUDENT;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_03_DeleteStudentTest extends BaseTest {
    Student createdStudentData;

    @Test
    @Tag(STUDENT)
    public void test_03_DeleteStudent() {
        uiTestFragments.login();
        deleteCreatedStudentWithUi(createdStudentData);
        uiTestFragments.checkStudentExists(createdStudentData, false);
    }

    @BeforeEach
    void beforeTest() {
        createStudentInTheDatabase();
    }

    public void deleteCreatedStudentWithUi(Student studentData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        StudentsPage studentsPage = dashboardPage.goToStudents();
        studentsPage.waitForPageLoad();
        studentsPage.deleteStudent(studentData.getFullName());
    }

    public void createStudentInTheDatabase() {
        createdStudentData = StudentFactory.createRandomStudent();
        createdStudentData.setId(dbAdapter.addStudent(createdStudentData));
        testData.addStudent(createdStudentData);
    }
}
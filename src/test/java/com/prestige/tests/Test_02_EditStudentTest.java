package com.prestige.tests;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.EditStudentPage;
import com.prestige.pages.StudentsPage;
import com.prestige.utils.StudentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.LOCK_STUDENT;
import static com.prestige.tests.TestGroups.STUDENT;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_02_EditStudentTest extends BaseTest {
    Student createdStudentData;
    Student editStudentData;

    @Test
    @Tag(STUDENT)
    @ResourceLock(LOCK_STUDENT)
    public void test_02_EditStudent() {
        uiTestFragments.login();
        editStudent(createdStudentData, editStudentData);
        uiTestFragments.checkStudentExists(editStudentData, true);
    }

    @BeforeEach
    void beforeTest() {
        createdStudentData = StudentFactory.createRandomStudent();
        editStudentData = StudentFactory.createRandomStudent();
        new DbAdapter().addStudent(createdStudentData);
        testData.addStudent(createdStudentData);
        testData.addStudent(editStudentData);
    }

    public void editStudent(Student studentForEditData, Student newStudentData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        StudentsPage studentsPage = dashboardPage.goToStudents();
        studentsPage.waitForPageLoad();
        EditStudentPage editStudentPage = studentsPage.clickEditStudent(studentForEditData.getFullName());
        editStudentPage.waitForPageLoad();
        editStudentPage.fillStudentForm(newStudentData);
        editStudentPage.clickSubmit();
    }
}
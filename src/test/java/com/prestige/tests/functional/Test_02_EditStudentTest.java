package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.EditStudentPage;
import com.prestige.pages.StudentsPage;
import com.prestige.utils.StudentFactory;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.LOCK_STUDENT;
import static com.prestige.tests.TestGroups.STUDENT;
import static com.prestige.utils.StepHelper.step;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_02_EditStudentTest extends BaseTest {
    Student createdStudentData;
    Student editStudentData;

    @Test
    @Tag(STUDENT)
    public void test_02_EditStudent() {
        uiTestFragments.login();
        editStudentWithUi(createdStudentData, editStudentData);
        uiTestFragments.checkStudentExists(editStudentData, true);
    }

    @BeforeEach
    void beforeTest() {
        generateStudentData();
        createStudentInTheDatabase();
    }

    public void editStudentWithUi(Student studentForEditData, Student newStudentData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        StudentsPage studentsPage = dashboardPage.goToStudents();
        studentsPage.waitForPageLoad();
        EditStudentPage editStudentPage = studentsPage.clickEditStudent(studentForEditData.getFullName());
        editStudentPage.waitForPageLoad();
        editStudentPage.fillStudentForm(newStudentData);
        editStudentPage.clickSubmit();
    }

    public void createStudentInTheDatabase(){
        step("Создать ученика в базе данных для редактирования", () -> {
            createdStudentData = StudentFactory.createRandomStudent();
            createdStudentData.setId(dbAdapter.addStudent(createdStudentData));
            testData.addStudent(createdStudentData);
        });
    }

    public void generateStudentData(){
        step("Сгенерировать данные ученика для редактирования", () -> {
            editStudentData = StudentFactory.createRandomStudent();
            testData.addStudent(editStudentData);
        });
    }
}
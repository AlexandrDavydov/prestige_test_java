package com.prestige.tests;

import com.microsoft.playwright.Page;
import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.models.User;
import com.prestige.pages.AddStudentPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.PlaywrightLoginPage;
import com.prestige.pages.StudentsPage;

public class UiTestFragments extends BaseTest {
    private final Page page;
    public UiTestFragments(Page page) {
        this.page = page;
    }

    public void login() {
        User admin = User.admin();
        PlaywrightLoginPage playwrightLoginPage = new PlaywrightLoginPage(page)
                .navigateTo();
        playwrightLoginPage
                .typeUsername(admin.getUsername())
                .typePassword(admin.getPassword())
                .clickLoginAndGoToDashboard();
    }

    public void createStudent(Student studentData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        StudentsPage studentsPage = dashboardPage.goToStudents();
        studentsPage.waitForPageLoad();
        AddStudentPage addStudentPage = studentsPage.clickAddStudent();
        addStudentPage.waitForPageLoad();
        addStudentPage.submitStudent(studentData);
    }

    public void checkStudentExists(Student studentData) {
        StudentsPage studentsPage =  new StudentsPage(page);
        studentsPage.isStudentExists(studentData.getFullName());
    }
}

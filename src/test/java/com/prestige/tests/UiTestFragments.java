package com.prestige.tests;

import com.microsoft.playwright.Page;
import com.prestige.base.BaseTest;
import com.prestige.models.Coach;
import com.prestige.models.Student;
import com.prestige.models.User;
import com.prestige.pages.CoachesPage;
import com.prestige.pages.PlaywrightLoginPage;
import com.prestige.pages.StudentsPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    public void checkStudentExists(Student studentData, boolean exists) {
        StudentsPage studentsPage =  new StudentsPage(page);
        assertEquals(studentsPage.isStudentExists(studentData.getFullName()), exists);
    }

    public void checkCoachExists(Coach coachData, boolean exists) {
        CoachesPage coachesPage =  new CoachesPage(page);
        assertEquals(coachesPage.isCoachExists(coachData.getFullName()), exists);
    }
}

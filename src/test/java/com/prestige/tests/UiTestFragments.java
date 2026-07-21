package com.prestige.tests;

import com.microsoft.playwright.Page;
import com.prestige.base.BaseTest;
import com.prestige.models.*;
import com.prestige.pages.*;

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

    public void checkLessonTemplateExists(LessonTemplate lessonTemplate, boolean exists) {
        LessonTemplatesPage lessonTemplatesPage =  new LessonTemplatesPage(page);
        assertEquals(exists, lessonTemplatesPage.isLessonTemplateExists(lessonTemplate));
    }

    public void checkLessonExists(Lesson lesson, boolean exists) {
        LessonsPage lessonsPage =  new LessonsPage(page);
        assertEquals(exists, lessonsPage.isLessonExists(lesson));
    }

    public void checkCoachExists(Coach coachData, boolean exists) {
        CoachesPage coachesPage =  new CoachesPage(page);
        assertEquals(coachesPage.isCoachExists(coachData.getFullName()), exists);
    }

    public void checkCardExists(Card cardData, boolean exists) {
        CardsPage cardsPage =  new CardsPage(page);
        cardsPage.waitForPageLoad();
        assertEquals(cardsPage.isCardExists(cardData), exists);
    }
}

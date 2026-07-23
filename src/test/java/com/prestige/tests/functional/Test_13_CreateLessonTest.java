package com.prestige.tests.functional;

import com.prestige.base.BaseTest;
import com.prestige.models.Lesson;
import com.prestige.pages.*;
import com.prestige.utils.LessonFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_13_CreateLessonTest extends BaseTest {
    Lesson lessonData;


    @Test
    @Tag(LESSON)
    public void test_13_CreateLesson() {
        uiTestFragments.login();
        createLessonWithUi(lessonData);
        uiTestFragments.checkLessonExists(lessonData, true);
    }

    @BeforeEach
    void beforeTest() {
        lessonData = LessonFactory.createRandomLesson();
        testData.addLesson(lessonData);
    }

    public void createLessonWithUi(Lesson lessonData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        LessonsPage lessonsPage = dashboardPage.goToLessons();
        lessonsPage.waitForPageLoad();
        AddLessonPage addLessonPage = lessonsPage.clickAddLesson();
        addLessonPage.waitForPageLoad();
        addLessonPage.fillLessonForm(lessonData);
        addLessonPage.clickSave();
    }
}
package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Lesson;
import com.prestige.pages.AddLessonPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.EditLessonPage;
import com.prestige.pages.LessonsPage;
import com.prestige.utils.LessonFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.LESSON_TEMPLATE;
import static com.prestige.tests.TestGroups.LOCK_LESSON_TEMPLATE;

@ResourceLock(LOCK_LESSON_TEMPLATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_14_EditLessonTest extends BaseTest {
    Lesson createdLessonData;
    Lesson lessonDataForEdit;


    @Test
    @Tag(LESSON_TEMPLATE)
    public void test_14_EditLessonTest() {
        uiTestFragments.login();
        EditLessonWithUi(lessonDataForEdit);
        uiTestFragments.checkLessonExists(lessonDataForEdit, true);
    }

    @BeforeEach
    void beforeTest() {
        createdLessonData = LessonFactory.createRandomLesson();
        lessonDataForEdit = LessonFactory.createRandomLesson();
        createdLessonData.setId(new DbAdapter().addLesson(createdLessonData));
        testData.addLesson(createdLessonData);
        testData.addLesson(lessonDataForEdit);
    }

    public void EditLessonWithUi(Lesson lessonData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        LessonsPage lessonsPage = dashboardPage.goToLessons();
        lessonsPage.waitForPageLoad();
        EditLessonPage editLessonPage = lessonsPage.clickEditLesson(createdLessonData.getLessonName());
        editLessonPage.waitForPageLoad();
        editLessonPage.fillLessonForm(lessonData);
        editLessonPage.clickSave();
    }
}
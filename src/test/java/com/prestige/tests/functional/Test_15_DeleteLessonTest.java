package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Lesson;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.LessonsPage;
import com.prestige.utils.LessonFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_15_DeleteLessonTest extends BaseTest {
    Lesson createdLessonData;

    @Test
    @Tag(LESSON)
    public void test_15_DeleteLesson() {
        uiTestFragments.login();
        deleteLessonWithUi(createdLessonData);
        uiTestFragments.checkLessonExists(createdLessonData, false);
    }

    @BeforeEach
    void beforeTest() {
        createdLessonData = LessonFactory.createRandomLesson();
        createdLessonData.setId(new DbAdapter().addLesson(createdLessonData));
        testData.addLesson(createdLessonData);
    }

    public void deleteLessonWithUi(Lesson LessonData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        LessonsPage lessonsPage = dashboardPage.goToLessons();
        lessonsPage.waitForPageLoad();
        lessonsPage.deleteLesson(LessonData.getLessonName());
    }
}

package com.prestige.tests.functional;

import com.prestige.base.BaseTest;
import com.prestige.models.Coach;
import com.prestige.models.Lesson;
import com.prestige.pages.CoachesPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.LessonsPage;
import com.prestige.utils.LessonFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.models.LessonStatus.HAPPENED;
import static com.prestige.tests.TestGroups.*;

@ResourceLock(LOCK_LESSON_COACH)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_17_CoachConductsTheLesson extends BaseTest {
    Lesson lessonData;

    @Test
    @Tag(LESSON)
    public void test_17_CoachConductsTheLesson() {
        uiTestFragments.login();
        conductTheLesson(lessonData);
        uiTestFragments.checkLessonExists(lessonData, false);
        switchFilterTo(HAPPENED);
        uiTestFragments.checkLessonExists(lessonData, true);
        checkPaymentToCoach();
    }

    @BeforeEach
    void beforeTest() {
        lessonData = LessonFactory.createRandomLessonWithClearCoach();
        lessonData.setId(dbAdapter.addLesson(lessonData));
        testData.addLesson(lessonData);
    }

    public void conductTheLesson(Lesson lessonData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        LessonsPage lessonsPage = dashboardPage.goToLessons();
        lessonsPage.waitForPageLoad();
        lessonsPage.markLessonAsDone(lessonData.getLessonName());
    }

    public void switchFilterTo(String filterName){
        LessonsPage lessonsPage = new LessonsPage(page);
        lessonsPage.filterByStatus(filterName);
    }
    public void checkPaymentToCoach(){
        LessonsPage lessonsPage = new LessonsPage(page);
        CoachesPage coachesPage = lessonsPage.goToCoaches();
        coachesPage.waitForPageLoad();
        Coach coach = dbAdapter.findCoachById(lessonData.getCoachId());
        coachesPage.checkPaymentsAreAccrued(coach.getFullName(), lessonData.getStudentsCount());
    }
}
package com.prestige.tests;

import com.prestige.base.BaseTest;
import com.prestige.models.Coach;
import com.prestige.pages.AddCoachPage;
import com.prestige.pages.CoachesPage;
import com.prestige.pages.DashboardPage;
import com.prestige.utils.CoachFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.COACH;
import static com.prestige.tests.TestGroups.LOCK_COACH;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_04_CreateCoachTest extends BaseTest {
    Coach coachData;

    @Test
    @Tag(COACH)
    @ResourceLock(LOCK_COACH)
    public void test_04_CreateCoach() {
        uiTestFragments.login();
        createCoachWithUi(coachData);
        uiTestFragments.checkCoachExists(coachData, true);
    }

    @BeforeEach
    void beforeTest() {
        coachData = CoachFactory.createRandomCoach();
        testData.addCoach(coachData);
    }

    public void createCoachWithUi(Coach coachData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        CoachesPage coachesPage = dashboardPage.goToCoaches();
        coachesPage.waitForPageLoad();
        AddCoachPage addCoachPage = coachesPage.clickAddCoach();
        addCoachPage.waitForPageLoad();
        addCoachPage.submitCoach(coachData);
    }
}
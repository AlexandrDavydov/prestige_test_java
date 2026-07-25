package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Coach;
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
class Test_06_DeleteCoachTest extends BaseTest {
    Coach createdCoachData;

    @Test
    @Tag(COACH)
    public void test_06_DeleteCoach() {
        uiTestFragments.login();
        deleteCoachWithUi(createdCoachData);
        uiTestFragments.checkCoachExists(createdCoachData, false);
    }

    @BeforeEach
    void beforeTest() {
        createCoachInDatabase();
    }

    public void deleteCoachWithUi(Coach coachData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        CoachesPage coachesPage = dashboardPage.goToCoaches();
        coachesPage.waitForPageLoad();
        coachesPage.deleteCoach(coachData.getFullName());
    }

    public void createCoachInDatabase(){
        createdCoachData = CoachFactory.createRandomCoach();
        createdCoachData.setId(dbAdapter.addCoach(createdCoachData));
        testData.addCoach(createdCoachData);
    }
}

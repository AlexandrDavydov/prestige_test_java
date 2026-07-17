package com.prestige.tests;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Coach;
import com.prestige.pages.*;
import com.prestige.utils.CoachFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_05_EditCoachTest extends BaseTest {
    Coach createdCoachData;
    Coach editCoachData;

    @Test
    public void test_05_EditCoach() {
        uiTestFragments.login();
        editCoach(createdCoachData, editCoachData);
        uiTestFragments.checkCoachExists(editCoachData, true);
    }

    @BeforeEach
    void beforeTest() {
        createdCoachData = CoachFactory.createRandomCoach();
        editCoachData = CoachFactory.createRandomCoach();
        new DbAdapter().addCoach(createdCoachData);
        testData.addCoach(createdCoachData);
        testData.addCoach(editCoachData);
    }

    public void editCoach(Coach coachForEditData, Coach newCoachData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        CoachesPage coachesPage = dashboardPage.goToCoaches();
        coachesPage.waitForPageLoad();
        EditCoachPage editCoachPage = coachesPage.clickEditCoach(coachForEditData.getFullName());
        editCoachPage.waitForPageLoad();
        editCoachPage.fillCoachForm(newCoachData);
        editCoachPage.clickSave();
    }
}

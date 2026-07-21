package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Coach;
import com.prestige.pages.CoachesPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.EditCoachPage;
import com.prestige.utils.CoachFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.COACH;
import static com.prestige.tests.TestGroups.LOCK_COACH;

@ResourceLock(LOCK_COACH)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_05_EditCoachTest extends BaseTest {
    Coach createdCoachData;
    Coach editCoachData;

    @Test
    @Tag(COACH)
    public void test_05_EditCoach() {
        uiTestFragments.login();
        editCoachWithUi(createdCoachData, editCoachData);
        uiTestFragments.checkCoachExists(editCoachData, true);
    }

    @BeforeEach
    void beforeTest() {
        createdCoachData = CoachFactory.createRandomCoach();
        editCoachData = CoachFactory.createRandomCoach();
        createdCoachData.setId(new DbAdapter().addCoach(createdCoachData));
        testData.addCoach(createdCoachData);
        testData.addCoach(editCoachData);
    }

    public void editCoachWithUi(Coach coachForEditData, Coach newCoachData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        CoachesPage coachesPage = dashboardPage.goToCoaches();
        coachesPage.waitForPageLoad();
        EditCoachPage editCoachPage = coachesPage.clickEditCoach(coachForEditData.getFullName());
        editCoachPage.waitForPageLoad();
        editCoachPage.fillCoachForm(newCoachData);
        editCoachPage.clickSave();
    }
}

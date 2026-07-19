package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Card;
import com.prestige.models.LessonTemplate;
import com.prestige.pages.CardsPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.LessonTemplatesPage;
import com.prestige.utils.CardFactory;
import com.prestige.utils.LessonTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.CARD;
import static com.prestige.tests.TestGroups.LOCK_CARD;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ResourceLock(LOCK_CARD)
class Test_12_DeleteLessonTemplateTest extends BaseTest {
    LessonTemplate createdLessonTemplateData;

    Card createdCardData;
    Card editCardData;

    @Test
    @Tag(CARD)
    public void test_12_DeleteLessonTemplateTest() {
        uiTestFragments.login();
        deleteLessonTemplateWithUi(createdLessonTemplateData);
        uiTestFragments.checkLessonTemplateExists(createdLessonTemplateData, false);
    }

    @BeforeEach
    void beforeTest() {
        createdLessonTemplateData = LessonTemplateFactory.createRandomLessonTemplate();
        createdLessonTemplateData.setId(new DbAdapter().addLessonTemplate(createdLessonTemplateData));
    }

    public void deleteLessonTemplateWithUi(LessonTemplate LessonTemplateData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        LessonTemplatesPage lessonTemplatePage = dashboardPage.goToLessonTemplates();
        lessonTemplatePage.waitForPageLoad();
        lessonTemplatePage.deleteTemplate(LessonTemplateData.getTemplateName());
    }
}

package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.LessonTemplate;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.LessonTemplatesPage;
import com.prestige.utils.LessonTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.LESSON_TEMPLATE;
import static com.prestige.tests.TestGroups.LOCK_LESSON_TEMPLATE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_12_DeleteLessonTemplateTest extends BaseTest {
    LessonTemplate createdLessonTemplateData;

    @Test
    @Tag(LESSON_TEMPLATE)
    public void test_12_DeleteLessonTemplate() {
        uiTestFragments.login();
        deleteLessonTemplateWithUi(createdLessonTemplateData);
        uiTestFragments.checkLessonTemplateExists(createdLessonTemplateData, false);
    }

    @BeforeEach
    void beforeTest() {
        generateLessonTemplateData();
    }

    public void deleteLessonTemplateWithUi(LessonTemplate LessonTemplateData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        LessonTemplatesPage lessonTemplatePage = dashboardPage.goToLessonTemplates();
        lessonTemplatePage.waitForPageLoad();
        lessonTemplatePage.deleteTemplate(LessonTemplateData.getTemplateName());
    }

    public void generateLessonTemplateData() {
        createdLessonTemplateData = LessonTemplateFactory.createRandomLessonTemplate();
        createdLessonTemplateData.setId(dbAdapter.addLessonTemplate(createdLessonTemplateData));
        testData.addLessonTemplate(createdLessonTemplateData);
    }
}

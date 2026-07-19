package com.prestige.tests.functional;

import com.prestige.base.BaseTest;
import com.prestige.models.LessonTemplate;
import com.prestige.pages.*;
import com.prestige.utils.LessonTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.*;

@ResourceLock(LOCK_LESSON_TEMPLATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_10_CreateLessonTemplateTest extends BaseTest {
    LessonTemplate lessonTemplateData;

    @Test
    @Tag(LESSON_TEMPLATE)
    public void test_10_CreateLessonTemplate() {
        uiTestFragments.login();
        createLessonTemplateWithUi(lessonTemplateData);
        uiTestFragments.checkLessonTemplateExists(lessonTemplateData, true);
    }

    @BeforeEach
    void beforeTest() {
        lessonTemplateData = LessonTemplateFactory.createRandomLessonTemplate();
        testData.addLessonTemplate(lessonTemplateData);
    }

    public void createLessonTemplateWithUi(LessonTemplate lessonTemplateData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        LessonTemplatesPage lessonTemplatePage = dashboardPage.goToLessonTemplates();
        lessonTemplatePage.waitForPageLoad();
        AddLessonTemplatePage addLessonTemplatePage = lessonTemplatePage.clickAddLessonTemplate();
        addLessonTemplatePage.waitForPageLoad();
        addLessonTemplatePage.fillForm(lessonTemplateData);
        addLessonTemplatePage.submitForm();
    }
}
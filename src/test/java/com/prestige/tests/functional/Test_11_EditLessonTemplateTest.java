package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.LessonTemplate;
import com.prestige.pages.AddLessonTemplatePage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.EditLessonTemplatePage;
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
class Test_11_EditLessonTemplateTest extends BaseTest {
    LessonTemplate createdLessonTemplateData;
    LessonTemplate LessonTemplateDataForEdit;

    @Test
    @Tag(LESSON_TEMPLATE)
    public void test_11_EditLessonTemplate() {
        uiTestFragments.login();
        editLessonTemplateWithUi(createdLessonTemplateData, LessonTemplateDataForEdit);
        uiTestFragments.checkLessonTemplateExists(LessonTemplateDataForEdit, true);
    }

    @BeforeEach
    void beforeTest() {
        createdLessonTemplateData = LessonTemplateFactory.createRandomLessonTemplate();
        LessonTemplateDataForEdit = LessonTemplateFactory.createRandomLessonTemplate();
        createdLessonTemplateData.setId(new DbAdapter().addLessonTemplate(createdLessonTemplateData));
        testData.addLessonTemplate(createdLessonTemplateData);
        testData.addLessonTemplate(LessonTemplateDataForEdit);
    }

    public void editLessonTemplateWithUi(LessonTemplate lessonTemplateData, LessonTemplate lessonTemplateDataForEdit) {
        DashboardPage dashboardPage = new DashboardPage(page);
        LessonTemplatesPage lessonTemplatePage = dashboardPage.goToLessonTemplates();
        lessonTemplatePage.waitForPageLoad();
        EditLessonTemplatePage editLessonTemplatePage = lessonTemplatePage.clickEditTemplate(lessonTemplateData.getTemplateName());
        editLessonTemplatePage.waitForPageLoad();
        editLessonTemplatePage.fillForm(lessonTemplateDataForEdit);
        editLessonTemplatePage.submitForm();
    }
}
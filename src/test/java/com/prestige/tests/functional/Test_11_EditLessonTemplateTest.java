package com.prestige.tests.functional;

import com.prestige.base.BaseTest;
import com.prestige.models.LessonTemplate;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.EditLessonTemplatePage;
import com.prestige.pages.LessonTemplatesPage;
import com.prestige.utils.LessonTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.prestige.tests.TestGroups.LESSON_TEMPLATE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_11_EditLessonTemplateTest extends BaseTest {
    LessonTemplate createdLessonTemplateData;
    LessonTemplate lessonTemplateDataForEdit;

    @Test
    @Tag(LESSON_TEMPLATE)
    public void test_11_EditLessonTemplate() {
        uiTestFragments.login();
        editLessonTemplateWithUi(createdLessonTemplateData, lessonTemplateDataForEdit);
        uiTestFragments.checkLessonTemplateExists(lessonTemplateDataForEdit, true);
    }

    @BeforeEach
    void beforeTest() {
        setCreatedLessonTemplateInTheDatabase();
        generateLessonTemplateData();
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

    public void setCreatedLessonTemplateInTheDatabase(){
        createdLessonTemplateData = LessonTemplateFactory.createRandomLessonTemplate();
        createdLessonTemplateData.setId(dbAdapter.addLessonTemplate(createdLessonTemplateData));
        testData.addLessonTemplate(createdLessonTemplateData);
    }

    public void generateLessonTemplateData(){
        lessonTemplateDataForEdit = LessonTemplateFactory.createRandomLessonTemplate();
        testData.addLessonTemplate(lessonTemplateDataForEdit);
    }
}
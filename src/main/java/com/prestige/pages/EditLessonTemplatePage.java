package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;

public class EditLessonTemplatePage extends BaseLessonTemplatePage<EditLessonTemplatePage> {

    public EditLessonTemplatePage(Page page) {
        super(page);
    }

    public LessonTemplatesPage submitForm() {
        page.click(submitButton);
        waitForPageLoad();
        return new LessonTemplatesPage(page);
    }

    public LessonTemplatesPage fillAndSubmit(LessonTemplate lessonTemplate) {
        fillForm(lessonTemplate);
        return submitForm();
    }
}

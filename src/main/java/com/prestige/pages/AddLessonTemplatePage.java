package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;

public class AddLessonTemplatePage extends BaseLessonTemplatePage<AddLessonTemplatePage> {

    public AddLessonTemplatePage(Page page) {
        super(page);
    }

    public LessonTemplatesPage submitForm() {
        page.click(submitButton);
        waitForPageLoad();
        return new LessonTemplatesPage(page);
    }
}

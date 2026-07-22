package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;
import io.qameta.allure.Step;

public class AddLessonTemplatePage extends BaseLessonTemplatePage<AddLessonTemplatePage> {

    public AddLessonTemplatePage(Page page) {
        super(page);
    }

    @Step("Сохранить шаблон занятия")
    public LessonTemplatesPage submitForm() {
        page.click(submitButton);
        waitForPageLoad();
        return new LessonTemplatesPage(page);
    }
}

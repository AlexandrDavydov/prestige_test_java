package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;
import io.qameta.allure.Step;

public class EditLessonTemplatePage extends BaseLessonTemplatePage<EditLessonTemplatePage> {

    public EditLessonTemplatePage(Page page) {
        super(page);
    }

    @Step("Сохранить изменения шаблона")
    public LessonTemplatesPage submitForm() {
        page.click(submitButton);
        waitForPageLoad();
        return new LessonTemplatesPage(page);
    }

    @Step("Заполнить и сохранить шаблон")
    public LessonTemplatesPage fillAndSubmit(LessonTemplate lessonTemplate) {
        fillForm(lessonTemplate);
        return submitForm();
    }
}

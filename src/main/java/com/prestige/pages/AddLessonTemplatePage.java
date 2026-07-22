package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;
import static com.prestige.utils.StepHelper.step;

public class AddLessonTemplatePage extends BaseLessonTemplatePage<AddLessonTemplatePage> {

    public AddLessonTemplatePage(Page page) {
        super(page);
    }

    public LessonTemplatesPage submitForm() {
        return step("Сохранить шаблон занятия", () -> {
            page.click(submitButton);
            waitForPageLoad();
            return new LessonTemplatesPage(page);
        });
    }
}

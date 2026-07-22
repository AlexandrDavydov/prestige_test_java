package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;
import static com.prestige.utils.StepHelper.step;

public class EditLessonTemplatePage extends BaseLessonTemplatePage<EditLessonTemplatePage> {

    public EditLessonTemplatePage(Page page) {
        super(page);
    }

    public LessonTemplatesPage submitForm() {
        return step("Сохранить изменения шаблона", () -> {
            page.click(submitButton);
            waitForPageLoad();
            return new LessonTemplatesPage(page);
        });
    }

    public LessonTemplatesPage fillAndSubmit(LessonTemplate lessonTemplate) {
        return step("Заполнить и сохранить шаблон", () -> {
            fillForm(lessonTemplate);
            return submitForm();
        });
    }
}

package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;
import io.qameta.allure.Step;

public abstract class BaseLessonTemplatePage<T extends BaseLessonTemplatePage<T>> extends BasePage {

    protected final String templateNameInput = "input[name='template_name']";
    protected final String coachSelect = "select[name='coach_id']";
    protected final String studentCheckboxes = "input[name='student_ids']";
    protected final String submitButton = "button[type='submit']";
    protected final String cancelButton = "a[href*='lesson_templates']";
    protected final String pageTitle = "h1";

    public BaseLessonTemplatePage(Page page) {
        super(page);
    }

    @SuppressWarnings("unchecked")
    @Step("Выбрать тренера в шаблоне: {coachIndex}")
    public T selectCoach(int coachIndex) {
        page.selectOption(coachSelect, String.valueOf(coachIndex));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Выбрать учеников в шаблоне: {studentIds}")
    public T selectStudents(String studentIds) {
        page.locator(studentCheckboxes).evaluateAll(
            "els => els.forEach(el => el.checked = false)");
        String[] ids = studentIds.split(",");
        for (String id : ids) {
            page.locator(studentCheckboxes + "[value='" + id.trim() + "']").check();
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Заполнить название шаблона: {templateName}")
    public T fillTemplateName(String templateName) {
        page.fill(templateNameInput, templateName);
        return (T) this;
    }

    public String getTemplateName() {
        return page.inputValue(templateNameInput);
    }

    @SuppressWarnings("unchecked")
    @Step("Заполнить форму шаблона занятия")
    public T fillForm(LessonTemplate lessonTemplate) {
        fillTemplateName(lessonTemplate.getTemplateName());
        selectCoach(lessonTemplate.getCoachId());
        selectStudents(lessonTemplate.getStudentsIds());
        return (T) this;
    }

    public boolean isSaveButtonVisible() {
        return page.isVisible(submitButton);
    }

    public boolean isCancelButtonVisible() {
        return page.isVisible(cancelButton);
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }
}

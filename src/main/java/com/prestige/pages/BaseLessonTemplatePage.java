package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;

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
    public T selectCoach(int coachIndex) {
        page.selectOption(coachSelect, String.valueOf(coachIndex));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
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
    public T fillTemplateName(String templateName) {
        page.fill(templateNameInput, templateName);
        return (T) this;
    }

    public String getTemplateName() {
        return page.inputValue(templateNameInput);
    }

    @SuppressWarnings("unchecked")
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

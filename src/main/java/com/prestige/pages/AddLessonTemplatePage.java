package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;

public class AddLessonTemplatePage extends BasePage {

    private final String templateNameInput = "input[name='template_name']";
    private final String coachSelect = "select[name='coach_id']";
    private final String studentCheckboxes = "input[name='student_ids']";
    private final String submitButton = "button[type='submit']";

    public AddLessonTemplatePage(Page page) {
        super(page);
    }
    public void fillForm(LessonTemplate lessonTemplate) {
        page.fill(templateNameInput, lessonTemplate.getTemplateName());
        selectCoach(lessonTemplate.getCoachId());
        selectStudents(lessonTemplate.getStudentsIds());
        submitForm();
    }

    public void selectCoach(int coachIndex) {
        page.selectOption(coachSelect, String.valueOf(coachIndex));
    }

    public void selectStudents(String studentIds) {
        String[] ids = studentIds.split(",");
        for (String id : ids) {
            page.locator(studentCheckboxes + "[value='" + id.trim() + "']").check();
        }
    }

    public LessonTemplatesPage submitForm() {
        page.click(submitButton);
        waitForPageLoad();
        return new LessonTemplatesPage(page);
    }
}

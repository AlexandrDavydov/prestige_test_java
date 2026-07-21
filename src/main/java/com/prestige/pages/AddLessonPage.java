package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.Lesson;

import java.util.ArrayList;
import java.util.List;

public class AddLessonPage extends BasePage {

    private final String pageTitle = "h1";
    private final String dateInput = "input[name='date']";
    private final String lessonNameInput = "input[name='lesson_name']";
    private final String coachSelect = "select[name='coach_id']";
    private final String studentCheckboxes = "input[name='student_ids']";
    private final String statusSelect = "select[name='status']";
    private final String submitButton = "button[type='submit']";
    private final String cancelButton = "a[href*='lessons']";

    private final String errorMessages = ".error-message";
    private final String fieldErrors = ".field-error";
    private final String requiredAttribute = "required";
    private final String oninputAttribute = "oninput";

    public AddLessonPage(Page page) {
        super(page);
    }

    public AddLessonPage navigateTo() {
        page.navigate("/add_lesson");
        waitForPageLoad();
        return this;
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    public AddLessonPage fillDate(String date) {
        page.fill(dateInput, date);
        return this;
    }

    public String getDate() {
        return page.inputValue(dateInput);
    }

    public AddLessonPage fillLessonName(String lessonName) {
        page.fill(lessonNameInput, lessonName);
        return this;
    }

    public AddLessonPage fillLessonNameWithCapitalize(String lessonName) {
        page.fill(lessonNameInput, lessonName);
        page.evaluate("document.querySelector('input[name=\"lesson_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    public String getLessonName() {
        return page.inputValue(lessonNameInput);
    }

    public AddLessonPage selectCoach(int coachId) {
        page.selectOption(coachSelect, String.valueOf(coachId));
        return this;
    }

    public String getSelectedCoachId() {
        return page.locator(coachSelect).getAttribute("value");
    }

    public AddLessonPage selectStudents(String studentIds) {
        page.locator(studentCheckboxes).evaluateAll(
                "els => els.forEach(el => el.checked = false)");
        if (studentIds != null && !studentIds.isEmpty()) {
            String[] ids = studentIds.split(",");
            for (String id : ids) {
                page.locator(studentCheckboxes + "[value='" + id.trim() + "']").check();
            }
        }
        return this;
    }

    public AddLessonPage selectStudentById(int studentId) {
        page.locator(studentCheckboxes + "[value='" + studentId + "']").check();
        return this;
    }

    public List<String> getSelectedStudentIds() {
        return page.locator(studentCheckboxes + ":checked").all().stream()
                .map(locator -> locator.getAttribute("value"))
                .toList();
    }

    public AddLessonPage selectStatus(String status) {
        page.selectOption(statusSelect, status);
        return this;
    }

    public String getSelectedStatus() {
        return page.locator(statusSelect).getAttribute("value");
    }

    public AddLessonPage fillLessonForm(Lesson lesson) {
        if (lesson.getDate() != null) {
            fillDate(lesson.getDate());
        }
        if (lesson.getLessonName() != null) {
            fillLessonName(lesson.getLessonName());
        }
        selectCoach(lesson.getCoachId());
        if (lesson.getStudentIds() != null) {
            selectStudents(lesson.getStudentIds());
        }
        if (lesson.getStatus() != null) {
            selectStatus(lesson.getStatus());
        }
        return this;
    }

    public Lesson getLessonFromForm() {
        Lesson lesson = new Lesson();
        lesson.setDate(getDate());
        lesson.setLessonName(getLessonName());
        String coachId = getSelectedCoachId();
        if (coachId != null && !coachId.isEmpty()) {
            lesson.setCoachId(Integer.parseInt(coachId));
        }
        lesson.setStudentIds(String.join(",", getSelectedStudentIds()));
        lesson.setStatus(getSelectedStatus());
        return lesson;
    }

    public LessonsPage clickSave() {
        page.click(submitButton);
        waitForPageLoad();
        return new LessonsPage(page);
    }

    public AddLessonPage clickSaveAndStay() {
        page.click(submitButton);
        waitForPageLoad();
        return this;
    }

    public LessonsPage clickCancel() {
        page.click(cancelButton);
        waitForPageLoad();
        return new LessonsPage(page);
    }

    public LessonsPage submitLesson(Lesson lesson) {
        fillLessonForm(lesson);
        return clickSave();
    }

    public AddLessonPage clearField(String fieldName) {
        page.fill(getFieldSelector(fieldName), "");
        return this;
    }

    public AddLessonPage clearAllFields() {
        page.fill(dateInput, "");
        page.fill(lessonNameInput, "");
        page.fill(coachSelect, "");
        page.fill(statusSelect, "");
        return this;
    }

    public boolean areAllFieldsVisible() {
        return page.isVisible(dateInput) &&
                page.isVisible(lessonNameInput) &&
                page.isVisible(coachSelect) &&
                page.isVisible(studentCheckboxes) &&
                page.isVisible(statusSelect);
    }

    public boolean isFieldRequired(String fieldName) {
        return page.getAttribute(getFieldSelector(fieldName), requiredAttribute) != null;
    }

    public boolean areRequiredFieldsMarked() {
        return isFieldRequired("date") && isFieldRequired("lesson_name");
    }

    public boolean hasCapitalizationAttribute() {
        String oninput = page.getAttribute(lessonNameInput, oninputAttribute);
        return oninput != null && oninput.contains("capitalizeFirst");
    }

    public boolean hasErrorMessages() {
        return page.locator(errorMessages).count() > 0 ||
                page.locator(fieldErrors).count() > 0;
    }

    public List<String> getErrorMessages() {
        List<String> errors = new ArrayList<>();
        errors.addAll(page.locator(errorMessages).allTextContents());
        errors.addAll(page.locator(fieldErrors).allTextContents());
        return errors;
    }

    public String getFieldError(String fieldName) {
        String selector = ".field-error[data-field='" + fieldName + "']";
        if (page.isVisible(selector)) {
            return page.textContent(selector);
        }
        return null;
    }

    public boolean isFormValid() {
        return !hasErrorMessages() &&
                isFieldFilled("date") &&
                isFieldFilled("lesson_name");
    }

    public boolean isFieldFilled(String fieldName) {
        String value = page.inputValue(getFieldSelector(fieldName));
        return value != null && !value.isEmpty();
    }

    public boolean isSaveButtonVisible() {
        return page.isVisible(submitButton);
    }

    public boolean isCancelButtonVisible() {
        return page.isVisible(cancelButton);
    }

    public boolean isUrlCorrect() {
        return page.url().contains("/add_lesson");
    }

    private String getFieldSelector(String fieldName) {
        switch (fieldName) {
            case "date": return dateInput;
            case "lesson_name": return lessonNameInput;
            case "coach_id": return coachSelect;
            case "student_ids": return studentCheckboxes;
            case "status": return statusSelect;
            default: throw new IllegalArgumentException("Неизвестное поле: " + fieldName);
        }
    }
}

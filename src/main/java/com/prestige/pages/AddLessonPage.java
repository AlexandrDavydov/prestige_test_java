package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.Lesson;
import io.qameta.allure.Step;

public class AddLessonPage extends BaseLessonPage<AddLessonPage> {

    private final String requiredAttribute = "required";
    private final String oninputAttribute = "oninput";

    public AddLessonPage(Page page) {
        super(page);
    }

    @Step("Переход на страницу добавления занятия")
    public AddLessonPage navigateTo() {
        page.navigate("/add_lesson");
        waitForPageLoad();
        return this;
    }

    public AddLessonPage fillLessonNameWithCapitalize(String lessonName) {
        page.fill(lessonNameInput, lessonName);
        page.evaluate("document.querySelector('input[name=\"lesson_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    @Step("Выбрать ученика по ID: {studentId}")
    public AddLessonPage selectStudentById(int studentId) {
        page.locator(studentCheckboxes + "[value='" + studentId + "']").check();
        return this;
    }

    @Step("Отправить форму занятия")
    public LessonsPage submitLesson(Lesson lesson) {
        fillLessonForm(lesson);
        return clickSave();
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

    public boolean isFormValid() {
        return !hasErrorMessages() &&
                isFieldFilled("date") &&
                isFieldFilled("lesson_name");
    }

    @Override
    public boolean isUrlCorrect() {
        return page.url().contains("/add_lesson");
    }

    // make getFieldSelector accessible from this package via the parent
    // by keeping it package-private in the parent
}

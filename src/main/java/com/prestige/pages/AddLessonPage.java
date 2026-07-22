package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.Lesson;
import static com.prestige.utils.StepHelper.step;

public class AddLessonPage extends BaseLessonPage<AddLessonPage> {

    private final String requiredAttribute = "required";
    private final String oninputAttribute = "oninput";

    public AddLessonPage(Page page) {
        super(page);
    }

    public AddLessonPage navigateTo() {
        return step("Переход на страницу добавления занятия", () -> {
            page.navigate("/add_lesson");
            waitForPageLoad();
            return this;
        });
    }

    public AddLessonPage fillLessonNameWithCapitalize(String lessonName) {
        page.fill(lessonNameInput, lessonName);
        page.evaluate("document.querySelector('input[name=\"lesson_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    public AddLessonPage selectStudentById(int studentId) {
        return step("Выбрать ученика по ID: " + studentId, () -> {
            page.locator(studentCheckboxes + "[value='" + studentId + "']").check();
            return this;
        });
    }

    public LessonsPage submitLesson(Lesson lesson) {
        return step("Отправить форму занятия", () -> {
            fillLessonForm(lesson);
            return clickSave();
        });
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
}

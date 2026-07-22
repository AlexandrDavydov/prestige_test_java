package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.Lesson;
import static com.prestige.utils.StepHelper.step;

import java.util.List;

public abstract class BaseLessonPage<T extends BaseLessonPage<T>> extends BasePage {

    protected final String pageTitle = "h1";
    protected final String dateInput = "input[name='date']";
    protected final String lessonNameInput = "input[name='lesson_name']";
    protected final String coachSelect = "select[name='coach_id']";
    protected final String studentCheckboxes = "input[name='student_ids']";
    protected final String statusSelect = "select[name='status']";
    protected final String submitButton = "button[type='submit']";
    protected final String cancelButton = "a[href*='lessons']";

    protected final String errorMessages = ".error-message";
    protected final String fieldErrors = ".field-error";

    public BaseLessonPage(Page page) {
        super(page);
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    @SuppressWarnings("unchecked")
    public T fillDate(String date) {
        return step("Заполнить дату занятия: " + date, () -> {
            page.fill(dateInput, date);
            return (T) this;
        });
    }

    public String getDate() {
        return page.inputValue(dateInput);
    }

    @SuppressWarnings("unchecked")
    public T fillLessonName(String lessonName) {
        return step("Заполнить название занятия: " + lessonName, () -> {
            page.fill(lessonNameInput, lessonName);
            return (T) this;
        });
    }

    public String getLessonName() {
        return page.inputValue(lessonNameInput);
    }

    @SuppressWarnings("unchecked")
    public T selectCoach(int coachId) {
        return step("Выбрать тренера: " + coachId, () -> {
            page.selectOption(coachSelect, String.valueOf(coachId));
            return (T) this;
        });
    }

    public String getSelectedCoachId() {
        return page.locator(coachSelect).getAttribute("value");
    }

    @SuppressWarnings("unchecked")
    public T selectStudents(String studentIds) {
        return step("Выбрать учеников: " + studentIds, () -> {
            page.locator(studentCheckboxes).evaluateAll(
                    "els => els.forEach(el => el.checked = false)");
            if (studentIds != null && !studentIds.isEmpty()) {
                String[] ids = studentIds.split(",");
                for (String id : ids) {
                    page.locator(studentCheckboxes + "[value='" + id.trim() + "']").check();
                }
            }
            return (T) this;
        });
    }

    public List<String> getSelectedStudentIds() {
        return page.locator(studentCheckboxes + ":checked").all().stream()
                .map(locator -> locator.getAttribute("value"))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public T selectStatus(String status) {
        return step("Выбрать статус: " + status, () -> {
            page.selectOption(statusSelect, status);
            return (T) this;
        });
    }

    public String getSelectedStatus() {
        return page.locator(statusSelect).getAttribute("value");
    }

    @SuppressWarnings("unchecked")
    public T fillLessonForm(Lesson lesson) {
        return step("Заполнить форму занятия", () -> {
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
            return (T) this;
        });
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
        return step("Сохранить занятие", () -> {
            page.click(submitButton);
            waitForPageLoad();
            return new LessonsPage(page);
        });
    }

    @SuppressWarnings("unchecked")
    public T clickSaveAndStay() {
        return step("Сохранить и остаться на странице", () -> {
            page.click(submitButton);
            waitForPageLoad();
            return (T) this;
        });
    }

    public LessonsPage clickCancel() {
        return step("Отменить", () -> {
            page.click(cancelButton);
            waitForPageLoad();
            return new LessonsPage(page);
        });
    }

    @SuppressWarnings("unchecked")
    public T clearField(String fieldName) {
        return step("Очистить поле: " + fieldName, () -> {
            page.fill(getFieldSelector(fieldName), "");
            return (T) this;
        });
    }

    @SuppressWarnings("unchecked")
    public T clearAllFields() {
        return step("Очистить все поля", () -> {
            page.fill(dateInput, "");
            page.fill(lessonNameInput, "");
            page.fill(coachSelect, "");
            page.fill(statusSelect, "");
            return (T) this;
        });
    }

    public boolean isFieldFilled(String fieldName) {
        String value = page.inputValue(getFieldSelector(fieldName));
        return value != null && !value.isEmpty();
    }

    public boolean areAllFieldsVisible() {
        return page.isVisible(dateInput) &&
                page.isVisible(lessonNameInput) &&
                page.isVisible(coachSelect) &&
                page.isVisible(studentCheckboxes) &&
                page.isVisible(statusSelect);
    }

    public boolean hasErrorMessages() {
        return page.locator(errorMessages).count() > 0 ||
                page.locator(fieldErrors).count() > 0;
    }

    public List<String> getErrorMessages() {
        return page.locator(errorMessages).allTextContents();
    }

    public String getFieldError(String fieldName) {
        String selector = ".field-error[data-field='" + fieldName + "']";
        if (page.isVisible(selector)) {
            return page.textContent(selector);
        }
        return null;
    }

    public boolean isSaveButtonVisible() {
        return page.isVisible(submitButton);
    }

    public boolean isCancelButtonVisible() {
        return page.isVisible(cancelButton);
    }

    public abstract boolean isUrlCorrect();

    protected String getFieldSelector(String fieldName) {
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

package com.prestige.pages;

import com.microsoft.playwright.Page;
import static com.prestige.utils.StepHelper.step;

public abstract class BaseCoachPage<T extends BaseCoachPage<T>> extends BasePage {

    protected final String lastNameInput = "input[name='last_name']";
    protected final String firstNameInput = "input[name='first_name']";
    protected final String middleNameInput = "input[name='middle_name']";
    protected final String contactsInput = "input[name='contacts']";
    protected final String birthdayInput = "input[name='birthday']";
    protected final String lessonsCountInput = "input[name='lessons_count']";
    protected final String lessonsPaidInput = "input[name='lessons_paid']";
    protected final String studentPaymentInput = "input[name='student_payment']";
    protected final String additionalInfoInput = "input[name='additional_info']";
    protected final String submitButton = "button[type='submit']";
    protected final String cancelButton = "a[href*='coaches']";
    protected final String pageTitle = "h1";

    public BaseCoachPage(Page page) {
        super(page);
    }

    @SuppressWarnings("unchecked")
    public T fillLastName(String lastName) {
        return step("Заполнить фамилию тренера: {lastName}", () -> {
            page.fill(lastNameInput, lastName);
            return (T) this;
        });
    }

    public String getLastName() {
        return page.inputValue(lastNameInput);
    }

    @SuppressWarnings("unchecked")
    public T fillFirstName(String firstName) {
        return step("Заполнить имя тренера: {firstName}", () -> {
            page.fill(firstNameInput, firstName);
            return (T) this;
        });
    }

    public String getFirstName() {
        return page.inputValue(firstNameInput);
    }

    @SuppressWarnings("unchecked")
    public T fillMiddleName(String middleName) {
        return step("Заполнить отчество тренера: {middleName}", () -> {
            page.fill(middleNameInput, middleName);
            return (T) this;
        });
    }

    public String getMiddleName() {
        return page.inputValue(middleNameInput);
    }

    @SuppressWarnings("unchecked")
    public T fillContacts(String contacts) {
        return step("Заполнить контакты тренера: {contacts}", () -> {
            page.fill(contactsInput, contacts);
            return (T) this;
        });
    }

    public String getContacts() {
        return page.inputValue(contactsInput);
    }

    @SuppressWarnings("unchecked")
    public T fillBirthday(String birthday) {
        return step("Заполнить дату рождения тренера: {birthday}", () -> {
            page.fill(birthdayInput, birthday);
            return (T) this;
        });
    }

    public String getBirthday() {
        return page.inputValue(birthdayInput);
    }

    @SuppressWarnings("unchecked")
    public T fillLessonsCount(int count) {
        step("Заполнить количество занятий: {count}", () -> {
            page.fill(lessonsCountInput, String.valueOf(count));
        });
        return (T) this;
    }

    public int getLessonsCount() {
        String value = page.inputValue(lessonsCountInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public T fillLessonsPaid(int paid) {
        step("Заполнить оплаченных занятий: {paid}", () -> {
            page.fill(lessonsPaidInput, String.valueOf(paid));
        });
        return (T) this;
    }

    public int getLessonsPaid() {
        String value = page.inputValue(lessonsPaidInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public T fillStudentPayment(double payment) {
        step("Заполнить оплату за ученика: {payment}", () -> {
            page.fill(studentPaymentInput, String.valueOf(payment));
        });
        return (T) this;
    }

    public int getStudentPayment() {
        String value = page.inputValue(studentPaymentInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public T fillAdditionalInfo(String info) {
        step("Заполнить доп. информацию тренера", () -> {
            page.fill(additionalInfoInput, info);
        });
        return (T) this;
    }

    public String getAdditionalInfo() {
        return page.inputValue(additionalInfoInput);
    }

    @SuppressWarnings("unchecked")
    public T clearAllFields() {
        return step("Очистить все поля тренера", () -> {
            page.fill(lastNameInput, "");
            page.fill(firstNameInput, "");
            page.fill(middleNameInput, "");
            page.fill(contactsInput, "");
            page.fill(birthdayInput, "");
            page.fill(lessonsCountInput, "");
            page.fill(lessonsPaidInput, "");
            page.fill(studentPaymentInput, "");
            page.fill(additionalInfoInput, "");
            return (T) this;
        });
    }

    protected String getFieldSelector(String fieldName) {
        switch (fieldName) {
            case "last_name": return lastNameInput;
            case "first_name": return firstNameInput;
            case "middle_name": return middleNameInput;
            case "contacts": return contactsInput;
            case "birthday": return birthdayInput;
            case "lessons_count": return lessonsCountInput;
            case "lessons_paid": return lessonsPaidInput;
            case "student_payment": return studentPaymentInput;
            case "additional_info": return additionalInfoInput;
            default: throw new IllegalArgumentException("Неизвестное поле: " + fieldName);
        }
    }

    public boolean isFieldEditable(String fieldName) {
        return page.isEditable(getFieldSelector(fieldName));
    }

    public boolean isFieldReadonly(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, "readonly") != null ||
                page.getAttribute(selector, "disabled") != null;
    }

    public String getFieldPlaceholder(String fieldName) {
        return page.getAttribute(getFieldSelector(fieldName), "placeholder");
    }

    public String getFieldType(String fieldName) {
        return page.getAttribute(getFieldSelector(fieldName), "type");
    }

    public boolean isFieldFilled(String fieldName) {
        String selector = getFieldSelector(fieldName);
        String value = page.inputValue(selector);
        return value != null && !value.isEmpty();
    }

    public boolean isSaveButtonVisible() {
        return page.isVisible(submitButton);
    }

    public String getSubmitButtonText() {
        return page.textContent(submitButton);
    }

    public boolean isCancelButtonVisible() {
        return page.isVisible(cancelButton);
    }

    public String getCancelButtonText() {
        return page.textContent(cancelButton);
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }
}

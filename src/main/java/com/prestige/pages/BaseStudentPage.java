package com.prestige.pages;

import com.microsoft.playwright.Page;

import static com.prestige.utils.StepHelper.step;

public abstract class BaseStudentPage<T extends BaseStudentPage<T>> extends BasePage {

    protected final String lastNameInput = "input[name='last_name']";
    protected final String firstNameInput = "input[name='first_name']";
    protected final String middleNameInput = "input[name='middle_name']";
    protected final String contactsInput = "input[name='contacts']";
    protected final String birthdayInput = "input[name='birthday']";
    protected final String lessonsCountInput = "input[name='lessons_count']";
    protected final String additionalInfoInput = "input[name='additional_info']";
    protected final String submitButton = "button[type='submit']";
    protected final String pageTitle = "h1";

    public BaseStudentPage(Page page) {
        super(page);
    }

    @SuppressWarnings("unchecked")
    public T fillLastName(String lastName) {
        return step("Заполнить фамилию: {lastName}", () -> {
            page.fill(lastNameInput, lastName);
            return (T) this;
        });
    }

    public String getLastName() {
        return page.inputValue(lastNameInput);
    }

    @SuppressWarnings("unchecked")
    public T fillFirstName(String firstName) {
        return step("Заполнить имя: {firstName}", () -> {
            page.fill(firstNameInput, firstName);
            return (T) this;
        });
    }

    public String getFirstName() {
        return page.inputValue(firstNameInput);
    }

    @SuppressWarnings("unchecked")
    public T fillMiddleName(String middleName) {
        return step("Заполнить отчество: {middleName}", () -> {
            page.fill(middleNameInput, middleName);
            return (T) this;
        });
    }

    public String getMiddleName() {
        return page.inputValue(middleNameInput);
    }

    @SuppressWarnings("unchecked")
    public T fillContacts(String contacts) {
        return step("Заполнить контакты: {contacts}", () -> {
            page.fill(contactsInput, contacts);
            return (T) this;
        });
    }

    public String getContacts() {
        return page.inputValue(contactsInput);
    }

    @SuppressWarnings("unchecked")
    public T fillBirthday(String birthday) {
        return step("Заполнить дату рождения: {birthday}", () -> {
            page.fill(birthdayInput, birthday);
            return (T) this;
        });
    }

    public String getBirthday() {
        return page.inputValue(birthdayInput);
    }

    public void fillLessonsCount(int count) {
        step("Заполнить количество занятий: {count}", () -> {
            page.fill(lessonsCountInput, String.valueOf(count));
        });
    }

    public void fillAdditionalInfo(String info) {
        step("Заполнить доп. информацию", () -> {
            page.fill(additionalInfoInput, info);
        });
    }

    public String getAdditionalInfo() {
        return page.inputValue(additionalInfoInput);
    }

    @SuppressWarnings("unchecked")
    public T clearAllFields() {
        return step("Очистить все поля", () -> {
            page.fill(lastNameInput, "");
            page.fill(firstNameInput, "");
            page.fill(middleNameInput, "");
            page.fill(contactsInput, "");
            page.fill(birthdayInput, "");
            page.fill(lessonsCountInput, "");
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

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }
}

package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.Student;
import io.qameta.allure.Step;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddStudentPage extends BaseStudentPage<AddStudentPage> {

    // Сообщения об ошибках
    private final String errorMessages = ".error-message";
    private final String fieldErrors = ".field-error";
    private final String flashMessages = ".flash";

    // Атрибуты полей
    private final String requiredAttribute = "required";
    private final String oninputAttribute = "oninput";

    // Кнопка отмены
    private final String cancelButton = "a[href*='students']";

    public AddStudentPage(Page page) {
        super(page);
    }

    @Step("Переход на страницу добавления ученика")
    public AddStudentPage navigateTo() {
        page.navigate("/add_student");
        waitForPageLoad();
        return this;
    }

    @Step("Заполнить форму ученика")
    public AddStudentPage fillStudentForm(Student student) {
        if (student.getLastName() != null) {
            fillLastName(student.getLastName());
        }
        if (student.getFirstName() != null) {
            fillFirstName(student.getFirstName());
        }
        if (student.getMiddleName() != null) {
            fillMiddleName(student.getMiddleName());
        }
        if (student.getContacts() != null) {
            fillContacts(student.getContacts());
        }
        if (student.getBirthday() != null) {
            fillBirthday(student.getBirthday());
        }
        fillLessonsCount(student.getLessonsCount());
        if (student.getAdditionalInfo() != null) {
            fillAdditionalInfo(student.getAdditionalInfo());
        }
        return this;
    }

    @Step("Заполнить форму ученика с капитализацией")
    public AddStudentPage fillStudentFormWithCapitalization(Student student) {
        fillLastNameWithCapitalize(student.getLastName());
        fillFirstNameWithCapitalize(student.getFirstName());
        fillMiddleNameWithCapitalize(student.getMiddleName());
        fillContacts(student.getContacts());
        fillBirthday(student.getBirthday());
        fillLessonsCount(student.getLessonsCount());
        fillAdditionalInfo(student.getAdditionalInfo());
        return this;
    }

    @Step("Заполнить обязательные поля ученика")
    public AddStudentPage fillRequiredFields(Student student) {
        fillLastName(student.getLastName());
        fillFirstName(student.getFirstName());
        return this;
    }

    public AddStudentPage fillLastNameWithCapitalize(String lastName) {
        page.fill(lastNameInput, lastName);
        page.evaluate("document.querySelector('input[name=\"last_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    public AddStudentPage fillFirstNameWithCapitalize(String firstName) {
        page.fill(firstNameInput, firstName);
        page.evaluate("document.querySelector('input[name=\"first_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    public AddStudentPage fillMiddleNameWithCapitalize(String middleName) {
        page.fill(middleNameInput, middleName);
        page.evaluate("document.querySelector('input[name=\"middle_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    @Step("Заполнить дату рождения: {birthday}")
    @Override
    public AddStudentPage fillBirthday(String birthday) {
        page.evaluate("document.querySelector('input[name=\"birthday\"]').value = '" + birthday + "'");
        page.evaluate("document.querySelector('input[name=\"birthday\"]').dispatchEvent(new Event('change', { bubbles: true }))");
        return this;
    }

    public AddStudentPage fillBirthday(LocalDate birthday) {
        String dateStr = birthday.format(DateTimeFormatter.ISO_LOCAL_DATE);
        page.fill(birthdayInput, dateStr);
        return this;
    }

    @Step("Заполнить дату рождения")
    public AddStudentPage fillBirthday(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return fillBirthday(date);
    }

    public int getLessonsCount() {
        String value = page.inputValue(lessonsCountInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Student getStudentFromForm() {
        Student student = Student.builder().build();
        student.setLastName(getLastName());
        student.setFirstName(getFirstName());
        student.setMiddleName(getMiddleName());
        student.setContacts(getContacts());
        student.setBirthday(getBirthday());
        student.setLessonsCount(getLessonsCount());
        student.setAdditionalInfo(getAdditionalInfo());
        return student;
    }

    @Step("Сохранить ученика")
    public StudentsPage clickSave() {
        page.click(submitButton);
        waitForPageLoad();
        return new StudentsPage(page);
    }

    @Step("Сохранить и остаться на странице")
    public AddStudentPage clickSaveAndStay() {
        page.click(submitButton);
        waitForPageLoad();
        return this;
    }

    @Step("Отменить добавление ученика")
    public StudentsPage clickCancel() {
        page.click(cancelButton);
        waitForPageLoad();
        return new StudentsPage(page);
    }

    @Step("Очистить поле: {fieldName}")
    public AddStudentPage clearField(String fieldName) {
        String selector = getFieldSelector(fieldName);
        page.fill(selector, "");
        return this;
    }

    @Step("Отправить форму ученика")
    public StudentsPage submitStudent(Student student) {
        fillStudentForm(student);
        return clickSave();
    }

    @Step("Отправить форму ученика и проверить")
    public StudentsPage submitStudentAndVerify(Student student) {
        fillStudentForm(student);
        StudentsPage studentsPage = clickSave();

        String fullName = student.getFullName();
        studentsPage.waitForStudentToAppear(fullName, 10);

        assert studentsPage.hasSuccessMessage("Ученик успешно добавлен") :
                "Не найдено сообщение об успешном добавлении";

        return studentsPage;
    }

    public boolean areAllFieldsVisible() {
        return page.isVisible(lastNameInput) &&
                page.isVisible(firstNameInput) &&
                page.isVisible(middleNameInput) &&
                page.isVisible(contactsInput) &&
                page.isVisible(birthdayInput) &&
                page.isVisible(lessonsCountInput) &&
                page.isVisible(additionalInfoInput);
    }

    public boolean isFieldRequired(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, requiredAttribute) != null;
    }

    public boolean hasCapitalizationAttribute(String fieldName) {
        String selector = getFieldSelector(fieldName);
        String oninput = page.getAttribute(selector, oninputAttribute);
        return oninput != null && oninput.contains("capitalizeFirst");
    }

    public boolean areRequiredFieldsMarked() {
        return isFieldRequired("last_name") && isFieldRequired("first_name");
    }

    public boolean areCapitalizationAttributesPresent() {
        return hasCapitalizationAttribute("last_name") &&
                hasCapitalizationAttribute("first_name") &&
                hasCapitalizationAttribute("middle_name");
    }

    public boolean hasErrorMessages() {
        return page.locator(errorMessages).count() > 0 ||
                page.locator(fieldErrors).count() > 0 ||
                page.locator(flashMessages + ".error").count() > 0;
    }

    public List<String> getErrorMessages() {
        List<String> errors = new ArrayList<>();
        errors.addAll(page.locator(errorMessages).allTextContents());
        errors.addAll(page.locator(fieldErrors).allTextContents());
        errors.addAll(page.locator(flashMessages + ".error").allTextContents());
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
                isFieldFilled("last_name") &&
                isFieldFilled("first_name");
    }

    public boolean isCancelButtonVisible() {
        return page.isVisible(cancelButton);
    }

    public String getCancelButtonText() {
        return page.textContent(cancelButton);
    }

    public boolean isFormDataCorrect(Student expectedStudent) {
        Student actualStudent = getStudentFromForm();
        boolean lastNameMatch = expectedStudent.getLastName().equals(actualStudent.getLastName());
        boolean firstNameMatch = expectedStudent.getFirstName().equals(actualStudent.getFirstName());
        boolean middleNameMatch = expectedStudent.getMiddleName().equals(actualStudent.getMiddleName());
        boolean contactsMatch = expectedStudent.getContacts().equals(actualStudent.getContacts());
        boolean birthdayMatch = expectedStudent.getBirthday().equals(actualStudent.getBirthday());
        boolean lessonsCountMatch = expectedStudent.getLessonsCount() == actualStudent.getLessonsCount();
        boolean additionalInfoMatch = expectedStudent.getAdditionalInfo().equals(actualStudent.getAdditionalInfo());

        return lastNameMatch && firstNameMatch && middleNameMatch && contactsMatch &&
                birthdayMatch && lessonsCountMatch && additionalInfoMatch;
    }

    public boolean isUrlCorrect() {
        return page.url().contains("/add_student");
    }
}

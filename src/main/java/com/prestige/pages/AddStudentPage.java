package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddStudentPage extends BasePage {

    // Заголовок страницы
    private final String pageTitle = "h1";

    // Поля формы
    private final String lastNameInput = "input[name='last_name']";
    private final String firstNameInput = "input[name='first_name']";
    private final String middleNameInput = "input[name='middle_name']";
    private final String contactsInput = "input[name='contacts']";
    private final String birthdayInput = "input[name='birthday']";
    private final String lessonsCountInput = "input[name='lessons_count']";
    private final String additionalInfoInput = "input[name='additional_info']";

    // Кнопки
    private final String saveButton = "button[type='submit']";
    private final String cancelButton = "a[href*='students']";

    // Сообщения об ошибках
    private final String errorMessages = ".error-message";
    private final String fieldErrors = ".field-error";
    private final String flashMessages = ".flash";

    // Атрибуты полей
    private final String requiredAttribute = "required";
    private final String oninputAttribute = "oninput";

    public AddStudentPage(Page page) {
        super(page);
    }

    /**
     * Перейти на страницу добавления ученика
     */
    public AddStudentPage navigateTo() {
        page.navigate("/add_student");
        waitForPageLoad();
        return this;
    }

    /**
     * Проверить, что страница загружена
     */
    @Override
    public boolean isPageLoaded() {
        return page.isVisible(pageTitle) &&
                page.textContent(pageTitle).contains("Добавить ученика") &&
                page.isVisible(lastNameInput) &&
                page.isVisible(firstNameInput);
    }

    /**
     * Получить заголовок страницы
     */
    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    // ============ ЗАПОЛНЕНИЕ ФОРМЫ ============

    /**
     * Заполнить форму данными ученика
     */
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

    /**
     * Заполнить форму с автоматической капитализацией
     */
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

    /**
     * Заполнить только обязательные поля
     */
    public AddStudentPage fillRequiredFields(Student student) {
        fillLastName(student.getLastName());
        fillFirstName(student.getFirstName());
        return this;
    }

    // ============ МЕТОДЫ ДЛЯ ОТДЕЛЬНЫХ ПОЛЕЙ ============

    /**
     * Заполнить фамилию
     */
    public AddStudentPage fillLastName(String lastName) {
        page.fill(lastNameInput, lastName);
        return this;
    }

    /**
     * Заполнить фамилию с капитализацией
     */
    public AddStudentPage fillLastNameWithCapitalize(String lastName) {
        page.fill(lastNameInput, lastName);
        page.evaluate("document.querySelector('input[name=\"last_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    /**
     * Заполнить имя
     */
    public AddStudentPage fillFirstName(String firstName) {
        page.fill(firstNameInput, firstName);
        return this;
    }

    /**
     * Заполнить имя с капитализацией
     */
    public AddStudentPage fillFirstNameWithCapitalize(String firstName) {
        page.fill(firstNameInput, firstName);
        page.evaluate("document.querySelector('input[name=\"first_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    /**
     * Заполнить отчество
     */
    public AddStudentPage fillMiddleName(String middleName) {
        page.fill(middleNameInput, middleName);
        return this;
    }

    /**
     * Заполнить отчество с капитализацией
     */
    public AddStudentPage fillMiddleNameWithCapitalize(String middleName) {
        page.fill(middleNameInput, middleName);
        page.evaluate("document.querySelector('input[name=\"middle_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    /**
     * Заполнить контакты
     */
    public AddStudentPage fillContacts(String contacts) {
        page.fill(contactsInput, contacts);
        return this;
    }

    /**
     * Заполнить день рождения
     */
//    public AddStudentPage fillBirthday(String birthday) {
//        page.fill(birthdayInput, birthday);
//        return this;
//    }
    public AddStudentPage fillBirthday(String birthday) {
        // Ожидаемый формат: YYYY-MM-DD
        page.evaluate("document.querySelector('input[name=\"birthday\"]').value = '" + birthday + "'");
        page.evaluate("document.querySelector('input[name=\"birthday\"]').dispatchEvent(new Event('change', { bubbles: true }))");
        return this;
    }

    /**
     * Заполнить день рождения (LocalDate)
     */
    public AddStudentPage fillBirthday(LocalDate birthday) {
        String dateStr = birthday.format(DateTimeFormatter.ISO_LOCAL_DATE);
        page.fill(birthdayInput, dateStr);
        return this;
    }

    /**
     * Заполнить день рождения (год, месяц, день)
     */
    public AddStudentPage fillBirthday(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return fillBirthday(date);
    }

    /**
     * Заполнить количество занятий
     */
    public AddStudentPage fillLessonsCount(int count) {
        page.fill(lessonsCountInput, String.valueOf(count));
        return this;
    }

    /**
     * Заполнить дополнительную информацию
     */
    public AddStudentPage fillAdditionalInfo(String info) {
        page.fill(additionalInfoInput, info);
        return this;
    }

    // ============ ПОЛУЧЕНИЕ ДАННЫХ ИЗ ФОРМЫ ============

    /**
     * Получить значение поля "Фамилия"
     */
    public String getLastName() {
        return page.inputValue(lastNameInput);
    }

    /**
     * Получить значение поля "Имя"
     */
    public String getFirstName() {
        return page.inputValue(firstNameInput);
    }

    /**
     * Получить значение поля "Отчество"
     */
    public String getMiddleName() {
        return page.inputValue(middleNameInput);
    }

    /**
     * Получить значение поля "Контакты"
     */
    public String getContacts() {
        return page.inputValue(contactsInput);
    }

    /**
     * Получить значение поля "День рождения"
     */
    public String getBirthday() {
        return page.inputValue(birthdayInput);
    }

    /**
     * Получить значение поля "Кол-во занятий"
     */
    public int getLessonsCount() {
        String value = page.inputValue(lessonsCountInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Получить значение поля "Доп. информация"
     */
    public String getAdditionalInfo() {
        return page.inputValue(additionalInfoInput);
    }

    /**
     * Получить все данные из формы в виде объекта Student
     */
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

    // ============ ДЕЙСТВИЯ С ФОРМОЙ ============

    /**
     * Сохранить ученика и перейти на страницу учеников
     */
    public StudentsPage clickSave() {
        page.click(saveButton);
        waitForPageLoad();
        return new StudentsPage(page);
    }

    /**
     * Сохранить ученика (остаться на странице, если есть ошибки)
     */
    public AddStudentPage clickSaveAndStay() {
        page.click(saveButton);
        waitForPageLoad();
        return this;
    }

    /**
     * Отменить добавление и вернуться на страницу учеников
     */
    public StudentsPage clickCancel() {
        page.click(cancelButton);
        waitForPageLoad();
        return new StudentsPage(page);
    }

    /**
     * Очистить все поля формы
     */
    public AddStudentPage clearAllFields() {
        page.fill(lastNameInput, "");
        page.fill(firstNameInput, "");
        page.fill(middleNameInput, "");
        page.fill(contactsInput, "");
        page.fill(birthdayInput, "");
        page.fill(lessonsCountInput, "");
        page.fill(additionalInfoInput, "");
        return this;
    }

    /**
     * Очистить конкретное поле
     */
    public AddStudentPage clearField(String fieldName) {
        String selector = getFieldSelector(fieldName);
        page.fill(selector, "");
        return this;
    }

    /**
     * Отправить форму с данными ученика
     */
    public StudentsPage submitStudent(Student student) {
        fillStudentForm(student);
        return clickSave();
    }

    /**
     * Отправить форму с данными ученика и проверить успешное добавление
     */
    public StudentsPage submitStudentAndVerify(Student student) {
        fillStudentForm(student);
        StudentsPage studentsPage = clickSave();

        // Проверяем, что ученик появился в таблице
        String fullName = student.getFullName();
        studentsPage.waitForStudentToAppear(fullName, 10);

        // Проверяем flash сообщение
        assert studentsPage.hasSuccessMessage("Ученик успешно добавлен") :
                "Не найдено сообщение об успешном добавлении";

        return studentsPage;
    }

    // ============ ВАЛИДАЦИИ ============

    /**
     * Проверить, что все поля доступны
     */
    public boolean areAllFieldsVisible() {
        return page.isVisible(lastNameInput) &&
                page.isVisible(firstNameInput) &&
                page.isVisible(middleNameInput) &&
                page.isVisible(contactsInput) &&
                page.isVisible(birthdayInput) &&
                page.isVisible(lessonsCountInput) &&
                page.isVisible(additionalInfoInput);
    }

    /**
     * Проверить, что поле является обязательным
     */
    public boolean isFieldRequired(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, requiredAttribute) != null;
    }

    /**
     * Проверить, что поле имеет атрибут oninput для капитализации
     */
    public boolean hasCapitalizationAttribute(String fieldName) {
        String selector = getFieldSelector(fieldName);
        String oninput = page.getAttribute(selector, oninputAttribute);
        return oninput != null && oninput.contains("capitalizeFirst");
    }

    /**
     * Проверить, что все обязательные поля имеют атрибут required
     */
    public boolean areRequiredFieldsMarked() {
        return isFieldRequired("last_name") && isFieldRequired("first_name");
    }

    /**
     * Проверить, что все поля имеют атрибут для капитализации
     */
    public boolean areCapitalizationAttributesPresent() {
        return hasCapitalizationAttribute("last_name") &&
                hasCapitalizationAttribute("first_name") &&
                hasCapitalizationAttribute("middle_name");
    }

    /**
     * Проверить наличие сообщений об ошибках
     */
    public boolean hasErrorMessages() {
        return page.locator(errorMessages).count() > 0 ||
                page.locator(fieldErrors).count() > 0 ||
                page.locator(flashMessages + ".error").count() > 0;
    }

    /**
     * Получить все сообщения об ошибках
     */
    public List<String> getErrorMessages() {
        List<String> errors = new ArrayList<>();
        errors.addAll(page.locator(errorMessages).allTextContents());
        errors.addAll(page.locator(fieldErrors).allTextContents());
        errors.addAll(page.locator(flashMessages + ".error").allTextContents());
        return errors;
    }

    /**
     * Получить сообщение об ошибке для конкретного поля
     */
    public String getFieldError(String fieldName) {
        String selector = ".field-error[data-field='" + fieldName + "']";
        if (page.isVisible(selector)) {
            return page.textContent(selector);
        }
        return null;
    }

    /**
     * Проверить, что форма валидна (нет ошибок)
     */
    public boolean isFormValid() {
        return !hasErrorMessages() &&
                isFieldFilled("last_name") &&
                isFieldFilled("first_name");
    }

    /**
     * Проверить, что поле заполнено
     */
    public boolean isFieldFilled(String fieldName) {
        String value = getFieldValue(fieldName);
        return value != null && !value.isEmpty();
    }

    /**
     * Получить значение поля по имени
     */
    private String getFieldValue(String fieldName) {
        switch (fieldName) {
            case "last_name":
                return getLastName();
            case "first_name":
                return getFirstName();
            case "middle_name":
                return getMiddleName();
            case "contacts":
                return getContacts();
            case "birthday":
                return getBirthday();
            case "lessons_count":
                return String.valueOf(getLessonsCount());
            case "additional_info":
                return getAdditionalInfo();
            default:
                throw new IllegalArgumentException("Неизвестное поле: " + fieldName);
        }
    }

    /**
     * Получить селектор поля по имени
     */
    private String getFieldSelector(String fieldName) {
        switch (fieldName) {
            case "last_name":
                return lastNameInput;
            case "first_name":
                return firstNameInput;
            case "middle_name":
                return middleNameInput;
            case "contacts":
                return contactsInput;
            case "birthday":
                return birthdayInput;
            case "lessons_count":
                return lessonsCountInput;
            case "additional_info":
                return additionalInfoInput;
            default:
                throw new IllegalArgumentException("Неизвестное поле: " + fieldName);
        }
    }

    // ============ ПРОВЕРКИ НА СТРАНИЦЕ ============

    /**
     * Проверить, что кнопка "Сохранить" видна
     */
    public boolean isSaveButtonVisible() {
        return page.isVisible(saveButton);
    }

    /**
     * Проверить, что кнопка "Отмена" видна
     */
    public boolean isCancelButtonVisible() {
        return page.isVisible(cancelButton);
    }

    /**
     * Получить текст кнопки "Сохранить"
     */
    public String getSaveButtonText() {
        return page.textContent(saveButton);
    }

    /**
     * Получить текст кнопки "Отмена"
     */
    public String getCancelButtonText() {
        return page.textContent(cancelButton);
    }

    /**
     * Проверить, что поле доступно для редактирования
     */
    public boolean isFieldEditable(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.isEditable(selector);
    }

    /**
     * Проверить, что поле заблокировано (readonly или disabled)
     */
    public boolean isFieldReadonly(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, "readonly") != null ||
                page.getAttribute(selector, "disabled") != null;
    }

    /**
     * Получить placeholder поля
     */
    public String getFieldPlaceholder(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, "placeholder");
    }

    /**
     * Получить тип поля (text, number, date и т.д.)
     */
    public String getFieldType(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, "type");
    }

    // ============ ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ============
    /**
     * Проверить, что все поля соответствуют данным ученика
     */
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

    /**
     * Получить текущий URL страницы
     */
    public String getCurrentUrl() {
        return page.url();
    }

    /**
     * Проверить, что URL содержит правильный путь
     */
    public boolean isUrlCorrect() {
        return page.url().contains("/add_student");
    }
}
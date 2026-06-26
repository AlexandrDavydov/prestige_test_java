package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.Coach;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditCoachPage extends BasePage {

    // Заголовок страницы
    private final String pageTitle = "h1";

    // Поля формы
    private final String lastNameInput = "input[name='last_name']";
    private final String firstNameInput = "input[name='first_name']";
    private final String middleNameInput = "input[name='middle_name']";
    private final String contactsInput = "input[name='contacts']";
    private final String birthdayInput = "input[name='birthday']";
    private final String lessonsCountInput = "input[name='lessons_count']";
    private final String lessonsPaidInput = "input[name='lessons_paid']";
    private final String studentPaymentInput = "input[name='student_payment']";
    private final String additionalInfoInput = "input[name='additional_info']";

    // Кнопки
    private final String saveButton = "button[type='submit']";
    private final String cancelButton = "a[href*='coaches']";

    // Сообщения об ошибках (если есть)
    private final String errorMessages = ".error-message";
    private final String fieldErrors = ".field-error";

    public EditCoachPage(Page page) {
        super(page);
    }

    /**
     * Проверить, что страница загружена
     */
    @Override
    public boolean isPageLoaded() {
        return page.isVisible(pageTitle) &&
                page.textContent(pageTitle).contains("Редактировать тренера") &&
                page.isVisible(lastNameInput);
    }

    /**
     * Получить заголовок страницы
     */
    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    // ============ ЗАПОЛНЕНИЕ ФОРМЫ ============

    /**
     * Заполнить форму данными тренера
     */
    public EditCoachPage fillCoachForm(Coach coach) {
        if (coach.getLastName() != null) {
            fillLastName(coach.getLastName());
        }
        if (coach.getFirstName() != null) {
            fillFirstName(coach.getFirstName());
        }
        if (coach.getMiddleName() != null) {
            fillMiddleName(coach.getMiddleName());
        }
        if (coach.getContacts() != null) {
            fillContacts(coach.getContacts());
        }
        if (coach.getBirthday() != null) {
            fillBirthday(coach.getBirthday());
        }
        fillLessonsCount(coach.getLessonsCount());
        fillLessonsPaid(coach.getLessonsPaid());
        fillStudentPayment(coach.getStudentPayment());
        if (coach.getAdditionalInfo() != null) {
            fillAdditionalInfo(coach.getAdditionalInfo());
        }
        return this;
    }

    /**
     * Заполнить форму с очисткой всех полей
     */
    public EditCoachPage fillCoachFormCleared(Coach coach) {
        clearAllFields();
        return fillCoachForm(coach);
    }

    /**
     * Заполнить только обязательные поля
     */
    public EditCoachPage fillRequiredFields(Coach coach) {
        fillLastName(coach.getLastName());
        fillFirstName(coach.getFirstName());
        return this;
    }

    // ============ МЕТОДЫ ДЛЯ ОТДЕЛЬНЫХ ПОЛЕЙ ============

    /**
     * Заполнить фамилию
     */
    public EditCoachPage fillLastName(String lastName) {
        page.fill(lastNameInput, lastName);
        return this;
    }

    /**
     * Заполнить имя
     */
    public EditCoachPage fillFirstName(String firstName) {
        page.fill(firstNameInput, firstName);
        return this;
    }

    /**
     * Заполнить отчество
     */
    public EditCoachPage fillMiddleName(String middleName) {
        page.fill(middleNameInput, middleName);
        return this;
    }

    /**
     * Заполнить контакты
     */
    public EditCoachPage fillContacts(String contacts) {
        page.fill(contactsInput, contacts);
        return this;
    }

    /**
     * Заполнить день рождения
     */
    public EditCoachPage fillBirthday(String birthday) {
        page.fill(birthdayInput, birthday);
        return this;
    }

    /**
     * Заполнить день рождения (LocalDate)
     */
    public EditCoachPage fillBirthday(LocalDate birthday) {
        String dateStr = birthday.format(DateTimeFormatter.ISO_LOCAL_DATE);
        page.fill(birthdayInput, dateStr);
        return this;
    }

    /**
     * Заполнить количество проведенных занятий
     */
    public EditCoachPage fillLessonsCount(int count) {
        page.fill(lessonsCountInput, String.valueOf(count));
        return this;
    }

    /**
     * Заполнить количество оплаченных занятий
     */
    public EditCoachPage fillLessonsPaid(int paid) {
        page.fill(lessonsPaidInput, String.valueOf(paid));
        return this;
    }

    /**
     * Заполнить оплату за одного ученика
     */
    public EditCoachPage fillStudentPayment(double payment) {
        page.fill(studentPaymentInput, String.valueOf(payment));
        return this;
    }

    /**
     * Заполнить дополнительную информацию
     */
    public EditCoachPage fillAdditionalInfo(String info) {
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
     * Получить значение поля "Кол-во проведённых занятий"
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
     * Получить значение поля "Кол-во оплаченных занятий"
     */
    public int getLessonsPaid() {
        String value = page.inputValue(lessonsPaidInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Получить значение поля "Оплата за одного ученика"
     */
    public double getStudentPayment() {
        String value = page.inputValue(studentPaymentInput);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Получить значение поля "Доп. информация"
     */
    public String getAdditionalInfo() {
        return page.inputValue(additionalInfoInput);
    }

    /**
     * Получить все данные из формы в виде объекта Coach
     */
    public Coach getCoachFromForm() {
        Coach coach = new Coach();
        coach.setLastName(getLastName());
        coach.setFirstName(getFirstName());
        coach.setMiddleName(getMiddleName());
        coach.setContacts(getContacts());
        coach.setBirthday(getBirthday());
        coach.setLessonsCount(getLessonsCount());
        coach.setLessonsPaid(getLessonsPaid());
        coach.setStudentPayment(getStudentPayment());
        coach.setAdditionalInfo(getAdditionalInfo());
        return coach;
    }

    // ============ ДЕЙСТВИЯ С ФОРМОЙ ============

    /**
     * Сохранить изменения и перейти на страницу тренеров
     */
    public CoachesPage clickSave() {
        page.click(saveButton);
        waitForPageLoad();
        return new CoachesPage(page);
    }

    /**
     * Сохранить изменения (остаться на странице, если есть ошибки)
     */
    public EditCoachPage clickSaveAndStay() {
        page.click(saveButton);
        waitForPageLoad();
        return this;
    }

    /**
     * Отменить редактирование и вернуться на страницу тренеров
     */
    public CoachesPage clickCancel() {
        page.click(cancelButton);
        waitForPageLoad();
        return new CoachesPage(page);
    }

    /**
     * Очистить все поля формы
     */
    public EditCoachPage clearAllFields() {
        page.fill(lastNameInput, "");
        page.fill(firstNameInput, "");
        page.fill(middleNameInput, "");
        page.fill(contactsInput, "");
        page.fill(birthdayInput, "");
        page.fill(lessonsCountInput, "");
        page.fill(lessonsPaidInput, "");
        page.fill(studentPaymentInput, "");
        page.fill(additionalInfoInput, "");
        return this;
    }

    /**
     * Очистить конкретное поле
     */
    public EditCoachPage clearField(String fieldName) {
        switch (fieldName) {
            case "last_name":
                page.fill(lastNameInput, "");
                break;
            case "first_name":
                page.fill(firstNameInput, "");
                break;
            case "middle_name":
                page.fill(middleNameInput, "");
                break;
            case "contacts":
                page.fill(contactsInput, "");
                break;
            case "birthday":
                page.fill(birthdayInput, "");
                break;
            case "lessons_count":
                page.fill(lessonsCountInput, "");
                break;
            case "lessons_paid":
                page.fill(lessonsPaidInput, "");
                break;
            case "student_payment":
                page.fill(studentPaymentInput, "");
                break;
            case "additional_info":
                page.fill(additionalInfoInput, "");
                break;
            default:
                throw new IllegalArgumentException("Неизвестное поле: " + fieldName);
        }
        return this;
    }

    // ============ ВАЛИДАЦИИ ============

    /**
     * Проверить, что все поля соответствуют данным тренера
     */
    public boolean isFormDataCorrect(Coach expectedCoach) {
        Coach actualCoach = getCoachFromForm();

        boolean lastNameMatch = expectedCoach.getLastName().equals(actualCoach.getLastName());
        boolean firstNameMatch = expectedCoach.getFirstName().equals(actualCoach.getFirstName());
        boolean middleNameMatch = expectedCoach.getMiddleName().equals(actualCoach.getMiddleName());
        boolean contactsMatch = expectedCoach.getContacts().equals(actualCoach.getContacts());
        boolean birthdayMatch = expectedCoach.getBirthday().equals(actualCoach.getBirthday());
        boolean lessonsCountMatch = expectedCoach.getLessonsCount() == actualCoach.getLessonsCount();
        boolean lessonsPaidMatch = expectedCoach.getLessonsPaid() == actualCoach.getLessonsPaid();
        boolean studentPaymentMatch = expectedCoach.getStudentPayment() == actualCoach.getStudentPayment();
        boolean additionalInfoMatch = expectedCoach.getAdditionalInfo().equals(actualCoach.getAdditionalInfo());

        return lastNameMatch && firstNameMatch && middleNameMatch && contactsMatch &&
                birthdayMatch && lessonsCountMatch && lessonsPaidMatch &&
                studentPaymentMatch && additionalInfoMatch;
    }

    /**
     * Проверить, что поле содержит значение
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
            case "lessons_paid":
                return String.valueOf(getLessonsPaid());
            case "student_payment":
                return String.valueOf(getStudentPayment());
            case "additional_info":
                return getAdditionalInfo();
            default:
                throw new IllegalArgumentException("Неизвестное поле: " + fieldName);
        }
    }

    /**
     * Проверить, что все обязательные поля заполнены
     */
    public boolean areRequiredFieldsFilled() {
        return isFieldFilled("last_name") && isFieldFilled("first_name");
    }

    /**
     * Проверить наличие сообщений об ошибках
     */
    public boolean hasErrorMessages() {
        return page.locator(errorMessages).count() > 0 ||
                page.locator(fieldErrors).count() > 0;
    }

    /**
     * Получить все сообщения об ошибках
     */
    public java.util.List<String> getErrorMessages() {
        return page.locator(errorMessages).allTextContents();
    }

    /**
     * Получить сообщения об ошибках для конкретного поля
     */
    public String getFieldError(String fieldName) {
        String selector = ".field-error[data-field='" + fieldName + "']";
        if (page.isVisible(selector)) {
            return page.textContent(selector);
        }
        return null;
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
     * Проверить, что поле доступно для редактирования
     */
    public boolean isFieldEditable(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.isEditable(selector);
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
            case "lessons_paid":
                return lessonsPaidInput;
            case "student_payment":
                return studentPaymentInput;
            case "additional_info":
                return additionalInfoInput;
            default:
                throw new IllegalArgumentException("Неизвестное поле: " + fieldName);
        }
    }

    /**
     * Проверить, что поле заблокировано (readonly или disabled)
     */
    public boolean isFieldReadonly(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, "readonly") != null ||
                page.getAttribute(selector, "disabled") != null;
    }

    // ============ ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ============

    /**
     * Получить placeholder поля
     */
    public String getFieldPlaceholder(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, "placeholder");
    }

    /**
     * Получить label для поля
     */
    public String getFieldLabel(String fieldName) {
        String selector = "label[for='" + fieldName + "']";
        if (page.isVisible(selector)) {
            return page.textContent(selector);
        }
        return null;
    }

    /**
     * Проверить, что форма валидна (нет ошибок)
     */
    public boolean isFormValid() {
        return !hasErrorMessages() && areRequiredFieldsFilled();
    }

    /**
     * Получить текущий URL страницы
     */
    public String getCurrentUrl() {
        return page.url();
    }

    /**
     * Проверить, что URL содержит ID тренера
     */
    public boolean isUrlForCoach(Long coachId) {
        return page.url().contains("/edit_coach/" + coachId);
    }

    /**
     * Сохранить форму и проверить успешное сохранение
     */
    public CoachesPage saveAndVerifySuccess() {
        clickSave();

        // Проверяем flash сообщение об успехе
        assert hasSuccessMessage("Тренер успешно обновлен") :
                "Не найдено сообщение об успешном обновлении";

        return new CoachesPage(page);
    }

    /**
     * Отменить редактирование и проверить возврат на страницу тренеров
     */
    public CoachesPage cancelAndVerify() {
        clickCancel();

        // Проверяем, что мы на странице тренеров
        CoachesPage coachesPage = new CoachesPage(page);
        assert coachesPage.isPageLoaded() : "Страница тренеров не загружена";

        return coachesPage;
    }
}
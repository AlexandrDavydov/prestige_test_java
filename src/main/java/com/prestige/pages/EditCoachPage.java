package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.Coach;
import static com.prestige.utils.StepHelper.step;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditCoachPage extends BasePage {

    private final String pageTitle = "h1";

    private final String lastNameInput = "input[name='last_name']";
    private final String firstNameInput = "input[name='first_name']";
    private final String middleNameInput = "input[name='middle_name']";
    private final String contactsInput = "input[name='contacts']";
    private final String birthdayInput = "input[name='birthday']";
    private final String lessonsCountInput = "input[name='lessons_count']";
    private final String lessonsPaidInput = "input[name='lessons_paid']";
    private final String studentPaymentInput = "input[name='student_payment']";
    private final String additionalInfoInput = "input[name='additional_info']";

    private final String saveButton = "button[type='submit']";
    private final String cancelButton = "a[href*='coaches']";

    private final String errorMessages = ".error-message";
    private final String fieldErrors = ".field-error";

    public EditCoachPage(Page page) {
        super(page);
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    // ============ ЗАПОЛНЕНИЕ ФОРМЫ ============

    public EditCoachPage fillCoachForm(Coach coach) {
        return step("Заполнить форму редактирования тренера", () -> {
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
        });
    }

    public EditCoachPage fillCoachFormCleared(Coach coach) {
        return step("Очистить и заполнить форму тренера", () -> {
            clearAllFields();
            return fillCoachForm(coach);
        });
    }

    public EditCoachPage fillRequiredFields(Coach coach) {
        return step("Заполнить обязательные поля тренера", () -> {
            fillLastName(coach.getLastName());
            fillFirstName(coach.getFirstName());
            return this;
        });
    }

    // ============ МЕТОДЫ ДЛЯ ОТДЕЛЬНЫХ ПОЛЕЙ ============

    public EditCoachPage fillLastName(String lastName) {
        return step("Заполнить фамилию тренера: {lastName}", () -> {
            page.fill(lastNameInput, lastName);
            return this;
        });
    }

    public EditCoachPage fillFirstName(String firstName) {
        return step("Заполнить имя тренера: {firstName}", () -> {
            page.fill(firstNameInput, firstName);
            return this;
        });
    }

    public EditCoachPage fillMiddleName(String middleName) {
        return step("Заполнить отчество тренера: {middleName}", () -> {
            page.fill(middleNameInput, middleName);
            return this;
        });
    }

    public EditCoachPage fillContacts(String contacts) {
        return step("Заполнить контакты тренера: {contacts}", () -> {
            page.fill(contactsInput, contacts);
            return this;
        });
    }

    public EditCoachPage fillBirthday(String birthday) {
        return step("Заполнить дату рождения тренера: {birthday}", () -> {
            page.fill(birthdayInput, birthday);
            return this;
        });
    }

    public EditCoachPage fillBirthday(LocalDate birthday) {
        return step("Заполнить дату рождения тренера: {birthday}", () -> {
            String dateStr = birthday.format(DateTimeFormatter.ISO_LOCAL_DATE);
            page.fill(birthdayInput, dateStr);
            return this;
        });
    }

    public EditCoachPage fillLessonsCount(int count) {
        return step("Заполнить количество занятий: {count}", () -> {
            page.fill(lessonsCountInput, String.valueOf(count));
            return this;
        });
    }

    public EditCoachPage fillLessonsPaid(int paid) {
        return step("Заполнить оплаченных занятий: {paid}", () -> {
            page.fill(lessonsPaidInput, String.valueOf(paid));
            return this;
        });
    }

    public EditCoachPage fillStudentPayment(double payment) {
        return step("Заполнить оплату за ученика: {payment}", () -> {
            page.fill(studentPaymentInput, String.valueOf(payment));
            return this;
        });
    }

    public EditCoachPage fillAdditionalInfo(String info) {
        return step("Заполнить доп. информацию тренера", () -> {
            page.fill(additionalInfoInput, info);
            return this;
        });
    }

    // ============ ПОЛУЧЕНИЕ ДАННЫХ ИЗ ФОРМЫ ============

    public String getLastName() {
        return page.inputValue(lastNameInput);
    }

    public String getFirstName() {
        return page.inputValue(firstNameInput);
    }

    public String getMiddleName() {
        return page.inputValue(middleNameInput);
    }

    public String getContacts() {
        return page.inputValue(contactsInput);
    }

    public String getBirthday() {
        return page.inputValue(birthdayInput);
    }

    public int getLessonsCount() {
        String value = page.inputValue(lessonsCountInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getLessonsPaid() {
        String value = page.inputValue(lessonsPaidInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getStudentPayment() {
        String value = page.inputValue(studentPaymentInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getAdditionalInfo() {
        return page.inputValue(additionalInfoInput);
    }

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

    public CoachesPage clickSave() {
        return step("Сохранить изменения тренера", () -> {
            page.click(saveButton);
            waitForPageLoad();
            return new CoachesPage(page);
        });
    }

    public EditCoachPage clickSaveAndStay() {
        return step("Сохранить и остаться на странице", () -> {
            page.click(saveButton);
            waitForPageLoad();
            return this;
        });
    }

    public CoachesPage clickCancel() {
        return step("Отменить редактирование тренера", () -> {
            page.click(cancelButton);
            waitForPageLoad();
            return new CoachesPage(page);
        });
    }

    public EditCoachPage clearAllFields() {
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
            return this;
        });
    }

    public EditCoachPage clearField(String fieldName) {
        return step("Очистить поле тренера: {fieldName}", () -> {
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
        });
    }

    // ============ ВАЛИДАЦИИ ============

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

    public boolean isFieldFilled(String fieldName) {
        String value = getFieldValue(fieldName);
        return value != null && !value.isEmpty();
    }

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

    public boolean areRequiredFieldsFilled() {
        return isFieldFilled("last_name") && isFieldFilled("first_name");
    }

    public boolean hasErrorMessages() {
        return page.locator(errorMessages).count() > 0 ||
                page.locator(fieldErrors).count() > 0;
    }

    public java.util.List<String> getErrorMessages() {
        return page.locator(errorMessages).allTextContents();
    }

    public String getFieldError(String fieldName) {
        String selector = ".field-error[data-field='" + fieldName + "']";
        if (page.isVisible(selector)) {
            return page.textContent(selector);
        }
        return null;
    }

    // ============ ПРОВЕРКИ НА СТРАНИЦЕ ============

    public boolean isSaveButtonVisible() {
        return page.isVisible(saveButton);
    }

    public boolean isCancelButtonVisible() {
        return page.isVisible(cancelButton);
    }

    public boolean isFieldEditable(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.isEditable(selector);
    }

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

    public boolean isFieldReadonly(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, "readonly") != null ||
                page.getAttribute(selector, "disabled") != null;
    }

    // ============ ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ============

    public String getFieldPlaceholder(String fieldName) {
        String selector = getFieldSelector(fieldName);
        return page.getAttribute(selector, "placeholder");
    }

    public String getFieldLabel(String fieldName) {
        String selector = "label[for='" + fieldName + "']";
        if (page.isVisible(selector)) {
            return page.textContent(selector);
        }
        return null;
    }

    public boolean isFormValid() {
        return !hasErrorMessages() && areRequiredFieldsFilled();
    }

    public String getCurrentUrl() {
        return page.url();
    }

    public boolean isUrlForCoach(Long coachId) {
        return page.url().contains("/edit_coach/" + coachId);
    }

    public CoachesPage saveAndVerifySuccess() {
        step("Сохранить тренера и проверить успех", () -> {
            clickSave();

            assert hasSuccessMessage("Тренер успешно обновлен") :
                    "Не найдено сообщение об успешном обновлении";
        });
        return new CoachesPage(page);
    }
}

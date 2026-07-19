package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.Coach;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddCoachPage extends BaseCoachPage<AddCoachPage> {

    private final String errorMessages = ".error-message";
    private final String fieldErrors = ".field-error";
    private final String requiredAttribute = "required";
    private final String oninputAttribute = "oninput";

    public AddCoachPage(Page page) {
        super(page);
    }

    public AddCoachPage navigateTo() {
        page.navigate("/add_coach");
        waitForPageLoad();
        return this;
    }

    public AddCoachPage fillCoachForm(Coach coach) {
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

    public AddCoachPage fillCoachFormWithCapitalization(Coach coach) {
        fillLastNameWithCapitalize(coach.getLastName());
        fillFirstNameWithCapitalize(coach.getFirstName());
        fillMiddleNameWithCapitalize(coach.getMiddleName());
        fillContacts(coach.getContacts());
        fillBirthday(coach.getBirthday());
        fillLessonsCount(coach.getLessonsCount());
        fillLessonsPaid(coach.getLessonsPaid());
        fillStudentPayment(coach.getStudentPayment());
        fillAdditionalInfo(coach.getAdditionalInfo());
        return this;
    }

    public AddCoachPage fillRequiredFields(Coach coach) {
        fillLastName(coach.getLastName());
        fillFirstName(coach.getFirstName());
        return this;
    }

    public AddCoachPage fillLastNameWithCapitalize(String lastName) {
        page.fill(lastNameInput, lastName);
        page.evaluate("document.querySelector('input[name=\"last_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    public AddCoachPage fillFirstNameWithCapitalize(String firstName) {
        page.fill(firstNameInput, firstName);
        page.evaluate("document.querySelector('input[name=\"first_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    public AddCoachPage fillMiddleNameWithCapitalize(String middleName) {
        page.fill(middleNameInput, middleName);
        page.evaluate("document.querySelector('input[name=\"middle_name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    public AddCoachPage fillBirthday(LocalDate birthday) {
        String dateStr = birthday.format(DateTimeFormatter.ISO_LOCAL_DATE);
        page.fill(birthdayInput, dateStr);
        return this;
    }

    public AddCoachPage fillBirthday(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return fillBirthday(date);
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

    public CoachesPage clickSave() {
        page.click(submitButton);
        waitForPageLoad();
        return new CoachesPage(page);
    }

    public AddCoachPage clickSaveAndStay() {
        page.click(submitButton);
        waitForPageLoad();
        return this;
    }

    public CoachesPage clickCancel() {
        page.click(cancelButton);
        waitForPageLoad();
        return new CoachesPage(page);
    }

    public AddCoachPage clearField(String fieldName) {
        String selector = getFieldSelector(fieldName);
        page.fill(selector, "");
        return this;
    }

    public CoachesPage submitCoach(Coach coach) {
        fillCoachForm(coach);
        return clickSave();
    }

    public CoachesPage submitCoachAndVerify(Coach coach) {
        fillCoachForm(coach);
        CoachesPage coachesPage = clickSave();

        String fullName = coach.getFullName();
        coachesPage.waitForCoachToAppear(fullName, 10);

        assert coachesPage.hasSuccessMessage("Тренер успешно добавлен") :
                "Не найдено сообщение об успешном добавлении";

        return coachesPage;
    }

    public boolean areAllFieldsVisible() {
        return page.isVisible(lastNameInput) &&
                page.isVisible(firstNameInput) &&
                page.isVisible(middleNameInput) &&
                page.isVisible(contactsInput) &&
                page.isVisible(birthdayInput) &&
                page.isVisible(lessonsCountInput) &&
                page.isVisible(lessonsPaidInput) &&
                page.isVisible(studentPaymentInput) &&
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
        return isFieldRequired("last_name") && isFieldRequired("first_name") && isFieldRequired("middle_name");
    }

    public boolean areCapitalizationAttributesPresent() {
        return hasCapitalizationAttribute("last_name") &&
                hasCapitalizationAttribute("first_name") &&
                hasCapitalizationAttribute("middle_name");
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
                isFieldFilled("last_name") &&
                isFieldFilled("first_name") &&
                isFieldFilled("middle_name");
    }

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

    public boolean isUrlCorrect() {
        return page.url().contains("/add_coach");
    }
}

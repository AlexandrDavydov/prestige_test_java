package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.prestige.models.Student;
import com.prestige.utils.DataUtils;

import java.util.HashMap;
import java.util.Map;

public class EditStudentPage {
    private final Page page;

    // Локаторы
    private final Locator lastNameInput;
    private final Locator firstNameInput;
    private final Locator middleNameInput;
    private final Locator contactsInput;
    private final Locator birthdayInput;
    private final Locator lessonsCountInput;
    private final Locator additionalInfoInput;
    private final Locator submitButton;
    private final Locator cancelButton;
    private final Locator pageTitle;

    public EditStudentPage(Page page) {
        this.page = page;

        // Инициализация локаторов
        this.lastNameInput = page.locator("input[name='last_name']");
        this.firstNameInput = page.locator("input[name='first_name']");
        this.middleNameInput = page.locator("input[name='middle_name']");
        this.contactsInput = page.locator("input[name='contacts']");
        this.birthdayInput = page.locator("input[name='birthday']");
        this.lessonsCountInput = page.locator("input[name='lessons_count']");
        this.additionalInfoInput = page.locator("input[name='additional_info']");
        this.submitButton = page.locator("button[type='submit']");
        this.cancelButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("❌ Отмена"));
        this.pageTitle = page.locator("h1");
    }

    // Методы для работы с полями

    public String getLastName() {
        return lastNameInput.getAttribute("value");
    }

    public EditStudentPage setLastName(String lastName) {
        lastNameInput.fill(lastName);
        return this;
    }

    public String getFirstName() {
        return firstNameInput.getAttribute("value");
    }

    public EditStudentPage setFirstName(String firstName) {
        firstNameInput.fill(firstName);
        return this;
    }

    public String getMiddleName() {
        return middleNameInput.getAttribute("value");
    }

    public EditStudentPage setMiddleName(String middleName) {
        middleNameInput.fill(middleName);
        return this;
    }

    public String getContacts() {
        return contactsInput.getAttribute("value");
    }

    public EditStudentPage setContacts(String contacts) {
        contactsInput.fill(contacts);
        return this;
    }

    public String getBirthday() {
        return birthdayInput.getAttribute("value");
    }

    public EditStudentPage setBirthday(String birthday) {
        birthdayInput.fill(birthday);
        return this;
    }

    public String getLessonsCount() {
        return lessonsCountInput.getAttribute("value");
    }

    public EditStudentPage setLessonsCount(int count) {
        lessonsCountInput.fill(String.valueOf(count));
        return this;
    }

    public String getAdditionalInfo() {
        return additionalInfoInput.getAttribute("value");
    }

    public EditStudentPage setAdditionalInfo(String info) {
        if (info != null) {
            additionalInfoInput.fill(info);
        }
        return this;
    }

    public String getPageTitle() {
        return pageTitle.textContent();
    }

    public void clickSubmit() {
        submitButton.click();
    }

    public void clickCancel() {
        cancelButton.click();
    }

//    public StudentListPage saveStudent() {
//        clickSubmit();
//        return new StudentListPage(page);
//    }
//
//    public StudentListPage cancelEdit() {
//        clickCancel();
//        return new StudentListPage(page);
//    }

    // Проверочные методы

    public boolean isPageLoaded() {
        try {
            page.waitForSelector("h1:has-text('Редактировать ученика')",
                    new Page.WaitForSelectorOptions().setTimeout(5000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public EditStudentPage waitForPageLoad() {
        page.waitForSelector("h1:has-text('Редактировать ученика')");
        return this;
    }

    // Получение всех данных

    public Map<String, String> getStudentData() {
        Map<String, String> data = new HashMap<>();
        data.put("last_name", getLastName());
        data.put("first_name", getFirstName());
        data.put("middle_name", getMiddleName());
        data.put("contacts", getContacts());
        data.put("birthday", getBirthday());
        data.put("lessons_count", getLessonsCount());
        data.put("additional_info", getAdditionalInfo());
        return data;
    }

    public EditStudentPage fillStudentForm(Student studentData) {

        if (!studentData.getLastName().isEmpty()) {
            setLastName(studentData.getLastName());
        }
        if (!studentData.getFirstName().isEmpty()) {
            setFirstName(studentData.getFirstName());
        }
        if (!studentData.getMiddleName().isEmpty()) {
            setMiddleName(studentData.getMiddleName());
        }
        if (!studentData.getContacts().isEmpty()) {
            setContacts(studentData.getContacts());
        }
        if (!studentData.getBirthday().isEmpty()) {
            setBirthday(DataUtils.convertDate(studentData.getBirthday(), "dd.MM.yyyy", "yyyy-MM-dd"));
        }
        setLessonsCount(studentData.getLessonsCount());
        if (studentData.getAdditionalInfo() != null) {
            setAdditionalInfo(studentData.getAdditionalInfo());
        }
        return this;
    }

    // Очистка полей

    public EditStudentPage clearForm() {
        lastNameInput.clear();
        firstNameInput.clear();
        middleNameInput.clear();
        contactsInput.clear();
        birthdayInput.clear();
        lessonsCountInput.clear();
        additionalInfoInput.clear();
        return this;
    }
}
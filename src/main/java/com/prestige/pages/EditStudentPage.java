package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.prestige.models.Student;
import com.prestige.utils.DataUtils;
import io.qameta.allure.Step;

import java.util.HashMap;
import java.util.Map;

public class EditStudentPage extends BaseStudentPage<EditStudentPage> {

    private final Locator cancelButton;

    public EditStudentPage(Page page) {
        super(page);
        this.cancelButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("❌ Отмена"));
    }

    @Step("Ожидание страницы редактирования ученика")
    public EditStudentPage waitForPageLoad() {
        page.waitForSelector("h1:has-text('Редактировать ученика')");
        return this;
    }

    public String getLessonsCount() {
        return page.inputValue(lessonsCountInput);
    }

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

    @Step("Заполнить форму редактирования ученика")
    public EditStudentPage fillStudentForm(Student studentData) {
        if (!studentData.getLastName().isEmpty()) {
            fillLastName(studentData.getLastName());
        }
        if (!studentData.getFirstName().isEmpty()) {
            fillFirstName(studentData.getFirstName());
        }
        if (!studentData.getMiddleName().isEmpty()) {
            fillMiddleName(studentData.getMiddleName());
        }
        if (!studentData.getContacts().isEmpty()) {
            fillContacts(studentData.getContacts());
        }
        if (!studentData.getBirthday().isEmpty()) {
            fillBirthday(DataUtils.convertDate(studentData.getBirthday(), "dd.MM.yyyy", "yyyy-MM-dd"));
        }
        fillLessonsCount(studentData.getLessonsCount());
        if (studentData.getAdditionalInfo() != null) {
            fillAdditionalInfo(studentData.getAdditionalInfo());
        }
        return this;
    }

    @Step("Сохранить изменения ученика")
    public void clickSubmit() {
        page.click(submitButton);
    }

    @Step("Отменить редактирование ученика")
    public void clickCancel() {
        cancelButton.click();
    }
}

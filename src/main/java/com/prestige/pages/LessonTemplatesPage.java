package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.LessonTemplate;

public class LessonTemplatesPage extends BasePage {

    private final String pageTitle = "h1";
    private final String addTemplateButton = "a[title='Добавить шаблон']";
    private final String addTemplateImage = "a[title='Добавить шаблон'] img";
    private final String sectionTitle = "table tr td.section_title";
    private final String editButton = "a[title='Редактировать']";
    private final String deleteButton = "a.delete-btn";

    public LessonTemplatesPage(Page page) {
        super(page);
    }

    public LessonTemplatesPage navigateTo() {
        page.navigate("/lesson_templates");
        waitForPageLoad();
        return this;
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    public AddLessonTemplatePage clickAddLessonTemplate() {
        page.click(addTemplateButton);
        return new AddLessonTemplatePage(page);
    }

    public boolean isAddTemplateButtonVisible() {
        return page.isVisible(addTemplateImage);
    }

    public int getTemplatesCount() {
        return page.locator(sectionTitle).count();
    }

    public String getTemplateName(int index) {
        Locator titles = page.locator(sectionTitle);
        if (index < titles.count()) {
            return titles.nth(index).textContent().trim();
        }
        return null;
    }

    public boolean isLessonTemplateExists(LessonTemplate lessonTemplate) {
        int rowIndex = findTemplateRowIndex(lessonTemplate.getTemplateName());
        if (rowIndex == -1) return false;

        String titleText = page.locator("table tr").nth(rowIndex).textContent().trim();

        boolean coachInTitle = true;
        if (lessonTemplate.getCoachId() > 0) {
            coachInTitle = titleText.contains(lessonTemplate.getTemplateName());
        }

        boolean studentsInRow = true;
        if (lessonTemplate.getStudentsIds() != null && !lessonTemplate.getStudentsIds().isEmpty()) {
            Locator dataRow = page.locator("table tr").nth(rowIndex + 1);
            String dataText = dataRow.textContent().trim();
            int expectedCount = lessonTemplate.getStudentsIds().split(",").length;
            int actualCount = dataText.split("\\d+\\.").length - 1;
            studentsInRow = !dataText.isEmpty() && actualCount == expectedCount;
        }

        return coachInTitle && studentsInRow;
    }

    public boolean isTemplateExists(String templateName) {
        Locator titles = page.locator(sectionTitle);
        for (int i = 0; i < titles.count(); i++) {
            if (titles.nth(i).textContent().trim().contains(templateName)) {
                return true;
            }
        }
        return false;
    }

    public EditLessonTemplatePage clickEditTemplate(String templateName) {
        int rowIndex = findTemplateRowIndex(templateName);
        if (rowIndex != -1) {
            Locator dataRow = page.locator("table tr").nth(rowIndex + 1);
            dataRow.locator(editButton).click();
            return new EditLessonTemplatePage(page);
        }
        throw new RuntimeException("Шаблон не найден: " + templateName);
    }

    public LessonTemplatesPage deleteTemplate(String templateName) {
        int rowIndex = findTemplateRowIndex(templateName);
        if (rowIndex != -1) {
            Locator dataRow = page.locator("table tr").nth(rowIndex + 1);
            dataRow.locator(deleteButton).click();
            confirmDeleteModal();
            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Шаблон не найден: " + templateName);
    }

    private int findTemplateRowIndex(String templateName) {
        Locator rows = page.locator("table tr");
        for (int i = 0; i < rows.count(); i++) {
            String text = rows.nth(i).textContent().trim();
            if (text.contains(templateName)) {
                return i;
            }
        }
        return -1;
    }

    public LessonTemplatesPage waitForTemplateToAppear(String templateName, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;
        while (System.currentTimeMillis() - startTime < timeout) {
            if (isTemplateExists(templateName)) {
                return this;
            }
            page.waitForTimeout(500);
        }
        throw new RuntimeException("Шаблон не появился за " + timeoutSeconds + " секунд: " + templateName);
    }

    public LessonTemplatesPage waitForTemplateToDisappear(String templateName, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;
        while (System.currentTimeMillis() - startTime < timeout) {
            if (!isTemplateExists(templateName)) {
                return this;
            }
            page.waitForTimeout(500);
        }
        throw new RuntimeException("Шаблон не исчез за " + timeoutSeconds + " секунд: " + templateName);
    }
}

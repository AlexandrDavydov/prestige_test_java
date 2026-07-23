package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.prestige.models.Lesson;
import static com.prestige.utils.StepHelper.step;
import com.prestige.models.LessonTemplate;
import com.prestige.models.Student;

import java.util.ArrayList;
import java.util.List;

public class LessonsPage extends BasePage {

    private final String pageTitle = "h1";

    private final String addLessonButton = "a[title='Добавить занятие']";
    private final String addLessonImage = "a[title='Добавить занятие'] img";

    private final String statusFilter = "#status_filter";
    private final String filterForm = "form[method='get']";
    private final String statusFilterOption = "#status_filter option";

    private final String lessonTemplateSelect = "#lesson_template_select";
    private final String addFromTemplateButton = "a.btn[onclick*='addFromTemplate']";

    private final String lessonRows = "table tr.table-title";
    private final String lessonTitleRow = "tr.table-title";
    private final String lessonInfoRow = "tr.table-title + tr";
    private final String lessonStudentsRow = "tr:has(th:has-text('Ученики'))";
    private final String lessonActionsRow = "tr:has(td:has(a[title='Состоялось']))";

    private final String lessonDate = "tr.table-title td i";
    private final String lessonName = "td";
    private final String lessonCoach = "span:has-text('тренер:') + *";
    private final String lessonStatus = "span:has-text('статус:') + *";
    private final String lessonStudents = "td:first-child";
    private final String lessonActions = "td:last-child";

    private final String lessonDoneButton = "a[title='Состоялось']";
    private final String lessonEditButton = "a[title='Редактировать']";
    private final String lessonDownloadTableButton = "a[onclick*='downloadFile_docx']";
    private final String lessonDownloadImageButton = "a[onclick*='downloadFile_png']";
    private final String lessonDeleteButton = "a.delete-btn";

    private final String paginationContainer = ".pagination";
    private final String paginationLinks = ".pagination a";
    private final String paginationCurrent = ".pagination .current";
    private final String paginationPrev = ".pagination .prev";
    private final String paginationNext = ".pagination .next";

    public LessonsPage(Page page) {
        super(page);
    }

    public LessonsPage navigateTo() {
        return step("Переход на страницу занятий", () -> {
            page.navigate("/lessons");
            waitForPageLoad();
            return this;
        });
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    // ============ ДОБАВЛЕНИЕ ЗАНЯТИЯ ============

    public AddLessonPage clickAddLesson() {
        return step("Нажать кнопку добавления занятия", () -> {
            page.click(addLessonButton);
            return new AddLessonPage(page);
        });
    }

    public boolean isAddLessonButtonVisible() {
        return page.isVisible(addLessonImage);
    }

    // ============ ФИЛЬТРЫ ============

    public LessonsPage filterByStatus(String status) {
        return step("Фильтр по статусу: " + status, () -> {
            page.selectOption(statusFilter, status);
            waitForPageLoad();
            return this;
        });
    }

    public String getCurrentFilterStatus() {
        return page.locator(statusFilter).getAttribute("value");
    }

    public List<String> getAvailableStatuses() {
        return page.locator(statusFilterOption).allTextContents();
    }

    public boolean isStatusFilterActive() {
        String value = page.locator(statusFilter).getAttribute("value");
        return value != null && !value.isEmpty();
    }

    // ============ ШАБЛОНЫ ============

    public List<String> getAvailableTemplates() {
        return page.locator(lessonTemplateSelect + " option").allTextContents();
    }

    public List<LessonTemplate> getLessonTemplates() {
        List<LessonTemplate> templates = new ArrayList<>();
        Locator options = page.locator(lessonTemplateSelect + " option");

        for (int i = 0; i < options.count(); i++) {
            Locator option = options.nth(i);
            String value = option.getAttribute("value");
            String name = option.textContent();

            if (value != null && !value.isEmpty()) {
                LessonTemplate template = new LessonTemplate();
                template.setId(Integer.parseInt(value));
                template.setTemplateName(name);
                templates.add(template);
            }
        }

        return templates;
    }

    // ============ ПОЛУЧЕНИЕ ЗАНЯТИЙ ============

    public int getLessonsCount() {
        return page.locator(lessonRows).count();
    }

    private Locator getInfoRowForLesson(int index) {
        Locator titleRow = page.locator(lessonRows).nth(index);
        return titleRow.locator("xpath=following-sibling::tr[1]");
    }

    private Locator getStudentsRowForLesson(int index) {
        Locator titleRow = page.locator(lessonRows).nth(index);
        return titleRow.locator("xpath=following-sibling::tr[2]");
    }

    private Locator getActionsRowForLesson(int index) {
        Locator titleRow = page.locator(lessonRows).nth(index);
        return titleRow.locator("xpath=following-sibling::tr[3]");
    }

    private List<Student> getStudentsFromRow(Locator row) {
        List<Student> students = new ArrayList<>();
        String studentsText = row.locator(lessonStudents).textContent().trim();

        if (studentsText != null && !studentsText.isEmpty()) {
            String[] names = studentsText.split("<br>");
            for (String name : names) {
                String[] parts = name.trim().split(" ");
                if (parts.length >= 2) {
                    Student student = Student.builder()
                                    .lastName(parts[0])
                                    .firstName(parts[1]).build();

                    if (parts.length > 2) {
                        student.setMiddleName(parts[2]);
                    }
                    students.add(student);
                }
            }
        }

        return students;
    }

    // ============ ДЕЙСТВИЯ С ЗАНЯТИЯМИ ============

    public LessonsPage markLessonAsDone(String lessonName) {
        return step("Отметить занятие как состоявшееся: " + lessonName, () -> {
            int index = findLessonIndex(lessonName);
            if (index >= 0) {
                getActionsRowForLesson(index).locator(lessonDoneButton).click();
                waitForPageLoad();
                return this;
            }
            throw new RuntimeException("Занятие не найдено: " + lessonName);
        });
    }

    public LessonsPage markLessonAsDoneAtIndex(int index) {
        return step("Отметить занятие как состоявшееся по индексу", () -> {
            getActionsRowForLesson(index).locator(lessonDoneButton).click();
            waitForPageLoad();
            return this;
        });
    }

    public EditLessonPage clickEditLesson(String lessonName) {
        return step("Редактировать занятие: " + lessonName, () -> {
            int index = findLessonIndex(lessonName);
            if (index >= 0) {
                getActionsRowForLesson(index).locator(lessonEditButton).click();
                return new EditLessonPage(page);
            }
            throw new RuntimeException("Занятие не найдено: " + lessonName);
        });
    }

    public void downloadLessonAsDocx(String lessonName) {
        step("Скачать занятие как DOCX: " + lessonName, () -> {
            int index = findLessonIndex(lessonName);
            if (index >= 0) {
                getActionsRowForLesson(index).locator(lessonDownloadTableButton).click();
                page.waitForTimeout(2000);
            } else {
                throw new RuntimeException("Занятие не найдено: " + lessonName);
            }
        });
    }

    public void downloadLessonAsPng(String lessonName) {
        step("Скачать занятие как PNG: " + lessonName, () -> {
            int index = findLessonIndex(lessonName);
            if (index >= 0) {
                getActionsRowForLesson(index).locator(lessonDownloadImageButton).click();
                page.waitForTimeout(2000);
            } else {
                throw new RuntimeException("Занятие не найдено: " + lessonName);
            }
        });
    }

    public LessonsPage deleteLesson(String lessonName) {
        return step("Удалить занятие: " + lessonName, () -> {
            int index = findLessonIndex(lessonName);
            if (index >= 0) {
                getActionsRowForLesson(index).locator(lessonDeleteButton).click();
                confirmDeleteModal();
                waitForPageLoad();
                return this;
            }
            throw new RuntimeException("Занятие не найдено: " + lessonName);
        });
    }

    private int findLessonIndex(String targetLessonName) {
        Locator titleRows = page.locator(lessonRows);

        for (int i = 0; i < titleRows.count(); i++) {
            String name = titleRows.nth(i).textContent().trim();
            if (name.contains(targetLessonName)) {
                return i;
            }
        }

        return -1;
    }

    public boolean isLessonExists(Lesson lesson) {
        return step("Проверить существование занятия: " + lesson.getLessonName(), () -> {
            long deadline = System.currentTimeMillis() + 5000;
            while (System.currentTimeMillis() < deadline) {
                try {
                    if (findLessonIndex(lesson.getLessonName()) >= 0) return true;
                } catch (PlaywrightException e) {
                    // игнорируем — страница в процессе навигации
                }
                page.waitForTimeout(300);
            }
            return false;
        });
    }

    // ============ ПАГИНАЦИЯ ============

    public LessonsPage goToPage(int pageNumber) {
        return step("Перейти на страницу пагинации: " + pageNumber, () -> {
            Locator link = page.locator(paginationLinks).filter(new Locator.FilterOptions()
                    .setHasText(String.valueOf(pageNumber)));
            if (link.count() > 0) {
                link.click();
                waitForPageLoad();
                return this;
            }
            throw new RuntimeException("Страница " + pageNumber + " не найдена");
        });
    }

    public LessonsPage goToNextPage() {
        return step("Следующая страница", () -> {
            page.locator(paginationNext).click();
            waitForPageLoad();
            return this;
        });
    }

    public LessonsPage goToPrevPage() {
        return step("Предыдущая страница", () -> {
            page.locator(paginationPrev).click();
            waitForPageLoad();
            return this;
        });
    }

    public int getCurrentPage() {
        String currentText = page.locator(paginationCurrent).textContent();
        return Integer.parseInt(currentText.trim());
    }

    public int getTotalPages() {
        Locator links = page.locator(paginationLinks);
        if (links.count() == 0) {
            return 1;
        }

        String lastPageText = links.last().textContent().trim();
        try {
            return Integer.parseInt(lastPageText);
        } catch (NumberFormatException e) {
            return links.count();
        }
    }

    public boolean hasNextPage() {
        return page.isVisible(paginationNext) &&
                !page.locator(paginationNext).getAttribute("class").contains("disabled");
    }

    public boolean hasPrevPage() {
        return page.isVisible(paginationPrev) &&
                !page.locator(paginationPrev).getAttribute("class").contains("disabled");
    }
}

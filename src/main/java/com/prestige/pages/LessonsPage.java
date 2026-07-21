package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.Lesson;
import com.prestige.models.LessonTemplate;
import com.prestige.models.Student;

import java.util.ArrayList;
import java.util.List;

public class LessonsPage extends BasePage {

    // Заголовок страницы
    private final String pageTitle = "h1";

    // Кнопка добавления занятия
    private final String addLessonButton = "a[title='Добавить занятие']";
    private final String addLessonImage = "a[title='Добавить занятие'] img";

    // Фильтр по статусу
    private final String statusFilter = "#status_filter";
    private final String filterForm = "form[method='get']";
    private final String statusFilterOption = "#status_filter option";

    // Шаблоны
    private final String lessonTemplateSelect = "#lesson_template_select";
    private final String addFromTemplateButton = "a.btn[onclick*='addFromTemplate']";

    // Таблица занятий
    private final String lessonRows = "table tr.table-title";
    private final String lessonTitleRow = "tr.table-title";
    private final String lessonInfoRow = "tr.table-title + tr";
    private final String lessonStudentsRow = "tr:has(th:has-text('Ученики'))";
    private final String lessonActionsRow = "tr:has(td:has(a[title='Состоялось']))";

    // Селекторы внутри строки занятия
    private final String lessonDate = "tr.table-title td i";
    private final String lessonName = "td";
    private final String lessonCoach = "span:has-text('тренер:') + *";
    private final String lessonStatus = "span:has-text('статус:') + *";
    private final String lessonStudents = "td:first-child";
    private final String lessonActions = "td:last-child";

    // Кнопки действий для занятия
    private final String lessonDoneButton = "a[title='Состоялось']";
    private final String lessonEditButton = "a[title='Редактировать']";
    private final String lessonDownloadTableButton = "a[onclick*='downloadFile_docx']";
    private final String lessonDownloadImageButton = "a[onclick*='downloadFile_png']";
    private final String lessonDeleteButton = "a.delete-btn";

    // Пагинация
    private final String paginationContainer = ".pagination";
    private final String paginationLinks = ".pagination a";
    private final String paginationCurrent = ".pagination .current";
    private final String paginationPrev = ".pagination .prev";
    private final String paginationNext = ".pagination .next";

    public LessonsPage(Page page) {
        super(page);
    }

    /**
     * Перейти на страницу занятий
     */
    public LessonsPage navigateTo() {
        page.navigate("/lessons");
        waitForPageLoad();
        return this;
    }

    /**
     * Получить заголовок страницы
     */
    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    // ============ ДОБАВЛЕНИЕ ЗАНЯТИЯ ============

    public AddLessonPage clickAddLesson() {
        page.click(addLessonButton);
        return new AddLessonPage(page);
    }

    /**
     * Проверить, видна ли кнопка добавления
     */
    public boolean isAddLessonButtonVisible() {
        return page.isVisible(addLessonImage);
    }

    // ============ ФИЛЬТРЫ ============

    /**
     * Фильтровать занятия по статусу
     */
    public LessonsPage filterByStatus(String status) {
        page.selectOption(statusFilter, status);
        page.click(filterForm + " input[type='hidden']"); // Trigger submit
        waitForPageLoad();
        return this;
    }

    /**
     * Получить текущий выбранный статус фильтра
     */
    public String getCurrentFilterStatus() {
        return page.locator(statusFilter).getAttribute("value");
    }

    /**
     * Получить все доступные статусы для фильтрации
     */
    public List<String> getAvailableStatuses() {
        return page.locator(statusFilterOption).allTextContents();
    }

    /**
     * Проверить, активен ли фильтр по статусу
     */
    public boolean isStatusFilterActive() {
        String value = page.locator(statusFilter).getAttribute("value");
        return value != null && !value.isEmpty();
    }

    // ============ ШАБЛОНЫ ============

    /**
     * Добавить занятие из шаблона
     */
//    public AddLessonPage addFromTemplate(String templateName) {
//        page.selectOption(lessonTemplateSelect, templateName);
//        page.click(addFromTemplateButton);
//        return new AddLessonPage(page);
//    }

    /**
     * Добавить занятие из шаблона по ID
     */
//    public AddLessonPage addFromTemplateById(String templateId) {
//        page.selectOption(lessonTemplateSelect, templateId);
//        page.click(addFromTemplateButton);
//        return new AddLessonPage(page);
//    }

    /**
     * Получить все доступные шаблоны
     */
    public List<String> getAvailableTemplates() {
        return page.locator(lessonTemplateSelect + " option").allTextContents();
    }

    /**
     * Получить список шаблонов с их ID
     */
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

    /**
     * Получить количество занятий на странице
     */
    public int getLessonsCount() {
        return page.locator(lessonRows).count();
    }

    /**
     * Получить все занятия
     */
//    public List<Lesson> getAllLessons() {
//        List<Lesson> lessons = new ArrayList<>();
//        Locator titleRows = page.locator(lessonRows);
//
//        for (int i = 0; i < titleRows.count(); i++) {
//            Lesson lesson = getLessonAtIndex(i);
//            lessons.add(lesson);
//        }
//
//        return lessons;
//    }

    /**
     * Получить занятие по индексу
     */
//    public Lesson getLessonAtIndex(int index) {
//        Locator titleRow = page.locator(lessonRows).nth(index);
//        Locator infoRow = getInfoRowForLesson(index);
//        Locator studentsRow = getStudentsRowForLesson(index);
//
//        // Парсим дату и название из title row
//        String titleText = titleRow.locator(lessonDate).textContent().trim();
//        String lessonName = titleRow.locator(lessonName).textContent().trim();
//        // Убираем дату из названия
//        if (lessonName.contains("|")) {
//            String[] parts = lessonName.split("\\|");
//            lessonName = parts.length > 1 ? parts[1].trim() : lessonName;
//        }
//
//        // Парсим информацию из info row
//        String coachName = infoRow.locator("span:has-text('тренер:') + *").textContent().trim();
//        String status = infoRow.locator("span:has-text('статус:') + *").textContent().trim();
//
//        // Парсим учеников
//        List<Student> students = getStudentsFromRow(studentsRow);
//
//        Lesson lesson = new Lesson();
//        lesson.setLessonName(lessonName);
//        lesson.setDate(titleText);
//        lesson.setCoachName(coachName);
//        lesson.setStatus(status);
//        lesson.setStudents(students);
//
//        return lesson;
//    }

    /**
     * Получить строку информации для занятия
     */
    private Locator getInfoRowForLesson(int index) {
        // Находим строку с информацией после title row
        Locator titleRow = page.locator(lessonRows).nth(index);
        return titleRow.locator("xpath=following-sibling::tr[1]");
    }

    /**
     * Получить строку с учениками для занятия
     */
    private Locator getStudentsRowForLesson(int index) {
        Locator titleRow = page.locator(lessonRows).nth(index);
        return titleRow.locator("xpath=following-sibling::tr[2]");
    }

    /**
     * Получить строку с действиями для занятия
     */
    private Locator getActionsRowForLesson(int index) {
        Locator titleRow = page.locator(lessonRows).nth(index);
        return titleRow.locator("xpath=following-sibling::tr[3]");
    }

    /**
     * Получить список учеников из строки
     */
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

    /**
     * Найти занятие по названию
     */
//    public Lesson getLessonByName(String lessonName) {
//        Locator titleRows = page.locator(lessonRows);
//
//        for (int i = 0; i < titleRows.count(); i++) {
//            String name = titleRows.nth(i).locator(lessonName).textContent().trim();
//            if (name.contains(lessonName)) {
//                return getLessonAtIndex(i);
//            }
//        }
//
//        return null;
//    }

    /**
     * Найти занятие по дате
     */
//    public Lesson getLessonByDate(String date) {
//        Locator titleRows = page.locator(lessonRows);
//
//        for (int i = 0; i < titleRows.count(); i++) {
//            String lessonDate = titleRows.nth(i).locator(lessonDate).textContent().trim();
//            if (lessonDate.equals(date)) {
//                return getLessonAtIndex(i);
//            }
//        }
//
//        return null;
//    }

    // ============ ДЕЙСТВИЯ С ЗАНЯТИЯМИ ============

    /**
     * Отметить занятие как состоявшееся
     */
    public LessonsPage markLessonAsDone(String lessonName) {
        int index = findLessonIndex(lessonName);
        if (index >= 0) {
            getActionsRowForLesson(index).locator(lessonDoneButton).click();
            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Занятие не найдено: " + lessonName);
    }

    /**
     * Отметить занятие как состоявшееся по индексу
     */
    public LessonsPage markLessonAsDoneAtIndex(int index) {
        getActionsRowForLesson(index).locator(lessonDoneButton).click();
        waitForPageLoad();
        return this;
    }

    /**
     * Редактировать занятие
     */
    public EditLessonPage clickEditLesson(String lessonName) {
        int index = findLessonIndex(lessonName);
        if (index >= 0) {
            getActionsRowForLesson(index).locator(lessonEditButton).click();
            return new EditLessonPage(page);
        }
        throw new RuntimeException("Занятие не найдено: " + lessonName);
    }

    /**
     * Редактировать занятие по индексу
     */
//    public EditLessonPage editLessonAtIndex(int index) {
//        getActionsRowForLesson(index).locator(lessonEditButton).click();
//        return new EditLessonPage(page);
//    }

    /**
     * Скачать занятие как таблицу (DOCX)
     */
    public void downloadLessonAsDocx(String lessonName) {
        int index = findLessonIndex(lessonName);
        if (index >= 0) {
            getActionsRowForLesson(index).locator(lessonDownloadTableButton).click();
            // Ждем загрузки файла
            page.waitForTimeout(2000);
        } else {
            throw new RuntimeException("Занятие не найдено: " + lessonName);
        }
    }

    /**
     * Скачать занятие как картинку (PNG)
     */
    public void downloadLessonAsPng(String lessonName) {
        int index = findLessonIndex(lessonName);
        if (index >= 0) {
            getActionsRowForLesson(index).locator(lessonDownloadImageButton).click();
            page.waitForTimeout(2000);
        } else {
            throw new RuntimeException("Занятие не найдено: " + lessonName);
        }
    }

    /**
     * Удалить занятие (с подтверждением)
     */
    public LessonsPage deleteLesson(String lessonName) {
        int index = findLessonIndex(lessonName);
        if (index >= 0) {
            // Кликаем на кнопку удаления
            getActionsRowForLesson(index).locator(lessonDeleteButton).click();

            // Подтверждаем удаление в модальном окне
            confirmDeleteModal();

            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Занятие не найдено: " + lessonName);
    }

    /**
     * Найти индекс занятия по названию
     */
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
        return findLessonIndex(lesson.getLessonName()) >= 0;
    }

    /**
     * Получить статус занятия
     */
//    public String getLessonStatus(String lessonName) {
//        Lesson lesson = getLessonByName(lessonName);
//        return lesson != null ? lesson.getStatus() : null;
//    }

    /**
     * Получить тренера занятия
     */
//    public String getLessonCoach(String lessonName) {
//        Lesson lesson = getLessonByName(lessonName);
//        return lesson != null ? lesson.getCoachName() : null;
//    }

    /**
     * Получить учеников занятия
     */
//    public List<Student> getLessonStudents(String lessonName) {
//        Lesson lesson = getLessonByName(lessonName);
//        return lesson != null ? lesson.getStudents() : new ArrayList<>();
//    }

    // ============ ПАГИНАЦИЯ ============

    /**
     * Перейти на страницу пагинации
     */
    public LessonsPage goToPage(int pageNumber) {
        Locator link = page.locator(paginationLinks).filter(new Locator.FilterOptions()
                .setHasText(String.valueOf(pageNumber)));
        if (link.count() > 0) {
            link.click();
            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Страница " + pageNumber + " не найдена");
    }

    /**
     * Перейти на следующую страницу
     */
    public LessonsPage goToNextPage() {
        page.locator(paginationNext).click();
        waitForPageLoad();
        return this;
    }

    /**
     * Перейти на предыдущую страницу
     */
    public LessonsPage goToPrevPage() {
        page.locator(paginationPrev).click();
        waitForPageLoad();
        return this;
    }

    /**
     * Получить текущий номер страницы
     */
    public int getCurrentPage() {
        String currentText = page.locator(paginationCurrent).textContent();
        return Integer.parseInt(currentText.trim());
    }

    /**
     * Получить общее количество страниц
     */
    public int getTotalPages() {
        Locator links = page.locator(paginationLinks);
        if (links.count() == 0) {
            return 1;
        }

        // Последняя ссылка - это последняя страница
        String lastPageText = links.last().textContent().trim();
        try {
            return Integer.parseInt(lastPageText);
        } catch (NumberFormatException e) {
            return links.count();
        }
    }

    /**
     * Проверить, есть ли следующая страница
     */
    public boolean hasNextPage() {
        return page.isVisible(paginationNext) &&
                !page.locator(paginationNext).getAttribute("class").contains("disabled");
    }

    /**
     * Проверить, есть ли предыдущая страница
     */
    public boolean hasPrevPage() {
        return page.isVisible(paginationPrev) &&
                !page.locator(paginationPrev).getAttribute("class").contains("disabled");
    }

    // ============ ВАЛИДАЦИИ ============

    /**
     * Проверить, что занятие имеет статус "Запланировано"
     */
//    public boolean isLessonPlanned(String lessonName) {
//        String status = getLessonStatus(lessonName);
//        return "Запланировано".equals(status);
//    }

    /**
     * Проверить, что занятие имеет статус "Состоялось"
     */
//    public boolean isLessonCompleted(String lessonName) {
//        String status = getLessonStatus(lessonName);
//        return "Состоялось".equals(status);
//    }

    /**
     * Проверить, что у занятия есть ученики
     */
//    public boolean hasStudents(String lessonName) {
//        List<Student> students = getLessonStudents(lessonName);
//        return !students.isEmpty();
//    }

    /**
     * Проверить, что ученик присутствует на занятии
     */
//    public boolean isStudentInLesson(String lessonName, String studentName) {
//        List<Student> students = getLessonStudents(lessonName);
//        return students.stream().anyMatch(s -> s.getFullName().equals(studentName));
//    }
}
package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.Student;

import java.util.ArrayList;
import java.util.List;

public class DashboardPage extends BasePage {

    // Заголовок страницы
    private final String pageTitle = "h1";

    // Виджет дней рождения
    private final String birthdayWidget = ".widget";
    private final String widgetHeader = ".widget-header";
    private final String widgetBody = ".widget-body";

    // Колонки с днями рождения
    private final String birthdayYesterdayColumn = ".birthday-column:first-child";
    private final String birthdayTodayColumn = ".birthday-column.today";
    private final String birthdayTomorrowColumn = ".birthday-column:last-child";

    // Содержимое колонок
    private final String columnTitle = "h4";
    private final String studentList = "ul";
    private final String studentItems = "ul li";
    private final String emptyMessage = ".empty";

    public DashboardPage(Page page) {
        super(page);
    }

    /**
     * Перейти на главную страницу
     */
    public DashboardPage navigateTo() {
        page.navigate("/");
        waitForPageLoad();
        return this;
    }

    /**
     * Проверить, что страница загружена
     */
    @Override
    public boolean isPageLoaded() {
        return page.isVisible(pageTitle) &&
                page.textContent(pageTitle).contains("Главная") &&
                page.isVisible(birthdayWidget);
    }

    /**
     * Получить заголовок страницы
     */
    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    // ============ ВИДЖЕТ ДНЕЙ РОЖДЕНИЯ ============

    /**
     * Проверить, что виджет дней рождения отображается
     */
    public boolean isBirthdayWidgetVisible() {
        return page.isVisible(birthdayWidget);
    }

    /**
     * Получить заголовок виджета
     */
    public String getWidgetTitle() {
        return page.textContent(widgetHeader);
    }

    /**
     * Проверить, что виджет содержит все три колонки
     */
    public boolean hasAllBirthdayColumns() {
        return page.isVisible(birthdayYesterdayColumn) &&
                page.isVisible(birthdayTodayColumn) &&
                page.isVisible(birthdayTomorrowColumn);
    }

    // ============ ДНИ РОЖДЕНИЯ ВЧЕРА ============

    /**
     * Получить заголовок колонки "Вчера"
     */
    public String getYesterdayColumnTitle() {
        return page.locator(birthdayYesterdayColumn + " " + columnTitle).textContent();
    }

    /**
     * Получить список учеников с днем рождения вчера
     */
    public List<Student> getBirthdaysYesterday() {
        return getStudentsFromColumn(birthdayYesterdayColumn);
    }

    /**
     * Получить имена учеников с днем рождения вчера
     */
    public List<String> getBirthdayNamesYesterday() {
        return getStudentNamesFromColumn(birthdayYesterdayColumn);
    }

    /**
     * Проверить, есть ли ученики с днем рождения вчера
     */
    public boolean hasBirthdaysYesterday() {
        return hasStudentsInColumn(birthdayYesterdayColumn);
    }

    /**
     * Получить количество учеников с днем рождения вчера
     */
    public int getBirthdaysYesterdayCount() {
        return getStudentCountInColumn(birthdayYesterdayColumn);
    }

    // ============ ДНИ РОЖДЕНИЯ СЕГОДНЯ ============

    /**
     * Получить заголовок колонки "Сегодня"
     */
    public String getTodayColumnTitle() {
        return page.locator(birthdayTodayColumn + " " + columnTitle).textContent();
    }

    /**
     * Получить список учеников с днем рождения сегодня
     */
    public List<Student> getBirthdaysToday() {
        return getStudentsFromColumn(birthdayTodayColumn);
    }

    /**
     * Получить имена учеников с днем рождения сегодня
     */
    public List<String> getBirthdayNamesToday() {
        return getStudentNamesFromColumn(birthdayTodayColumn);
    }

    /**
     * Проверить, есть ли ученики с днем рождения сегодня
     */
    public boolean hasBirthdaysToday() {
        return hasStudentsInColumn(birthdayTodayColumn);
    }

    /**
     * Получить количество учеников с днем рождения сегодня
     */
    public int getBirthdaysTodayCount() {
        return getStudentCountInColumn(birthdayTodayColumn);
    }

    // ============ ДНИ РОЖДЕНИЯ ЗАВТРА ============

    /**
     * Получить заголовок колонки "Завтра"
     */
    public String getTomorrowColumnTitle() {
        return page.locator(birthdayTomorrowColumn + " " + columnTitle).textContent();
    }

    /**
     * Получить список учеников с днем рождения завтра
     */
    public List<Student> getBirthdaysTomorrow() {
        return getStudentsFromColumn(birthdayTomorrowColumn);
    }

    /**
     * Получить имена учеников с днем рождения завтра
     */
    public List<String> getBirthdayNamesTomorrow() {
        return getStudentNamesFromColumn(birthdayTomorrowColumn);
    }

    /**
     * Проверить, есть ли ученики с днем рождения завтра
     */
    public boolean hasBirthdaysTomorrow() {
        return hasStudentsInColumn(birthdayTomorrowColumn);
    }

    /**
     * Получить количество учеников с днем рождения завтра
     */
    public int getBirthdaysTomorrowCount() {
        return getStudentCountInColumn(birthdayTomorrowColumn);
    }

    // ============ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ КОЛОНОК ============

    /**
     * Получить учеников из колонки
     */
    private List<Student> getStudentsFromColumn(String columnSelector) {
        List<Student> students = new ArrayList<>();
        Locator items = page.locator(columnSelector + " " + studentItems);

        for (int i = 0; i < items.count(); i++) {
            String fullName = items.nth(i).textContent().trim();
            Student student = parseStudentName(fullName);
            students.add(student);
        }

        return students;
    }

    /**
     * Получить имена учеников из колонки
     */
    private List<String> getStudentNamesFromColumn(String columnSelector) {
        List<String> names = new ArrayList<>();
        Locator items = page.locator(columnSelector + " " + studentItems);

        for (int i = 0; i < items.count(); i++) {
            names.add(items.nth(i).textContent().trim());
        }

        return names;
    }

    /**
     * Проверить, есть ли ученики в колонке
     */
    private boolean hasStudentsInColumn(String columnSelector) {
        // Если есть сообщение "Нет", значит учеников нет
        if (page.isVisible(columnSelector + " " + emptyMessage)) {
            return false;
        }
        return page.locator(columnSelector + " " + studentItems).count() > 0;
    }

    /**
     * Получить количество учеников в колонке
     */
    private int getStudentCountInColumn(String columnSelector) {
        if (page.isVisible(columnSelector + " " + emptyMessage)) {
            return 0;
        }
        return page.locator(columnSelector + " " + studentItems).count();
    }

    /**
     * Получить текст сообщения "Нет" из колонки
     */
    public String getEmptyMessageForColumn(String column) {
        String columnSelector = getColumnSelector(column);
        if (page.isVisible(columnSelector + " " + emptyMessage)) {
            return page.textContent(columnSelector + " " + emptyMessage);
        }
        return null;
    }

    /**
     * Получить селектор колонки по названию
     */
    private String getColumnSelector(String column) {
        switch (column.toLowerCase()) {
            case "вчера":
            case "yesterday":
                return birthdayYesterdayColumn;
            case "сегодня":
            case "today":
                return birthdayTodayColumn;
            case "завтра":
            case "tomorrow":
                return birthdayTomorrowColumn;
            default:
                throw new IllegalArgumentException("Неизвестная колонка: " + column);
        }
    }

    /**
     * Проверить, есть ли конкретный ученик в колонке
     */
    public boolean isStudentInColumn(String column, String studentName) {
        List<String> names = getStudentNamesFromColumn(getColumnSelector(column));
        return names.stream().anyMatch(name -> name.equals(studentName));
    }

    /**
     * Проверить, есть ли ученик с днем рождения в указанный день
     */
    public boolean hasStudentBirthday(String day, String studentName) {
        String columnSelector = getColumnSelector(day);
        List<String> names = getStudentNamesFromColumn(columnSelector);
        return names.stream().anyMatch(name -> name.equals(studentName));
    }

    // ============ ПАРСИНГ ИМЕНИ ============

    /**
     * Распарсить имя ученика из строки
     */
    private Student parseStudentName(String fullName) {
        Student student = Student.builder().build();
        String[] parts = fullName.trim().split(" ");

        if (parts.length >= 2) {
            student.setLastName(parts[0]);
            student.setFirstName(parts[1]);
            if (parts.length >= 3) {
                student.setMiddleName(parts[2]);
            }
        }

        return student;
    }

    // ============ ПОЛУЧЕНИЕ ВСЕХ ДНЕЙ РОЖДЕНИЯ ============

    /**
     * Получить всех учеников с днями рождения (все колонки)
     */
    public List<Student> getAllBirthdayStudents() {
        List<Student> allStudents = new ArrayList<>();
        allStudents.addAll(getBirthdaysYesterday());
        allStudents.addAll(getBirthdaysToday());
        allStudents.addAll(getBirthdaysTomorrow());
        return allStudents;
    }

    /**
     * Получить общее количество дней рождения
     */
    public int getTotalBirthdays() {
        return getBirthdaysYesterdayCount() +
                getBirthdaysTodayCount() +
                getBirthdaysTomorrowCount();
    }

    /**
     * Получить статистику по дням рождения
     */
    public BirthdayStatistics getBirthdayStatistics() {
        return new BirthdayStatistics(
                getBirthdaysYesterdayCount(),
                getBirthdaysTodayCount(),
                getBirthdaysTomorrowCount(),
                getTotalBirthdays()
        );
    }

    // ============ ВНУТРЕННИЙ КЛАСС ДЛЯ СТАТИСТИКИ ============

    public static class BirthdayStatistics {
        private final int yesterday;
        private final int today;
        private final int tomorrow;
        private final int total;

        public BirthdayStatistics(int yesterday, int today, int tomorrow, int total) {
            this.yesterday = yesterday;
            this.today = today;
            this.tomorrow = tomorrow;
            this.total = total;
        }

        public int getYesterday() { return yesterday; }
        public int getToday() { return today; }
        public int getTomorrow() { return tomorrow; }
        public int getTotal() { return total; }

        @Override
        public String toString() {
            return String.format("Вчера: %d, Сегодня: %d, Завтра: %d, Всего: %d",
                    yesterday, today, tomorrow, total);
        }
    }

    // ============ ВАЛИДАЦИИ ============

    /**
     * Проверить, что все колонки имеют правильные заголовки
     */
    public boolean hasCorrectColumnTitles() {
        return "Вчера".equals(getYesterdayColumnTitle()) &&
                "Сегодня".equals(getTodayColumnTitle()) &&
                "Завтра".equals(getTomorrowColumnTitle());
    }

    /**
     * Проверить, что колонка с сегодняшними днями рождения подсвечена
     */
    public boolean isTodayColumnHighlighted() {
        return page.locator(birthdayTodayColumn).getAttribute("class").contains("today");
    }

    /**
     * Проверить, что все ученики с днями рождения отображаются
     */
    public boolean areAllBirthdaysDisplayed(List<String> expectedNames, String day) {
        String columnSelector = getColumnSelector(day);
        List<String> actualNames = getStudentNamesFromColumn(columnSelector);

        if (expectedNames.size() != actualNames.size()) {
            return false;
        }

        return actualNames.containsAll(expectedNames) && expectedNames.containsAll(actualNames);
    }

    /**
     * Проверить, что сообщение "Нет" отображается когда нет учеников
     */
    public boolean isEmptyMessageDisplayed(String column) {
        String columnSelector = getColumnSelector(column);
        return page.isVisible(columnSelector + " " + emptyMessage);
    }

    /**
     * Получить текст сообщения "Нет"
     */
    public String getEmptyMessageText(String column) {
        String columnSelector = getColumnSelector(column);
        if (page.isVisible(columnSelector + " " + emptyMessage)) {
            return page.textContent(columnSelector + " " + emptyMessage);
        }
        return null;
    }

    // ============ ВИЗУАЛЬНЫЕ ПРОВЕРКИ ============

    /**
     * Проверить, что колонка видна на странице
     */
    public boolean isColumnVisible(String column) {
        String columnSelector = getColumnSelector(column);
        return page.isVisible(columnSelector);
    }

    /**
     * Проверить, что колонка имеет правильный стиль
     */
    public boolean hasColumnCorrectStyle(String column) {
        String columnSelector = getColumnSelector(column);
        String className = page.getAttribute(columnSelector, "class");

        if ("today".equalsIgnoreCase(column)) {
            return className != null && className.contains("today");
        }

        return true;
    }

    /**
     * Проверить, что виджет полностью виден на странице
     */
    public boolean isWidgetFullyVisible() {
        Locator widget = page.locator(birthdayWidget);
        return widget.isVisible() &&
                widget.isEnabled() &&
                page.isVisible(widgetHeader) &&
                page.isVisible(widgetBody);
    }

    // ============ ОЖИДАНИЯ ============

    /**
     * Ожидать загрузки виджета
     */
//    public DashboardPage waitForWidgetToLoad() {
//        page.waitForSelector(birthdayWidget, new Page.WaitForSelectorOptions()
//                .setState(Page.WaitForSelectorState.VISIBLE));
//        return this;
//    }

    /**
     * Ожидать обновления списка дней рождения
     */
//    public DashboardPage waitForBirthdayUpdate() {
//        // Ждем пока загрузятся данные
//        page.waitForSelector(birthdayTodayColumn + " " + studentItems,
//                new Page.WaitForSelectorOptions()
//                        .setTimeout(5000)
//                        .setState(Page.WaitForSelectorState.VISIBLE));
//        return this;
//    }

    /**
     * Ожидать появления ученика в колонке
     */
    public DashboardPage waitForStudentInColumn(String column, String studentName, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (isStudentInColumn(column, studentName)) {
                return this;
            }
            page.waitForTimeout(500);
        }

        throw new RuntimeException("Ученик не появился в колонке '" + column +
                "' за " + timeoutSeconds + " секунд: " + studentName);
    }
}
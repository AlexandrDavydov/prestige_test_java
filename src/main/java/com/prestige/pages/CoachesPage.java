package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.Coach;

import java.util.ArrayList;
import java.util.List;

public class CoachesPage extends BasePage {

    // Заголовок страницы
    private final String pageTitle = "h1";

    // Кнопка добавления тренера
    private final String addCoachButton = "a[title='Добавить тренера']";
    private final String addCoachImage = "a[title='Добавить тренера'] img";

    // Таблица тренеров
    private final String tableRows = "table tr:not(.section_sub_title)";
    private final String tableHeader = "table tr.section_sub_title";

    // Селекторы для колонок таблицы
    private final String coachFullName = "td:nth-child(1)";
    private final String coachContacts = "td:nth-child(2)";
    private final String coachBirthday = "td:nth-child(3)";
    private final String coachLessonsCount = "td:nth-child(4)";
    private final String coachLessonsPaid = "td:nth-child(5)";
    private final String coachStudentPayment = "td:nth-child(6)";
    private final String coachAdditionalInfo = "td:nth-child(7)";
    private final String coachDebt = "td:nth-child(8)";
    private final String coachActions = "td:nth-child(9)";

    // Кнопки действий для тренера
    private final String giveMoneyButton = "a[title='Уплачено']";
    private final String editButton = "a[title='Редактировать']";
    private final String deleteButton = "a.delete-btn";

    public CoachesPage(Page page) {
        super(page);
    }

    /**
     * Перейти на страницу тренеров
     */
    public CoachesPage navigateTo() {
        page.navigate("/coaches");
        waitForPageLoad();
        return this;
    }

    /**
     * Проверить, что страница загружена
     */
    @Override
    public boolean isPageLoaded() {
        return page.isVisible(pageTitle) &&
                page.textContent(pageTitle).contains("Тренеры");
    }

    /**
     * Получить заголовок страницы
     */
    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    // ============ ДОБАВЛЕНИЕ ТРЕНЕРА ============

    /**
     * Кликнуть на кнопку "Добавить тренера"
     */
    public AddCoachPage clickAddCoach() {
        page.click(addCoachButton);
        return new AddCoachPage(page);
    }

    /**
     * Проверить, видна ли кнопка добавления
     */
    public boolean isAddCoachButtonVisible() {
        return page.isVisible(addCoachImage);
    }

    // ============ ПОЛУЧЕНИЕ ТРЕНЕРОВ ============

    /**
     * Получить количество тренеров в таблице
     */
    public int getCoachesCount() {
        return page.locator(tableRows).count();
    }

    /**
     * Получить всех тренеров
     */
    public List<Coach> getAllCoaches() {
        List<Coach> coaches = new ArrayList<>();
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            Locator row = rows.nth(i);
            Coach coach = mapRowToCoach(row);
            coaches.add(coach);
        }

        return coaches;
    }

    /**
     * Найти тренера по полному имени
     */
    public Coach getCoachByFullName(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            return mapRowToCoach(row);
        }
        return null;
    }

    /**
     * Найти строку тренера по полному имени
     */
    private Locator findCoachRow(String fullName) {
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            Locator row = rows.nth(i);
            String name = row.locator(coachFullName).textContent().trim();

            if (name.equals(fullName)) {
                return row;
            }
        }

        return null;
    }

    /**
     * Найти всех тренеров по имени (частичное совпадение)
     */
    public List<Coach> findCoachesByName(String namePart) {
        List<Coach> result = new ArrayList<>();
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            Locator row = rows.nth(i);
            String name = row.locator(coachFullName).textContent().trim();

            if (name.toLowerCase().contains(namePart.toLowerCase())) {
                result.add(mapRowToCoach(row));
            }
        }

        return result;
    }

    /**
     * Получить всех тренеров с долгами
     */
    public List<Coach> getCoachesWithDebt() {
        List<Coach> coachesWithDebt = new ArrayList<>();
        List<Coach> allCoaches = getAllCoaches();

        for (Coach coach : allCoaches) {
            if (coach.getDebt() > 0) {
                coachesWithDebt.add(coach);
            }
        }

        return coachesWithDebt;
    }

    /**
     * Получить тренеров без долгов
     */
    public List<Coach> getCoachesWithoutDebt() {
        List<Coach> coachesWithoutDebt = new ArrayList<>();
        List<Coach> allCoaches = getAllCoaches();

        for (Coach coach : allCoaches) {
            if (coach.getDebt() == 0) {
                coachesWithoutDebt.add(coach);
            }
        }

        return coachesWithoutDebt;
    }

    /**
     * Преобразовать строку таблицы в объект Coach
     */
    private Coach mapRowToCoach(Locator row) {
        String fullName = row.locator(coachFullName).textContent().trim();
        String contacts = row.locator(coachContacts).textContent().trim();
        String birthday = row.locator(coachBirthday).textContent().trim();
        String lessonsCountStr = row.locator(coachLessonsCount).textContent().trim();
        String lessonsPaidStr = row.locator(coachLessonsPaid).textContent().trim();
        String studentPaymentStr = row.locator(coachStudentPayment).textContent().trim();
        String additionalInfo = row.locator(coachAdditionalInfo).textContent().trim();
        String debtStr = row.locator(coachDebt).textContent().trim();

        // Парсим ФИО
        String[] nameParts = fullName.split(" ");
        String lastName = nameParts.length > 0 ? nameParts[0] : "";
        String firstName = nameParts.length > 1 ? nameParts[1] : "";
        String middleName = nameParts.length > 2 ? nameParts[2] : "";

        // Парсим числа
        int lessonsCount = parseInteger(lessonsCountStr);
        int lessonsPaid = parseInteger(lessonsPaidStr);
        double studentPayment = parseDouble(studentPaymentStr);
        double debt = parseDouble(debtStr);

        Coach coach = new Coach();
        coach.setLastName(lastName);
        coach.setFirstName(firstName);
        coach.setMiddleName(middleName);
        coach.setContacts(contacts);
        coach.setBirthday(birthday);
        coach.setLessonsCount(lessonsCount);
        coach.setLessonsPaid(lessonsPaid);
        coach.setStudentPayment(studentPayment);
        coach.setAdditionalInfo(additionalInfo);
        coach.setDebt(debt);

        return coach;
    }

    /**
     * Парсинг целого числа из строки
     */
    private int parseInteger(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Парсинг числа с плавающей точкой из строки
     */
    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // ============ ДЕЙСТВИЯ С ТРЕНЕРАМИ ============

    /**
     * Отметить оплату тренеру
     */
    public CoachesPage giveMoneyToCoach(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            row.locator(giveMoneyButton).click();
            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Отметить оплату тренеру по индексу
     */
    public CoachesPage giveMoneyToCoachAtIndex(int index) {
        Locator rows = page.locator(tableRows);
        if (index < rows.count()) {
            rows.nth(index).locator(giveMoneyButton).click();
            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Тренер с индексом " + index + " не найден");
    }

    /**
     * Редактировать тренера
     */
    public EditCoachPage clickEditCoach(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            row.locator(editButton).click();
            return new EditCoachPage(page);
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Редактировать тренера по индексу
     */
    public EditCoachPage clickEditCoachAtIndex(int index) {
        Locator rows = page.locator(tableRows);
        if (index < rows.count()) {
            rows.nth(index).locator(editButton).click();
            return new EditCoachPage(page);
        }
        throw new RuntimeException("Тренер с индексом " + index + " не найден");
    }

    /**
     * Удалить тренера (с подтверждением)
     */
    public CoachesPage deleteCoach(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            // Кликаем на кнопку удаления
            row.locator(deleteButton).click();

            // Подтверждаем удаление в модальном окне
            confirmDeleteModal();

            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Удалить тренера с проверкой модального окна
     */
    public CoachesPage deleteCoachWithModal(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            // Получаем URL удаления
            String deleteUrl = row.locator(deleteButton).getAttribute("data-url");

            // Открываем модальное окно
            row.locator(deleteButton).click();

            // Проверяем, что модальное окно открылось
            assert isDeleteModalVisible() : "Модальное окно не открылось";

            // Подтверждаем удаление
            confirmDeleteModal();

            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Удалить тренера по индексу
     */
    public CoachesPage deleteCoachAtIndex(int index) {
        Locator rows = page.locator(tableRows);
        if (index < rows.count()) {
            rows.nth(index).locator(deleteButton).click();
            confirmDeleteModal();
            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Тренер с индексом " + index + " не найден");
    }

    // ============ ПОЛУЧЕНИЕ ДАННЫХ О ТРЕНЕРЕ ============

    /**
     * Получить количество проведенных занятий тренера
     */
    public int getCoachLessonsCount(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getLessonsCount();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Получить количество оплаченных занятий тренера
     */
    public int getCoachLessonsPaid(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getLessonsPaid();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Получить сумму оплаты за 1 человека
     */
    public double getCoachStudentPayment(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getStudentPayment();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Получить долг тренера
     */
    public double getCoachDebt(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getDebt();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Получить контакты тренера
     */
    public String getCoachContacts(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getContacts();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Получить дополнительную информацию о тренере
     */
    public String getCoachAdditionalInfo(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getAdditionalInfo();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    // ============ ВАЛИДАЦИИ ============

    /**
     * Проверить, существует ли тренер
     */
    public boolean isCoachExists(String fullName) {
        return findCoachRow(fullName) != null;
    }

    /**
     * Проверить, существует ли тренер с указанными данными
     */
    public boolean isCoachExists(Coach coach) {
        String fullName = coach.getFullName();
        Coach found = getCoachByFullName(fullName);

        if (found == null) {
            return false;
        }

        return found.getContacts().equals(coach.getContacts()) &&
                found.getBirthday().equals(coach.getBirthday()) &&
                found.getLessonsCount() == coach.getLessonsCount() &&
                found.getLessonsPaid() == coach.getLessonsPaid() &&
                found.getStudentPayment() == coach.getStudentPayment();
    }

    /**
     * Проверить, есть ли у тренера долг
     */
    public boolean hasDebt(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        return coach != null && coach.getDebt() > 0;
    }

    /**
     * Проверить, оплачены ли все занятия тренера
     */
    public boolean isFullyPaid(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        return coach != null && coach.getDebt() == 0;
    }

    /**
     * Проверить, есть ли у тренера проведенные занятия
     */
    public boolean hasLessons(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        return coach != null && coach.getLessonsCount() > 0;
    }

    // ============ СТАТИСТИКА ============

    /**
     * Получить общее количество проведенных занятий
     */
    public int getTotalLessonsCount() {
        List<Coach> coaches = getAllCoaches();
        int total = 0;
        for (Coach coach : coaches) {
            total += coach.getLessonsCount();
        }
        return total;
    }

    /**
     * Получить общую сумму долгов
     */
    public double getTotalDebt() {
        List<Coach> coaches = getAllCoaches();
        double total = 0;
        for (Coach coach : coaches) {
            total += coach.getDebt();
        }
        return total;
    }

    /**
     * Получить среднее количество занятий на тренера
     */
    public double getAverageLessonsPerCoach() {
        List<Coach> coaches = getAllCoaches();
        if (coaches.isEmpty()) {
            return 0;
        }
        int total = getTotalLessonsCount();
        return (double) total / coaches.size();
    }

    /**
     * Получить тренера с максимальным долгом
     */
    public Coach getCoachWithMaxDebt() {
        List<Coach> coaches = getAllCoaches();
        if (coaches.isEmpty()) {
            return null;
        }

        Coach maxDebtCoach = coaches.get(0);
        for (Coach coach : coaches) {
            if (coach.getDebt() > maxDebtCoach.getDebt()) {
                maxDebtCoach = coach;
            }
        }
        return maxDebtCoach;
    }

    /**
     * Получить тренера с максимальным количеством занятий
     */
    public Coach getCoachWithMaxLessons() {
        List<Coach> coaches = getAllCoaches();
        if (coaches.isEmpty()) {
            return null;
        }

        Coach maxLessonsCoach = coaches.get(0);
        for (Coach coach : coaches) {
            if (coach.getLessonsCount() > maxLessonsCoach.getLessonsCount()) {
                maxLessonsCoach = coach;
            }
        }
        return maxLessonsCoach;
    }

    // ============ ОЖИДАНИЯ ============

    /**
     * Ожидать появления тренера в таблице
     */
    public CoachesPage waitForCoachToAppear(String fullName, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (isCoachExists(fullName)) {
                return this;
            }
            page.waitForTimeout(500);
        }

        throw new RuntimeException("Тренер не появился за " + timeoutSeconds + " секунд: " + fullName);
    }

    /**
     * Ожидать исчезновения тренера из таблицы
     */
    public CoachesPage waitForCoachToDisappear(String fullName, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (!isCoachExists(fullName)) {
                return this;
            }
            page.waitForTimeout(500);
        }

        throw new RuntimeException("Тренер не исчез за " + timeoutSeconds + " секунд: " + fullName);
    }

    /**
     * Ожидать обновления долга тренера
     */
    public CoachesPage waitForDebtUpdate(String fullName, double expectedDebt, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            double currentDebt = getCoachDebt(fullName);
            if (Math.abs(currentDebt - expectedDebt) < 0.01) {
                return this;
            }
            page.waitForTimeout(500);
            refreshPage();
        }

        throw new RuntimeException("Долг не обновился до " + expectedDebt + " за " + timeoutSeconds + " секунд");
    }

    // ============ ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ============

    /**
     * Получить все имена тренеров
     */
    public List<String> getAllCoachNames() {
        List<String> names = new ArrayList<>();
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            String name = rows.nth(i).locator(coachFullName).textContent().trim();
            names.add(name);
        }

        return names;
    }

    /**
     * Получить URL удаления тренера
     */
    public String getDeleteUrlForCoach(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            return row.locator(deleteButton).getAttribute("data-url");
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    /**
     * Проверить, что таблица не пуста
     */
    public boolean isTableNotEmpty() {
        return getCoachesCount() > 0;
    }

    /**
     * Получить данные тренера в виде строки (для отладки)
     */
    public String getCoachRowData(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            return row.textContent().trim();
        }
        return "Тренер не найден: " + fullName;
    }
}
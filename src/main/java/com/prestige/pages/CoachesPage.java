package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.Coach;
import static com.prestige.utils.StepHelper.step;

import java.util.ArrayList;
import java.util.List;

public class CoachesPage extends BasePage {

    private final String pageTitle = "h1";

    private final String addCoachButton = "a[title='Добавить тренера']";
    private final String addCoachImage = "a[title='Добавить тренера'] img";

    private final String tableRows = "table tr:not(.section_sub_title)";
    private final String tableHeader = "table tr.section_sub_title";

    private final String coachFullName = "td:nth-child(1)";
    private final String coachContacts = "td:nth-child(2)";
    private final String coachBirthday = "td:nth-child(3)";
    private final String coachLessonsCount = "td:nth-child(4)";
    private final String coachLessonsPaid = "td:nth-child(5)";
    private final String coachStudentPayment = "td:nth-child(6)";
    private final String coachAdditionalInfo = "td:nth-child(7)";
    private final String coachDebt = "td:nth-child(8)";
    private final String coachActions = "td:nth-child(9)";

    private final String giveMoneyButton = "a[title='Уплачено']";
    private final String editButton = "a[title='Редактировать']";
    private final String deleteButton = "a.delete-btn";

    public CoachesPage(Page page) {
        super(page);
    }

    public CoachesPage navigateTo() {
        return step("Переход на страницу тренеров", () -> {
            page.navigate("/coaches");
            waitForPageLoad();
            return this;
        });
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    // ============ ДОБАВЛЕНИЕ ТРЕНЕРА ============

    public AddCoachPage clickAddCoach() {
        return step("Нажать кнопку добавления тренера", () -> {
            page.click(addCoachButton);
            return new AddCoachPage(page);
        });
    }

    public boolean isAddCoachButtonVisible() {
        return page.isVisible(addCoachImage);
    }

    // ============ ПОЛУЧЕНИЕ ТРЕНЕРОВ ============

    public int getCoachesCount() {
        return page.locator(tableRows).count();
    }

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

    public Coach getCoachByFullName(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            return mapRowToCoach(row);
        }
        return null;
    }

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

    private Coach mapRowToCoach(Locator row) {
        String fullName = row.locator(coachFullName).textContent().trim();
        String contacts = row.locator(coachContacts).textContent().trim();
        String birthday = row.locator(coachBirthday).textContent().trim();
        String lessonsCountStr = row.locator(coachLessonsCount).textContent().trim();
        String lessonsPaidStr = row.locator(coachLessonsPaid).textContent().trim();
        String studentPaymentStr = row.locator(coachStudentPayment).textContent().trim();
        String additionalInfo = row.locator(coachAdditionalInfo).textContent().trim();
        String debtStr = row.locator(coachDebt).textContent().trim();

        String[] nameParts = fullName.split(" ");
        String lastName = nameParts.length > 0 ? nameParts[0] : "";
        String firstName = nameParts.length > 1 ? nameParts[1] : "";
        String middleName = nameParts.length > 2 ? nameParts[2] : "";

        int lessonsCount = parseInteger(lessonsCountStr);
        int lessonsPaid = parseInteger(lessonsPaidStr);
        int studentPayment = parseInteger(studentPaymentStr);
        int debt = parseInteger(debtStr);

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

    private int parseInteger(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // ============ ДЕЙСТВИЯ С ТРЕНЕРАМИ ============

    public CoachesPage giveMoneyToCoach(String fullName) {
        return step("Отметить оплату тренеру: {fullName}", () -> {
            Locator row = findCoachRow(fullName);
            if (row != null) {
                row.locator(giveMoneyButton).click();
                waitForPageLoad();
                return this;
            }
            throw new RuntimeException("Тренер не найден: " + fullName);
        });
    }

    public CoachesPage giveMoneyToCoachAtIndex(int index) {
        return step("Отметить оплату тренеру по индексу: {index}", () -> {
            Locator rows = page.locator(tableRows);
            if (index < rows.count()) {
                rows.nth(index).locator(giveMoneyButton).click();
                waitForPageLoad();
                return this;
            }
            throw new RuntimeException("Тренер с индексом " + index + " не найден");
        });
    }

    public EditCoachPage clickEditCoach(String fullName) {
        return step("Редактировать тренера: {fullName}", () -> {
            Locator row = findCoachRow(fullName);
            if (row != null) {
                row.locator(editButton).click();
                return new EditCoachPage(page);
            }
            throw new RuntimeException("Тренер не найден: " + fullName);
        });
    }

    public EditCoachPage clickEditCoachAtIndex(int index) {
        return step("Редактировать тренера по индексу: {index}", () -> {
            Locator rows = page.locator(tableRows);
            if (index < rows.count()) {
                rows.nth(index).locator(editButton).click();
                return new EditCoachPage(page);
            }
            throw new RuntimeException("Тренер с индексом " + index + " не найден");
        });
    }

    public CoachesPage deleteCoach(String fullName) {
        return step("Удалить тренера: {fullName}", () -> {
            Locator row = findCoachRow(fullName);
            if (row != null) {
                row.locator(deleteButton).click();
                confirmDeleteModal();
                return this;
            }
            throw new RuntimeException("Тренер не найден: " + fullName);
        });
    }

    public int getCoachLessonsCount(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getLessonsCount();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    public int getCoachLessonsPaid(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getLessonsPaid();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    public double getCoachStudentPayment(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getStudentPayment();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    public double getCoachDebt(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getDebt();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    public String getCoachContacts(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getContacts();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    public String getCoachAdditionalInfo(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        if (coach != null) {
            return coach.getAdditionalInfo();
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    // ============ ВАЛИДАЦИИ ============

    public boolean isCoachExists(String fullName) {
        return findCoachRow(fullName) != null;
    }

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

    public boolean hasDebt(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        return coach != null && coach.getDebt() > 0;
    }

    public boolean isFullyPaid(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        return coach != null && coach.getDebt() == 0;
    }

    public boolean hasLessons(String fullName) {
        Coach coach = getCoachByFullName(fullName);
        return coach != null && coach.getLessonsCount() > 0;
    }

    // ============ СТАТИСТИКА ============

    public int getTotalLessonsCount() {
        List<Coach> coaches = getAllCoaches();
        int total = 0;
        for (Coach coach : coaches) {
            total += coach.getLessonsCount();
        }
        return total;
    }

    public double getTotalDebt() {
        List<Coach> coaches = getAllCoaches();
        double total = 0;
        for (Coach coach : coaches) {
            total += coach.getDebt();
        }
        return total;
    }

    public double getAverageLessonsPerCoach() {
        List<Coach> coaches = getAllCoaches();
        if (coaches.isEmpty()) {
            return 0;
        }
        int total = getTotalLessonsCount();
        return (double) total / coaches.size();
    }

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

    public CoachesPage waitForCoachToAppear(String fullName, int timeoutSeconds) {
        return step("Ожидание появления тренера: {fullName}", () -> {
            long startTime = System.currentTimeMillis();
            long timeout = timeoutSeconds * 1000L;

            while (System.currentTimeMillis() - startTime < timeout) {
                if (isCoachExists(fullName)) {
                    return this;
                }
                page.waitForTimeout(500);
            }

            throw new RuntimeException("Тренер не появился за " + timeoutSeconds + " секунд: " + fullName);
        });
    }

    public CoachesPage waitForCoachToDisappear(String fullName, int timeoutSeconds) {
        return step("Ожидание удаления тренера: {fullName}", () -> {
            long startTime = System.currentTimeMillis();
            long timeout = timeoutSeconds * 1000L;

            while (System.currentTimeMillis() - startTime < timeout) {
                if (!isCoachExists(fullName)) {
                    return this;
                }
                page.waitForTimeout(500);
            }

            throw new RuntimeException("Тренер не исчез за " + timeoutSeconds + " секунд: " + fullName);
        });
    }

    public CoachesPage waitForDebtUpdate(String fullName, double expectedDebt, int timeoutSeconds) {
        return step("Ожидание обновления долга тренера: {fullName}", () -> {
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
        });
    }

    // ============ ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ============

    public List<String> getAllCoachNames() {
        List<String> names = new ArrayList<>();
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            String name = rows.nth(i).locator(coachFullName).textContent().trim();
            names.add(name);
        }

        return names;
    }

    public String getDeleteUrlForCoach(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            return row.locator(deleteButton).getAttribute("data-url");
        }
        throw new RuntimeException("Тренер не найден: " + fullName);
    }

    public boolean isTableNotEmpty() {
        return getCoachesCount() > 0;
    }

    public String getCoachRowData(String fullName) {
        Locator row = findCoachRow(fullName);
        if (row != null) {
            return row.textContent().trim();
        }
        return "Тренер не найден: " + fullName;
    }
}

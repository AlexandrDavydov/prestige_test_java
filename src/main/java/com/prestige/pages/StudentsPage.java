package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentsPage extends BasePage {

    // Селекторы
    private final String pageTitle = "h1";
    private final String addStudentButton = "a[title='Добавить ученика'] img";
    private final String addStudentLink = "a[title='Добавить ученика']";
    private final String tableRows = ".custom-table tbody tr";
    private final String tableHeader = ".custom-table tr.section_sub_title";

    // Селекторы для строк таблицы
    private final String studentName = "//td[1]";
    private final String studentContacts = "td:nth-child(2)";
    private final String studentBirthday = "td:nth-child(3)";
    private final String studentLessonsCount = "td:nth-child(4)";
    private final String studentAdditionalInfo = "td:nth-child(5)";
    private final String studentActions = "td:nth-child(6)";

    // Селекторы для кнопок действий
    private final String buyCardsButton = "a[title='Продать абонемент']";
    private final String editButton = "a[title='Редактировать']";
    private final String deleteButton = "a.delete-btn";

    public StudentsPage(Page page) {
        super(page);
    }

    /**
     * Перейти на страницу учеников
     */
    public StudentsPage navigateTo() {
        page.navigate("/students");
        waitForPageLoad();
        return this;
    }

    @Override
    public boolean isPageLoaded() {
        return page.isVisible(pageTitle) &&
                page.textContent(pageTitle).contains("Ученики");
    }

    public String getPageTitle() {
        return page.textContent(pageTitle);
    }

    public AddStudentPage clickAddStudent() {
        page.click(addStudentLink);
        return new AddStudentPage(page);
    }

    public int getStudentsCount() {
        return page.locator(tableRows).count();
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            Locator row = rows.nth(i);
            Student student = mapRowToStudent(row);
            students.add(student);
        }

        return students;
    }

    public Student getStudentByFullName(String fullName) {
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            Locator row = rows.nth(i);
            String name = row.locator(studentName).textContent().trim();

            if (name.equals(fullName)) {
                return mapRowToStudent(row);
            }
        }

        return null;
    }

    private Locator findStudentRow(String fullName) {
        Locator rows = page.locator(tableRows);

        for (int i = 1; i < rows.count(); i++) {
            Locator row = rows.nth(i);
            String name = row.locator(studentName).textContent().trim();

            if (name.equals(fullName)) {
                return row;
            }
        }

        return null;
    }

    /**
     * Найти ученика и кликнуть на кнопку "Продать абонемент"
     */
//    public BuyCardsPage clickBuyCardsForStudent(String fullName) {
//        Locator row = findStudentRow(fullName);
//        if (row != null) {
//            row.locator(buyCardsButton).click();
//            return new BuyCardsPage(page);
//        }
//        throw new RuntimeException("Ученик не найден: " + fullName);
//    }
//
    public EditStudentPage clickEditStudent(String fullName) {
        Locator row = findStudentRow(fullName);
        if (row != null) {
            row.locator(editButton).click();
            return new EditStudentPage(page);
        }
        throw new RuntimeException("Ученик не найден: " + fullName);
    }

    public StudentsPage deleteStudent(String fullName) {
        Locator row = findStudentRow(fullName);
        if (row != null) {
            // Кликаем на кнопку удаления
            row.locator(deleteButton).click();
            confirmDeleteModal();
            return this;
        }
        throw new RuntimeException("Ученик не найден: " + fullName);
    }

    public boolean isStudentExists(String fullName) {
        return findStudentRow(fullName) != null;
    }

    public boolean checkStudentExists(String fullName, boolean exists) {
        return findStudentRow(fullName) != null;
    }

    public boolean isStudentExists(Student student) {
        String fullName = student.getFullName();
        Student found = getStudentByFullName(fullName);

        if (found == null) {
            return false;
        }

        // Сравниваем данные
        return found.getContacts().equals(student.getContacts()) &&
                found.getBirthday().equals(student.getBirthday()) &&
                found.getLessonsCount() == student.getLessonsCount();
    }

    /**
     * Получить количество занятий у ученика
     */
    public int getStudentLessonsCount(String fullName) {
        Locator row = findStudentRow(fullName);
        if (row != null) {
            String countText = row.locator(studentLessonsCount).textContent().trim();
            return Integer.parseInt(countText);
        }
        throw new RuntimeException("Ученик не найден: " + fullName);
    }

    /**
     * Получить дополнительную информацию об ученике
     */
    public String getStudentAdditionalInfo(String fullName) {
        Locator row = findStudentRow(fullName);
        if (row != null) {
            return row.locator(studentAdditionalInfo).textContent().trim();
        }
        throw new RuntimeException("Ученик не найден: " + fullName);
    }

    /**
     * Преобразовать строку таблицы в объект Student
     */
    private Student mapRowToStudent(Locator row) {
        String name = row.locator(studentName).textContent().trim();
        String contacts = row.locator(studentContacts).textContent().trim();
        String birthday = row.locator(studentBirthday).textContent().trim();
        String lessonsCountStr = row.locator(studentLessonsCount).textContent().trim();
        String additionalInfo = row.locator(studentAdditionalInfo).textContent().trim();

        // Парсим ФИО
        String[] nameParts = name.split(" ");
        String lastName = nameParts.length > 0 ? nameParts[0] : "";
        String firstName = nameParts.length > 1 ? nameParts[1] : "";
        String middleName = nameParts.length > 2 ? nameParts[2] : "";

        int lessonsCount = 0;
        try {
            lessonsCount = Integer.parseInt(lessonsCountStr);
        } catch (NumberFormatException e) {
            // Если не число, оставляем 0
        }

        Student student = Student.builder().build();
        student.setLastName(lastName);
        student.setFirstName(firstName);
        student.setMiddleName(middleName);
        student.setContacts(contacts);
        student.setBirthday(birthday);
        student.setLessonsCount(lessonsCount);
        student.setAdditionalInfo(additionalInfo);

        return student;
    }

    /**
     * Получить URL для удаления ученика из data-url атрибута
     */
    public String getDeleteUrlForStudent(String fullName) {
        Locator row = findStudentRow(fullName);
        if (row != null) {
            return row.locator(deleteButton).getAttribute("data-url");
        }
        throw new RuntimeException("Ученик не найден: " + fullName);
    }

    /**
     * Ожидать появления ученика в таблице
     */
    public StudentsPage waitForStudentToAppear(String fullName, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (isStudentExists(fullName)) {
                return this;
            }
            page.waitForTimeout(500);
        }

        throw new RuntimeException("Ученик не появился в таблице за " + timeoutSeconds + " секунд: " + fullName);
    }

    /**
     * Ожидать исчезновения ученика из таблицы
     */
    public StudentsPage waitForStudentToDisappear(String fullName, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (!isStudentExists(fullName)) {
                return this;
            }
            page.waitForTimeout(500);
        }

        throw new RuntimeException("Ученик не исчез из таблицы за " + timeoutSeconds + " секунд: " + fullName);
    }

    /**
     * Получить все имена учеников
     */
    public List<String> getAllStudentNames() {
        List<String> names = new ArrayList<>();
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            String name = rows.nth(i).locator(studentName).textContent().trim();
            names.add(name);
        }

        return names;
    }
}
package com.prestige.models;

public class Coach {
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String contacts;
    private String birthday;
    private int lessonsCount;
    private int lessonsPaid;
    private double studentPayment;
    private String additionalInfo;
    private double debt;

    // Конструкторы
    public Coach() {}

    public Coach(String lastName, String firstName, String middleName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getLessonsCount() {
        return lessonsCount;
    }

    public void setLessonsCount(int lessonsCount) {
        this.lessonsCount = lessonsCount;
    }

    public int getLessonsPaid() {
        return lessonsPaid;
    }

    public void setLessonsPaid(int lessonsPaid) {
        this.lessonsPaid = lessonsPaid;
    }

    public double getStudentPayment() {
        return studentPayment;
    }

    public void setStudentPayment(double studentPayment) {
        this.studentPayment = studentPayment;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    /**
     * Получить полное имя
     */
    public String getFullName() {
        if (middleName != null && !middleName.isEmpty()) {
            return lastName + " " + firstName + " " + middleName;
        }
        return lastName + " " + firstName;
    }

    /**
     * Рассчитать долг
     */
    public double calculateDebt() {
        return (lessonsCount - lessonsPaid) * studentPayment;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", fullName='" + getFullName() + '\'' +
                ", contacts='" + contacts + '\'' +
                ", birthday='" + birthday + '\'' +
                ", lessonsCount=" + lessonsCount +
                ", lessonsPaid=" + lessonsPaid +
                ", studentPayment=" + studentPayment +
                ", debt=" + debt +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
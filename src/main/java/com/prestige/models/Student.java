package com.prestige.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder()

public class Student {
    private int id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String contacts;
    private String birthday;
    private int lessonsCount;
    private String additionalInfo;

    /**
     * Получить полное имя
     */
    public String getFullName() {
        if (middleName != null && !middleName.isEmpty()) {
            return lastName + " " + firstName + " " + middleName;
        }
        return lastName + " " + firstName;
    }

    public String getFirstAndLastName() {
        return lastName + " " + firstName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fullName='" + getFullName() + '\'' +
                ", contacts='" + contacts + '\'' +
                ", birthday='" + birthday + '\'' +
                ", lessonsCount=" + lessonsCount +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
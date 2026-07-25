package com.prestige.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coach {
    private int id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String contacts;
    private String birthday;
    private int lessonsCount;
    private int lessonsPaid;
    private int studentPayment;
    private String additionalInfo;
    private int debt;

    public String getFullName() {
        if (middleName != null && !middleName.isEmpty()) {
            return lastName + " " + firstName + " " + middleName;
        }
        return lastName + " " + firstName;
    }

    public String getFirstAndLastName() {
        return lastName + " " + firstName;
    }

    public double calculateDebt() {
        return (lessonsCount - lessonsPaid) * studentPayment;
    }
}

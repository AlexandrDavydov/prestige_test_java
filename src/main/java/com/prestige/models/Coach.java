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

    public String getFullName() {
        if (middleName != null && !middleName.isEmpty()) {
            return lastName + " " + firstName + " " + middleName;
        }
        return lastName + " " + firstName;
    }

    public double calculateDebt() {
        return (lessonsCount - lessonsPaid) * studentPayment;
    }
}

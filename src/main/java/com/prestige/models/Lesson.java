package com.prestige.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private int id;
    private String date;
    private String lessonName;
    private int coachId;
    private String status;
    private String studentIds;

    public int getStudentsCount() {
        if (studentIds == null || studentIds.isBlank()) return 0;
        return studentIds.split(",").length;
    }
}

package com.prestige.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonTemplate {
    private int id;
    private String templateName;
    private int coachId;
    private  String studentsIds;
}

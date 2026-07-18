package com.prestige.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private int id;
    private String name;
    private int price;
    private int lessonsCount;
    private String duration;
    private String color;
    private String status;
    private String creationDate;
}

package com.prestige.utils;

import com.github.javafaker.Faker;
import com.prestige.models.Card;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CardFactory {
    private static final Faker faker = new Faker(new Locale("ru", "RU"));

    private static final String[] CARD_NAMES = {
            "Абонемент на 4 занятия",
            "Абонемент на 8 занятий",
            "Абонемент на 12 занятий",
            "Абонемент на 16 занятий",
            "Пробное занятие",
            "Индивидуальное занятие"
    };

    private static final String[] DURATIONS = {
            "1 месяц", "2 месяца", "3 месяца", "6 месяцев", "12 месяцев"
    };

    private static final String[] COLORS = {
            "#FF5733", "#33FF57", "#3357FF", "#FF33F5", "#F5FF33", "#33FFF5", "#FF8C33", "#8C33FF"
    };

    private static final String[] STATUSES = {
            "active", "inactive", "archived"
    };

    public static Card createRandomCard() {
        Card card = new Card();
        card.setName(CARD_NAMES[faker.random().nextInt(CARD_NAMES.length)]);
        card.setPrice(faker.random().nextInt(2000, 12000));
        card.setLessonsCount((faker.random().nextInt(4) + 1) * 4);
        card.setDuration(faker.random().nextInt(4,20).toString());
        card.setColor(COLORS[faker.random().nextInt(COLORS.length)]);
        card.setStatus(STATUSES[faker.random().nextInt(STATUSES.length)]);
        card.setCreationDate(getFormattedDate(faker.date().birthday(0, 2)));
        return card;
    }

    public static Card createCardWithCustomData(
            String name,
            int price,
            int lessonsCount,
            String duration,
            String color,
            String status,
            String creationDate) {
        Card card = new Card();
        card.setName(name);
        card.setPrice(price);
        card.setLessonsCount(lessonsCount);
        card.setDuration(duration);
        card.setColor(color);
        card.setStatus(status);
        card.setCreationDate(creationDate);
        return card;
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}

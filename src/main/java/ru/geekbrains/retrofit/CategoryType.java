package ru.geekbrains.retrofit;

import lombok.Getter;

public enum CategoryType {
        FOOD(1, "Food"),
        ELECTRONIC(2, "Electronic");

        @Getter
        private final String title;
        @Getter
        private final Integer id;

    CategoryType(Integer id, String title) {
        this.id = id;
        this.title = title;
    }
}

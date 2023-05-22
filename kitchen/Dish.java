package com.javarush.task.task27.task2712.kitchen;

public enum Dish {
    FISH(25),
    STEAK(30),
    SOUP(15),
    JUICE(5),
    WATER(3);

    private int duration;

    Dish(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public static String allDishesToString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Dish.values().length; i++) {
            builder.append(Dish.values()[i]);
            if (i < Dish.values().length - 1)
                builder.append(", ");
        }
        return builder.toString().trim();
    }
}

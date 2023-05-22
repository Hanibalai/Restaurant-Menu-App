package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleHelper {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        return reader.readLine();
    }

    public static List<Dish> getAllDishesForOrder() throws IOException {
        List<Dish> dishes = new ArrayList<>();
        for (Dish dish : Dish.values()) {
            System.out.println(dish);
        }
        writeMessage("Please choose a dish from the list.");
        while (true) {
            String dishName = ConsoleHelper.readString().trim();
            if ("exit".equalsIgnoreCase(dishName)) {
                break;
            }
            try {
                Dish dish = Dish.valueOf(dishName.toUpperCase());
                dishes.add(dish);
                writeMessage(dishName + " has been successfully added to your order");
            } catch (Exception e) {
                writeMessage(dishName + " doesn't exists in the list");
            }
        }
        return dishes;
    }
}

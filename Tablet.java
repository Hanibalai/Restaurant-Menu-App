package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.AdvertisementManager;
import com.javarush.task.task27.task2712.ad.NoVideoAvailableException;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.TestOrder;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class Tablet {
    private final int number;
    static Logger logger = Logger.getLogger(Tablet.class.getName());

    private LinkedBlockingQueue<Order> queue;

    public Tablet(int number) {
        this.number = number;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public void createOrder() {
        try {
            processOrder(new Order(this));
        } catch (IOException e) {
            logger.log(SEVERE, "Console is unavailable.");
        }
    }

    public void createTestOrder() {
        try {
            processOrder(new TestOrder(this));
        } catch (IOException e) {
            logger.log(SEVERE, "Console is unavailable.");
        }
    }

    private void processOrder(Order order) {
        ConsoleHelper.writeMessage(order.toString());
        if (!order.isEmpty()) {
            queue.add(order);
            AdvertisementManager manager = new AdvertisementManager(order.getTotalCookingTime() * 60);
            try {
                manager.processVideos();
            } catch (NoVideoAvailableException e) {
                logger.log(Level.INFO, "No video is available for the order " + order);
            }
        }
    }

    @Override
    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }
}

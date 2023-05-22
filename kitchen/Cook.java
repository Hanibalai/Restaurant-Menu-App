package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook extends Observable implements Runnable {
    private String name;

    private LinkedBlockingQueue<Order> queue;

    public Cook(String name) {
        this.name = name;
    }

    private boolean busy;

    public boolean isBusy() {
        return busy;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10);
                    if (!queue.isEmpty()) {
                        if (!busy)
                            startCookingOrder(queue.take());
                    }
                }
            } catch (InterruptedException ignored) {}
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void startCookingOrder(Order order) {
        busy = true;
        ConsoleHelper.writeMessage("Start cooking - " + order);
        try {
            Thread.sleep(order.getTotalCookingTime() * 10L);
        } catch (InterruptedException ignored) {}
        setChanged();
        notifyObservers(order);
        EventDataRow row = new CookedOrderEventDataRow(order.getTablet().toString(),
                name, order.getTotalCookingTime() * 60, order.getDishes());
        StatisticManager.getInstance().register(row);
        busy = false;
    }

    @Override
    public String toString() {
        return name;
    }
}

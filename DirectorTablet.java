package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.Advertisement;
import com.javarush.task.task27.task2712.ad.StatisticAdvertisementManager;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DirectorTablet {
    public void printAdvertisementProfit() {
        Map<String, Double> profitResultMap = StatisticManager.getInstance().getProfitMap();
        double totalAmount = 0;
        for (String day : profitResultMap.keySet()) {
            ConsoleHelper.writeMessage(String.format("%s - %3.2f", day, profitResultMap.get(day)));
            totalAmount += profitResultMap.get(day);
        }
        ConsoleHelper.writeMessage(String.format("Total - %3.2f", totalAmount));
    }

    public void printCookWorkloading() {
        Map<String, Map<String, Integer>> cookWorkloadMap = StatisticManager.getInstance().getCookWorkloadMap();
        for (String day : cookWorkloadMap.keySet()) {
            ConsoleHelper.writeMessage(day);
            Map<String, Integer> cookWorkloadMapPerDay = cookWorkloadMap.get(day);
            for (String cookName : cookWorkloadMapPerDay.keySet()) {
                int cookWorkloadMin = cookWorkloadMapPerDay.get(cookName);
                if (cookWorkloadMin > 0)
                    ConsoleHelper.writeMessage(String.format("%s - %d min", cookName, cookWorkloadMin));
            }
        }
    }

    public void printActiveVideoSet() {
        List<Advertisement> advertisements = StatisticAdvertisementManager.getInstance().getActiveVideoSet();
        advertisements.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        for (Advertisement advertisement : advertisements) {
            ConsoleHelper.writeMessage(advertisement.getName() + " - " + advertisement.getHits());
        }
    }

    public void printArchivedVideoSet() {
        List<Advertisement> advertisements = StatisticAdvertisementManager.getInstance().getArchivedVideoSet();
        advertisements.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        for (Advertisement advertisement : advertisements) {
            ConsoleHelper.writeMessage(advertisement.getName());
        }
    }
}

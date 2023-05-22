package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticManager {
    private static StatisticManager instance;
    private final StatisticStorage statisticStorage = new StatisticStorage();

    private StatisticManager() {
    }

    public static StatisticManager getInstance() {
        if (instance == null) {
            instance = new StatisticManager();
        }
        return instance;
    }

    public void register(EventDataRow data) {
        statisticStorage.put(data);
    }

    public Map<String, Double> getProfitMap() {
        Map<String, Double> resultMap = new TreeMap<>(Collections.reverseOrder());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        List<EventDataRow> eventDataRowList = statisticStorage.storage.get(EventType.SELECTED_VIDEOS);
        for (EventDataRow row : eventDataRowList) {
            VideoSelectedEventDataRow videoSelectedEventDataRow = (VideoSelectedEventDataRow) row;
            String day = formatter.format(videoSelectedEventDataRow.getDate());
            double amount = (double) videoSelectedEventDataRow.getAmount() / 100;
            if (!resultMap.containsKey(day)) {
                resultMap.put(day, 0.0);
            }
            resultMap.put(day, resultMap.get(day) + amount);
        }
        return resultMap;
    }

    public Map<String, Map<String, Integer>> getCookWorkloadMap() {
        Map<String, Map<String, Integer>> resultMap = new TreeMap<>(Collections.reverseOrder());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        List<EventDataRow> eventDataRowList = statisticStorage.getStorage().get(EventType.COOKED_ORDER);
        for (EventDataRow row : eventDataRowList) {
            CookedOrderEventDataRow dataRow = (CookedOrderEventDataRow) row;
            String day = formatter.format(dataRow.getDate());
            String cookName = dataRow.getCookName();
            int cookingTimeMin = (dataRow.getTime() + 59) / 60;
            if (!resultMap.containsKey(day)) {
                resultMap.put(day, new TreeMap<>());
            }
            Map<String, Integer> cookMap = resultMap.get(day);
            if (!cookMap.containsKey(cookName)) {
                cookMap.put(cookName, 0);
            }
            cookMap.put(cookName, cookMap.get(cookName) + cookingTimeMin);
        }
        return resultMap;
    }

    private class StatisticStorage {
        private Map<EventType, List<EventDataRow>> storage;

        public StatisticStorage() {
            storage = new HashMap<>();
            for (EventType type : EventType.values()) {
                storage.put(type, new ArrayList<>());
            }
        }

        private void put(EventDataRow data) {
            EventType type = data.getType();
            if (!this.storage.containsKey(type))
                throw new UnsupportedOperationException();
            
            this.storage.get(type).add(data);
        }

        private Map<EventType, List<EventDataRow>> getStorage() {
            return storage;
        }
    }
}

package com.javarush.task.task27.task2712.ad;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticAdvertisementManager {
    private static StatisticAdvertisementManager instance;
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();

    private StatisticAdvertisementManager() {
    }

    public static StatisticAdvertisementManager getInstance() {
        if (instance == null) {
            instance = new StatisticAdvertisementManager();
        }
        return instance;
    }

    public List<Advertisement> getActiveVideoSet() {
        return storage.list().stream().filter(Advertisement::isActive).collect(Collectors.toList());
    }

    public List<Advertisement> getArchivedVideoSet() {
        return storage.list().stream().filter(x -> !x.isActive()).collect(Collectors.toList());
    }
}

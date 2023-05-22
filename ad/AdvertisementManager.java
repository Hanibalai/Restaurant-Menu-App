package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private final int timeSeconds;
    private List<Advertisement> optimalVideoSet = null;
    private long optimalAmount;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() {
        if (storage.list().isEmpty()) throw new NoVideoAvailableException();
        List<Advertisement> advertisementList = checkCountVideoHits(storage.list());
        Collections.reverse(advertisementList);
        checkAllVideoSets(advertisementList);
        EventDataRow row = new VideoSelectedEventDataRow(optimalVideoSet,
                optimalAmount, calculateVideoSetTotalDuration(optimalVideoSet));
        StatisticManager.getInstance().register(row);
        if (optimalVideoSet != null) displayAdvertisement();
    }

    private void checkAllVideoSets(List<Advertisement> videoSet) {
        if (videoSet.size() > 0) {
            checkVideoSet(videoSet);
        }
        for (int i = 0; i < videoSet.size(); i++) {
            List<Advertisement> copiedVideoSet = new ArrayList<>(videoSet);
            copiedVideoSet.remove(i);
            if (calculateVideoSetTotalAmount(copiedVideoSet) >= optimalAmount) {
                checkAllVideoSets(copiedVideoSet);
            }
        }
    }

    private void checkVideoSet(List<Advertisement> videoSet) {
        if (optimalVideoSet == null) {
            if (calculateVideoSetTotalDuration(videoSet) <= timeSeconds) {
                optimalVideoSet = videoSet;
                optimalAmount = calculateVideoSetTotalAmount(videoSet);
            }
        } else {
            long totalAmount = calculateVideoSetTotalAmount(videoSet);
            int totalDuration = calculateVideoSetTotalDuration(videoSet);
            int bestVideoSetTotalDuration = calculateVideoSetTotalDuration(optimalVideoSet);
            if (totalDuration <= timeSeconds) {
                if (totalAmount > optimalAmount) {
                    optimalVideoSet = videoSet;
                    optimalAmount = totalAmount;
                } else if (totalAmount == optimalAmount) {
                    if (totalDuration > bestVideoSetTotalDuration) {
                        optimalVideoSet = videoSet;
                    } else if (totalDuration == bestVideoSetTotalDuration && videoSet.size() < optimalVideoSet.size()) {
                        optimalVideoSet = videoSet;
                    }
                }
            }
        }
    }

    private long calculateVideoSetTotalAmount(List<Advertisement> videoSet) {
        return videoSet.stream().mapToLong(Advertisement::getAmountPerOneDisplaying).sum();
    }

    private int calculateVideoSetTotalDuration(List<Advertisement> videoSet) {
        return videoSet.stream().mapToInt(Advertisement::getDuration).sum();
    }

    private List<Advertisement> checkCountVideoHits(List<Advertisement> videoSet) {
        return videoSet.stream().filter(Advertisement::isActive).collect(Collectors.toList());
    }

    private void displayAdvertisement() {
        optimalVideoSet.sort((o1, o2) -> {
            if (o2.getAmountPerOneDisplaying() - o1.getAmountPerOneDisplaying() == 0) {
                return o2.getDuration() - o1.getDuration();
            }
            return Long.compare(o2.getAmountPerOneDisplaying(), o1.getAmountPerOneDisplaying());
        });
        for (Advertisement video : optimalVideoSet) {
            showVideoInConsole(video);
            video.revalidate();
        }
    }

    private void showVideoInConsole(Advertisement advertisement) {
        ConsoleHelper.writeMessage(String.format("%s is displaying... %d, %d ",
                advertisement.getName(),
                advertisement.getAmountPerOneDisplaying(),
                advertisement.getAmountPerOneDisplaying() * 1000 / advertisement.getDuration()));
    }
}

package com.github.mensetsu.nannmon.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.mensetsu.nannmon.controller.StatisticsResponse;
import com.github.mensetsu.nannmon.controller.TransactionRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Stores all transaction values in a 60 element array where each element holds
 * the transactions values for a one second interval. The array is looped so
 * that the 61st second interval values are stored back in the 1st element of
 * the array (and so on).
 * 
 * @author amatsuo
 */
@Slf4j
@Service
public class StorageService {

    private final static int SIZE_IN_SECONDS = 60;
    // X seconds in milliseconds
    private final static long TOTAL_CAPACITY_IN_MS = 1000 * SIZE_IN_SECONDS;

    // entries of transaction amounts by timestamp
    @Getter(AccessLevel.PACKAGE) // only for unit testing
    private final AggregateStatistic[] entries;

    public StorageService() {
        // create initial cache with one element for each second that we care about
        entries = new AggregateStatistic[SIZE_IN_SECONDS];
        for (int i = 0; i < SIZE_IN_SECONDS; i++) {
            // creating empty stats for each entry
            entries[i] = new AggregateStatistic();
        }
    }

    /**
     * Adds transaction to current entry if it's valid; ie, not older than 1 minute
     * (from current time) and not in the future. The future requirement was not
     * explicitly stated in the requirements, but it could potentially cause
     * problems with the reuse of our entries array, so we will not allow it.
     * 
     * @param request
     *            a single transaction request
     * @return true if request was added, false otherwise
     */
    public boolean add(TransactionRequest request) {
        long now = System.currentTimeMillis();
        if (request.getTimestamp() < now - TOTAL_CAPACITY_IN_MS) {
            log.debug("Request is too old: {}", request);
            return false;
        }
        if (request.getTimestamp() > now) {
            log.debug("Request is too new: {}", request);
            return false;
        }
        int currentIndex = getTimeIndex(request.getTimestamp());
        entries[currentIndex].addStatisticValue(request.getAmount());
        return true;
    }

    /**
     * Calculates the real-time statistic values for the current 60 second interval.
     * 
     * @return transaction values for the past 60 seconds
     */
    public StatisticsResponse getCurrentResponse() {
        int currentIndex = getCurrentIndex();
        // iterate last 60 seconds
        StatisticsResponse response = new StatisticsResponse();
        for (int count = 0; count < SIZE_IN_SECONDS; count++) {
            int i = subtractFromIndex(currentIndex, count);
            response.update(entries[i]);
        }
        // calculate average of last 60 seconds
        response.calculateAvg();

        log.debug("Returning response: {}", response);
        return response;
    }

    // scheduled task(s)

    /**
     * Scheduled method that is run in a separate thread to clean up or initialize
     * the next (expiring) second element before it is used to store new values.
     */
    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    public void cleanup() {
        long now = System.currentTimeMillis();
        int currentIndex = getTimeIndex(now);
        int expiringIndex = subtractFromIndex(currentIndex, (StorageService.SIZE_IN_SECONDS - 1));
        long msToNextSecond = 1000 - (now % 1000);

        try {
            log.debug("Clean up will wait for ~{}ms", msToNextSecond);
            // sleep to expiry time minus a small delta
            Thread.sleep(msToNextSecond - 1);
        } catch (InterruptedException e) {
            log.warn("Thread was interrupted: {}", e);
        }

        log.debug("Cleanup has been called on index: {}", expiringIndex);
        entries[expiringIndex].clear();
    }

    // a couple of handy methods to deal with index logic

    // package-level so we can call method from tests as well
    int getTimeIndex(long timestamp) {
        int currentIndex = (int) Math.floor(timestamp / 1000) % SIZE_IN_SECONDS;
        log.debug("Index is: {}", currentIndex);
        return currentIndex;
    }

    private int getCurrentIndex() {
        return getTimeIndex(System.currentTimeMillis());
    }

    private int subtractFromIndex(int index, int minus) {
        int newIndex = index - minus;
        if (newIndex < 0) { // means we've encountered beginning of array
            newIndex += SIZE_IN_SECONDS;
        }
        return newIndex;
    }
}

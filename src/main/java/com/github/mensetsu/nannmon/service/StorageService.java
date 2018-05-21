package com.github.mensetsu.nannmon.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.mensetsu.nannmon.controller.StatisticsResponse;
import com.github.mensetsu.nannmon.controller.TransactionRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StorageService {
	
	private final static int SECONDS = 60;
	// X seconds in milliseconds 
	private final static long ONE_MINUTE = 1000 * SECONDS;
	
	// entries of transaction amounts by timestamp
	@Getter(AccessLevel.PACKAGE) // only for unit testing
	private final AggregateStatistic[] entries;
	
	public StorageService() {
		// create initial cache with one element for each second that we care about
		entries = new AggregateStatistic[SECONDS];
		for (int i = 0; i < SECONDS; i++) {
			// creating empty stats for each entry
			entries[i] = new AggregateStatistic();
		}
	}

	public boolean add(TransactionRequest request) {
		if (request.getTimestamp() < System.currentTimeMillis() - ONE_MINUTE) {
			return false;
		}
		int currentIndex = getTimeIndex(request.getTimestamp());
		entries[currentIndex].addStatisticValue(request.getAmount());
		return true;
	}
	
	public StatisticsResponse getCurrentResponse() {
		int currentIndex = getCurrentIndex();
		// iterate last 60 seconds
		StatisticsResponse response = new StatisticsResponse();
		for (int count = 0; count < SECONDS; count++) {
			int i = subtractFromIndex(currentIndex, count);
			response.update(entries[i]);
		}
		// calculate average of last 60 seconds
		response.calculateAvg();
		
		log.debug("Returning response: {}", response);
		return response;
	}
	
	// scheduled task(s)
	
	@Scheduled(initialDelay = 1000, fixedRate = 1000)
	public void cleanup() {
		int currentIndex = getCurrentIndex();
		int expiredIndex = subtractFromIndex(currentIndex, (StorageService.SECONDS - 1));
		
		log.debug("Cleanup has been called on index: {}", expiredIndex);
		entries[expiredIndex].clear();
	}
	
	// a couple of handy methods to deal with index logic
	
	// package-level so we can call method from tests as well
	int getTimeIndex(long timestamp) {
		int currentIndex = (int) Math.floor(timestamp / 1000) % SECONDS;
		log.debug("Index is: {}", currentIndex);
		return currentIndex;
	}
	
	private int getCurrentIndex() {
		return getTimeIndex(System.currentTimeMillis());
	}
	
	private int subtractFromIndex(int index, int minus) {
		int newIndex = index - minus;
		if (newIndex < 0) { // means we've encountered beginning of array
			newIndex += SECONDS;
		}
		return newIndex;
	}
}

package com.github.mensetsu.nannmon.service;

import org.springframework.stereotype.Service;

import com.github.mensetsu.nannmon.controller.StatisticsResponse;
import com.github.mensetsu.nannmon.controller.TransactionRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CacheService {
	
	private final static int SECONDS = 60;
	// 60 seconds in milliseconds 
	private final static long ONE_MINUTE = 1000 * SECONDS;
	
	// entries of transaction amounts by timestamp
	private final CachedStatistic[] entries;
	
	public CacheService() {
		// create initial cache with one element for each second that we care about
		entries = new CachedStatistic[60];
		for (int i = 0; i < 60; i++) {
			// creating empty stats for each entry
			entries[i] = new CachedStatistic();
		}
	}

	public boolean add(TransactionRequest request) {
		long now = System.currentTimeMillis();
		if (request.getTimestamp() < now - ONE_MINUTE) {
			return false;
		}
		int currentIndex = (int) Math.floor(now / 1000) % SECONDS;
		entries[currentIndex].addEntry(request.getAmount());
		return true;
	}
	
	public StatisticsResponse getCurrentResponse() {
		int currentIndex = (int) Math.floor(System.currentTimeMillis() / 1000) % SECONDS;
		log.debug("Index is: {}", currentIndex);
		
		// iterate last 60 seconds
		StatisticsResponse response = new StatisticsResponse();
		for (int count = 0; count < SECONDS; count++) {
			int i = currentIndex - count;
			if (i < 0) { // means we've encountered beginning of array
				i += SECONDS;
			}
			response.update(entries[i]);
		}
		// calculate average of last 60 seconds
		response.calculateAvg();
		
		log.debug("Returning response: {}", response);
		return response;
	}
}

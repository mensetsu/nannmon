package com.github.mensetsu.nannmon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.github.mensetsu.nannmon.controller.StatisticsResponse;
import com.github.mensetsu.nannmon.controller.TransactionRequest;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CacheService {
	
	// 60 seconds in milliseconds 
	private final static long ONE_MINUTE = 1000 * 60;
	
	// entries of transaction amounts by timestamp
	private final ConcurrentHashMap<Long, List<Double>> entries;
	@Getter
	private StatisticsResponse currentResponse;
	
	public CacheService() {
		entries = new ConcurrentHashMap<>();
		currentResponse = new StatisticsResponse();
	}

	public boolean add(TransactionRequest request) {
		long now = System.currentTimeMillis();
		if (request.getTimestamp() < now - ONE_MINUTE) {
			return false;
		}
		entries.computeIfAbsent(request.getTimestamp(), l -> new ArrayList<>()).add(request.getAmount());
		return true;
	}
	
	public void computeCurrentResponse() {
		long oneMinuteAgo = System.currentTimeMillis() - ONE_MINUTE;
		// initialize empty response values
		final StatisticsResponse newResponse = new StatisticsResponse();
		entries.keySet().iterator().forEachRemaining(k -> {
			// too old, removing and ignoring for this run
			if (k < oneMinuteAgo) {
				log.debug("Removing old entry: {}", k);
				entries.remove(k);
			} else {
				log.debug("Adding values for: {}", k);
				entries.get(k).forEach(v -> {
					newResponse.addEntry(v);
				});
			}
		});
		newResponse.calculateAvg();
		// replace currentResponse;
		currentResponse = newResponse;
		log.info("New response value: {}", newResponse);
	}
}

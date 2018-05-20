package com.github.mensetsu.nannmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.mensetsu.nannmon.service.CacheService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class Statistics {
	
	private final CacheService cache;
	
	@Autowired
	public Statistics(CacheService cache) {
		this.cache = cache;
	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public StatisticsResponse get() {
		log.debug("Stats GET has been called...");
		
		// specifications don't mention a specific response code, so won't specify one here
		return cache.getCurrentResponse();
	}

}

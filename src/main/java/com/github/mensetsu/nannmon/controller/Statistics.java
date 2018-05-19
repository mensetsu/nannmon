package com.github.mensetsu.nannmon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class Statistics {

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public StatisticsResponse get() {
		log.debug("Stats GET has been called...");
		// specifications don't mention a specific response code, so won't specify one here
		return new StatisticsResponse(1000, 100, 200, 50, 10);
	}

}

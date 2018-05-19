package com.github.mensetsu.nannmon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class Statistics {

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public String get() {
		log.info("Get has been called...");
		return "Get stats has been called";
	}

}

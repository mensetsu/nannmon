package com.github.mensetsu.nannmon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class Transactions {

	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public String post() {
		log.info("Post has been called...");
		return "Tx post has been called";
	}

}

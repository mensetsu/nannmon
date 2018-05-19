package com.github.mensetsu.nannmon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class Transactions {
	
	// 60 seconds in milliseconds 
	private final static long ONE_MINUTE = 1000 * 60;

	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public ResponseEntity<Void> post(@RequestBody TransactionRequest request) {
		log.debug("Tx POST has been called with: {}", request);
		
		// simple validation
		if (request == null || !request.isValid()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		long now = System.currentTimeMillis();
		// handle old transactions
		if (request.getTimestamp() < now - ONE_MINUTE) {
			log.debug("Tx is too old: request ({}) vs now ({})", request.getTimestamp(), now);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} 
		// specifications don't mention anything about timestamps in the future so they will be treated as valid
		log.debug("Tx timestamp is valid");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}

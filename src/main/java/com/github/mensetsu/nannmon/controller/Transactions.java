package com.github.mensetsu.nannmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.mensetsu.nannmon.service.CacheService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class Transactions {
	
	private final CacheService cache;
	
	@Autowired
	public Transactions(CacheService cache) {
		this.cache = cache;
	}
	
	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public ResponseEntity<Void> post(@RequestBody TransactionRequest request) {
		log.debug("Txn POST has been called with: {}", request);
		
		// simple validation
		if (request == null || !request.isValid()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// try to add request to cache
		if (!cache.add(request)) {
			log.debug("Txn is too old to be cached: request ({})", request.getTimestamp());
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} 
		// specifications don't mention anything about timestamps in the future so they will be treated as valid
		log.debug("Txn timestamp is valid");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}

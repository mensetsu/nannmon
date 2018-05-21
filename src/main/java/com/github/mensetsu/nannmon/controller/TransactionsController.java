package com.github.mensetsu.nannmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.mensetsu.nannmon.service.StorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * JSON REST controller for accepting transaction information.
 * 
 * @author amatsuo
 */
@Slf4j
@RestController
public class TransactionsController {
	
	private final StorageService storage;
	
	@Autowired
	public TransactionsController(StorageService storage) {
		this.storage = storage;
	}
	
	/**
	 * POST method for /transactions
	 * @param request contains the transaction parameters
	 * @return ResponseEntity with HttpStatus of BAD_REQUEST, NO_CONTENT, or CREATED
	 */
	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public ResponseEntity<Void> post(@RequestBody TransactionRequest request) {
		log.debug("Txn POST has been called with: {}", request);
		
		// simple validation
		if (request == null || !request.isValid()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// try to add request to storage
		if (!storage.add(request)) {
			log.debug("Txn was not added to storage: request ({})", request.getTimestamp());
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} 
		log.debug("Txn timestamp is valid");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}

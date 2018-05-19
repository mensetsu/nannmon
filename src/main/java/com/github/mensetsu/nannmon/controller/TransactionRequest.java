package com.github.mensetsu.nannmon.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TransactionRequest {
	
	private Double amount;
	private Long timestamp;
	
	// simple check to ensure parameters have been provided
	@JsonIgnore
	public boolean isValid() {
		return amount != null && timestamp != null;
	}

}

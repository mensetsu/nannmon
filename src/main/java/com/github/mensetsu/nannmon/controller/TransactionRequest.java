package com.github.mensetsu.nannmon.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
	
	// fields are Objects as it's easier to check when parameters haven't been provided (see isValid())
	private Double amount;
	private Long timestamp;
	
	// simple check to ensure parameters have been provided
	@JsonIgnore
	public boolean isValid() {
		return amount != null && timestamp != null;
	}

}

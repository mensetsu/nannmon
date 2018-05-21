package com.github.mensetsu.nannmon.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data object to hold request information for the transaction api.
 * 
 * @author amatsuo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
	
	// fields are Objects as it's easier to check when parameters haven't been provided (see isValid())
	private Double amount;
	private Long timestamp;
	
	/**
	 * Simple validation check to ensure parameters have been provided (ie; are not null).
	 * @return whether this object is valid or not
	 */
	@JsonIgnore
	public boolean isValid() {
		return amount != null && timestamp != null;
	}

}

package com.github.mensetsu.nannmon.controller;

import lombok.Data;

@Data
public class StatisticsResponse {
	
	private final double sum;
	private final double avg;
	private final double max;
	private final double min;
	private final long count;
	
}

package com.github.mensetsu.nannmon.controller;

import lombok.Data;

@Data
public class StatisticsResponse {
	
	private double sum;
	private double avg;
	private double max;
	private double min;
	private long count;
	
	public StatisticsResponse() {
		// initialize everything to 0
		sum = avg = max = min = count = 0;
	}
	
	public void addEntry(double value) {
		count++;
		if (min == 0d || value < min) {
			min = value;
		}
		if (value > max) {
			max = value;
		}
		sum += value;
	}
	
	public void calculateAvg() {
		if (count != 0l) {
			avg = sum / count;
		}
	}
	
}

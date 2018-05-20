package com.github.mensetsu.nannmon.controller;

import com.github.mensetsu.nannmon.service.AggregateStatistic;

import lombok.Data;

@Data
public class StatisticsResponse {
	
	private double sum;
	private double avg;
	private double max;
	private double min;
	private long count;
	
	public StatisticsResponse() {
		// initialize
		sum = avg = max = min = count = 0;
	}
	
	public void update(AggregateStatistic aggregate) {
		// do nothing if there are no statistics
		if (aggregate.getCount() < 1) {
			return; 
		}
		sum += aggregate.getSum();
		// just be careful not to use min if it's 0
		double minValue = aggregate.getMin();
		if (minValue > 0d && (min == 0d || minValue < min)) {
			min = minValue;
		}
		double maxValue = aggregate.getMax();
		if (maxValue > max) {
			max = maxValue;
		}
		count += aggregate.getCount();
	}
	
	public void calculateAvg() {
		if (count != 0l) {
			avg = sum / count;
		}
	}
	
}
package com.github.mensetsu.nannmon.controller;

import com.github.mensetsu.nannmon.service.CachedStatistic;

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
	
	public void update(CachedStatistic cachedStatistic) {
		sum += cachedStatistic.getSum();
		// just be careful not to use min if it's 0
		double minValue = cachedStatistic.getMin();
		if (minValue > 0d && (min == 0d || minValue < min)) {
			min = minValue;
		}
		double maxValue = cachedStatistic.getMax();
		if (maxValue > max) {
			max = maxValue;
		}
		count += cachedStatistic.getCount();
	}
	
	public void calculateAvg() {
		if (count != 0l) {
			avg = sum / count;
		}
	}
	
}
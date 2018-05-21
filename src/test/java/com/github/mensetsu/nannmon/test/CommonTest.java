package com.github.mensetsu.nannmon.test;

import static org.junit.Assert.assertEquals;

import com.github.mensetsu.nannmon.controller.StatisticsResponse;
import com.github.mensetsu.nannmon.service.AggregateStatistic;

public interface CommonTest {
	
	// constants
	
	double DELTA = 1e-15;
	
	// methods
	
	default void assertStatistic(double max, double min, double sum, long count, AggregateStatistic statistic) {
		assertEquals("max", max, statistic.getMax(), DELTA);
		assertEquals("min", min, statistic.getMin(), DELTA);
		assertEquals("sum", sum, statistic.getSum(), DELTA);
		assertEquals("count", count, statistic.getCount());
	}
	
	default void assertResponse(double sum, double avg, double max, double min, long count,
		StatisticsResponse response) {
		assertEquals("sum", sum, response.getSum(), DELTA);
		assertEquals("avg", avg, response.getAvg(), DELTA);
		assertEquals("max", max, response.getMax(), DELTA);
		assertEquals("min", min, response.getMin(), DELTA);
		assertEquals("count", count, response.getCount());
	}
}

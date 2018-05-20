package com.github.mensetsu.nammon.controller;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.locks.StampedLock;

import org.junit.Test;

import com.github.mensetsu.nannmon.controller.StatisticsResponse;
import com.github.mensetsu.nannmon.service.CachedAggregateStatistic;

public class StatisticsResponseTest {
	
	private static final double DELTA = 1e-15;
	
	private void assertResponse(double sum, double avg, double max, double min, long count, 
		StatisticsResponse response) {
		assertEquals("sum", sum, response.getSum(), DELTA);
		assertEquals("avg", avg, response.getAvg(), DELTA);
		assertEquals("max", max, response.getMax(), DELTA);
		assertEquals("min", min, response.getMin(), DELTA);
		assertEquals("count", count, response.getCount());
	}
	
	@Test
	public void testInitialization() {
		StatisticsResponse response = new StatisticsResponse();
		assertResponse(0d, 0d, 0d, 0d, 0l, response);
	}
	
	@Test
	public void testNegativeMin() {
		StatisticsResponse response = new StatisticsResponse();
		CachedAggregateStatistic badStat = new CachedAggregateStatistic(0d, 0d, -1d, 0, new StampedLock());
		response.update(badStat);
		response.calculateAvg();
		assertResponse(0d, 0d, 0d, 0d, 0l, response); // min doesn't change
	}
	
	@Test
	public void testCalcuations() {
		CachedAggregateStatistic stat1 = new CachedAggregateStatistic(2d, 2d, 2d, 1, new StampedLock());
		StatisticsResponse response = new StatisticsResponse();
		response.update(stat1);
		response.calculateAvg();
		assertResponse(2d, 2d, 2d, 2d, 1l, response);
		
		CachedAggregateStatistic stat2 = new CachedAggregateStatistic(4d, 4d, 4d, 1, new StampedLock());
		response.update(stat2);
		response.calculateAvg();
		assertResponse(6d, 3d, 4d, 2d, 2l, response); // new max
		
		CachedAggregateStatistic stat3 = new CachedAggregateStatistic(1d, 1d, 1d, 1, new StampedLock());
		response.update(stat3);
		response.calculateAvg();
		assertResponse(7d, 7d/3d, 4d, 1d, 3l, response); // new min
		
		CachedAggregateStatistic stat4 = new CachedAggregateStatistic(10d, 1d, 1d, 10, new StampedLock());
		response.update(stat4);
		response.calculateAvg();
		assertResponse(17d, 17d/13d, 4d, 1d, 13l, response); // aggregate count
	}	

}

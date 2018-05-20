package com.github.mensetsu.nammon.controller;

import java.util.concurrent.locks.StampedLock;

import org.junit.Test;

import com.github.mensetsu.nammon.test.CommonTest;
import com.github.mensetsu.nannmon.controller.StatisticsResponse;
import com.github.mensetsu.nannmon.service.AggregateStatistic;

public class StatisticsResponseTest implements CommonTest {
	
	@Test
	public void testInitialization() {
		StatisticsResponse response = new StatisticsResponse();
		assertResponse(0d, 0d, 0d, 0d, 0l, response);
	}
	
	@Test
	public void testNegativeMin() {
		StatisticsResponse response = new StatisticsResponse();
		AggregateStatistic badStat = new AggregateStatistic(0d, 0d, -1d, 0, new StampedLock());
		response.update(badStat);
		response.calculateAvg();
		assertResponse(0d, 0d, 0d, 0d, 0l, response); // min doesn't change
	}
	
	@Test
	public void testCalcuations() {
		AggregateStatistic stat1 = new AggregateStatistic(2d, 2d, 2d, 1, new StampedLock());
		StatisticsResponse response = new StatisticsResponse();
		response.update(stat1);
		response.calculateAvg();
		assertResponse(2d, 2d, 2d, 2d, 1l, response);
		
		AggregateStatistic stat2 = new AggregateStatistic(4d, 4d, 4d, 1, new StampedLock());
		response.update(stat2);
		response.calculateAvg();
		assertResponse(6d, 3d, 4d, 2d, 2l, response); // new max
		
		AggregateStatistic stat3 = new AggregateStatistic(1d, 1d, 1d, 1, new StampedLock());
		response.update(stat3);
		response.calculateAvg();
		assertResponse(7d, 7d/3d, 4d, 1d, 3l, response); // new min
		
		AggregateStatistic stat4 = new AggregateStatistic(10d, 1d, 1d, 10, new StampedLock());
		response.update(stat4);
		response.calculateAvg();
		assertResponse(17d, 17d/13d, 4d, 1d, 13l, response); // aggregate count
	}	

}

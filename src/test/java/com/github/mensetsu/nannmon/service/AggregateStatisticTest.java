package com.github.mensetsu.nannmon.service;

import org.junit.Test;

import com.github.mensetsu.nannmon.service.AggregateStatistic;
import com.github.mensetsu.nannmon.test.CommonTest;

public class AggregateStatisticTest implements CommonTest {

	@Test
	public void testInitialValues() {
		AggregateStatistic statistic = new AggregateStatistic();
		assertStatistic(0d, 0d, 0d, 0l, statistic);
	}
	
	@Test
	public void testModifyValues() {
		AggregateStatistic statistic = new AggregateStatistic();
		statistic.addStatisticValue(1d);
		assertStatistic(1d, 1d, 1d, 1l, statistic);
		
		statistic.addStatisticValue(2d);
		assertStatistic(2d, 1d, 3d, 2l, statistic); // new max
		
		statistic.addStatisticValue(0.5d);
		assertStatistic(2d, 0.5d, 3.5d, 3l, statistic); // new min
		
		statistic.addStatisticValue(1d);
		assertStatistic(2d, 0.5d, 4.5d, 4l, statistic); // only update count
		
		statistic.clear();
		assertStatistic(0d, 0d, 0d, 0l, statistic);
	}
}

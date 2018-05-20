package com.github.mensetsu.nammon.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.github.mensetsu.nannmon.controller.StatisticsController;
import com.github.mensetsu.nannmon.controller.StatisticsResponse;
import com.github.mensetsu.nannmon.service.StorageService;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsControllerTest {
	
	@Mock
	private StorageService cache;
	
	@Test
	public void testCallToCurrentResponse() {
		StatisticsController controller = new StatisticsController(cache);
		StatisticsResponse expected = new StatisticsResponse();
		expected.setMax(99d); // just setting a random value
		when(cache.getCurrentResponse()).thenReturn(expected);
		
		StatisticsResponse response = controller.get();
		assertEquals(expected, response);
	}

}

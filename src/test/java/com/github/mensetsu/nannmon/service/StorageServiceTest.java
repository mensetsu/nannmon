package com.github.mensetsu.nannmon.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.github.mensetsu.nannmon.controller.StatisticsResponse;
import com.github.mensetsu.nannmon.controller.TransactionRequest;
import com.github.mensetsu.nannmon.test.CommonTest;

@RunWith(MockitoJUnitRunner.class)
public class StorageServiceTest implements CommonTest {

    private StorageService service;

    @Before
    public void setUp() {
        service = new StorageService();
    }

    private void assertInitialState(AggregateStatistic[] entries) {
        assertEquals(60, entries.length);
        Arrays.stream(entries).forEach(e -> {
            assertStatistic(0d, 0d, 0d, 0l, e);
        });
    }

    @Test
    public void testInitialStorage() {
        assertInitialState(service.getEntries());
    }

    @Test
    public void testAdding() {
        // too old by 1ms
        long oldTime = System.currentTimeMillis() - (1000 * 60 + 1);
        TransactionRequest oldRequest = new TransactionRequest(1d, oldTime);
        // old request
        boolean ok = service.add(oldRequest);
        assertFalse(ok);
        assertInitialState(service.getEntries());

        // too new by 1ms
        TransactionRequest tooNewRequest = new TransactionRequest(1d, System.currentTimeMillis() + 1);
        // too new request
        ok = service.add(tooNewRequest);
        assertFalse(ok);
        assertInitialState(service.getEntries());

        // new request
        TransactionRequest newRequest = new TransactionRequest(1d, System.currentTimeMillis());
        ok = service.add(newRequest);
        assertTrue(ok);
        int index = service.getTimeIndex(newRequest.getTimestamp());
        for (int i = 0; i < 60; i++) {
            if (i == index) {
                assertStatistic(1d, 1d, 1d, 1l, service.getEntries()[i]);
            } else {
                // initial state is unchanged for other entries
                assertStatistic(0d, 0d, 0d, 0l, service.getEntries()[i]);
            }
        }
    }

    @Test
    public void testCurrentResponse() {
        // add some random values into entries
        AggregateStatistic[] entries = service.getEntries();
        entries[0].addStatisticValue(1d);
        entries[29].addStatisticValue(2d);
        entries[29].addStatisticValue(2d);
        entries[59].addStatisticValue(3d);
        // get results
        StatisticsResponse response = service.getCurrentResponse();
        assertResponse(8d, 2d, 3d, 1d, 4l, response);
    }

    @Test
    public void testCleanup() throws InterruptedException {
        // add value to next index to be cleared, and previous index that won't be
        // cleared
        int index = service.getTimeIndex(System.currentTimeMillis());
        service.getEntries()[index + 1].addStatisticValue(1d);
        service.getEntries()[index - 1].addStatisticValue(1d);
        // clean up next entry
        service.cleanup();
        // should have cleared next index and left other entries
        for (int i = 0; i < 60; i++) {
            if (i == (index - 1)) {
                assertStatistic(1d, 1d, 1d, 1l, service.getEntries()[i]);
            } else {
                // initial state is unchanged for other entries
                assertStatistic(0d, 0d, 0d, 0l, service.getEntries()[i]);
            }
        }
    }

}

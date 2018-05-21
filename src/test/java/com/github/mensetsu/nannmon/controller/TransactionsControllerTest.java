package com.github.mensetsu.nannmon.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.mensetsu.nannmon.controller.TransactionRequest;
import com.github.mensetsu.nannmon.controller.TransactionsController;
import com.github.mensetsu.nannmon.service.StorageService;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsControllerTest {

    @Mock
    private StorageService cache;
    private TransactionsController controller;

    @Before
    public void setUp() {
        controller = new TransactionsController(cache);
    }

    @Test
    public void testValidation() {
        ResponseEntity<Void> response = controller.post(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        TransactionRequest invalidRequest = new TransactionRequest();
        assertFalse(invalidRequest.isValid());
        response = controller.post(invalidRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        invalidRequest.setAmount(99d);
        assertFalse(invalidRequest.isValid());
        response = controller.post(invalidRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        invalidRequest = new TransactionRequest(null, 99l);
        assertFalse(invalidRequest.isValid());
        response = controller.post(invalidRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testOldTimestamp() {
        TransactionRequest request = new TransactionRequest(99d, 99l);
        when(cache.add(request)).thenReturn(false); // mock up old request response

        ResponseEntity<Void> response = controller.post(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testValidTimestamp() {
        TransactionRequest request = new TransactionRequest(99d, 99l);
        when(cache.add(request)).thenReturn(true); // mock up valid request response

        ResponseEntity<Void> response = controller.post(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}

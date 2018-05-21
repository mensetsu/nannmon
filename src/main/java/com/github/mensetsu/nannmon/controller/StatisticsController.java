package com.github.mensetsu.nannmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.mensetsu.nannmon.service.StorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * JSON REST controller for displaying statistics.
 * 
 * @author amatsuo
 */
@Slf4j
@RestController
public class StatisticsController {

    private final StorageService storage;

    @Autowired
    public StatisticsController(StorageService storage) {
        this.storage = storage;
    }

    /**
     * GET method for /statistics
     * 
     * @return statistics for the past 60 seconds
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public StatisticsResponse get() {
        log.debug("Stats GET has been called...");

        // specifications don't mention a specific response code, so won't specify one
        // here
        return storage.getCurrentResponse();
    }

}

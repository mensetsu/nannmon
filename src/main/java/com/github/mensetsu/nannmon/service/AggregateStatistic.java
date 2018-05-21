package com.github.mensetsu.nannmon.service;

import java.util.concurrent.locks.StampedLock;

import lombok.AllArgsConstructor;

/**
 * Holds all the values of all statistics that have occurred during one second.
 * AggregateStatistic must be thread-safe and is reused after each 60 second
 * period. Therefore, all read and write calls have been locked appropriately.
 * 
 * @author amatsuo
 */
@AllArgsConstructor // just for tests
public class AggregateStatistic {

    private double sum;
    private double max;
    private double min;
    private long count;
    private final StampedLock lock;

    public AggregateStatistic() {
        lock = new StampedLock();
        // initialize everything to 0
        clear();
    }

    // lock all write operations

    /**
     * Adds a single transaction value to this aggregate value.
     * 
     * @param value
     *            the amount value from a single transaction
     */
    public void addStatisticValue(double value) {
        long stamp = lock.writeLock();
        try {
            count++;
            if (min == 0d || value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
            sum += value;
        } finally {
            lock.unlock(stamp);
        }
    }

    /**
     * Clears all values (sets to 0).
     */
    public void clear() {
        long stamp = lock.writeLock();
        try {
            sum = max = min = count = 0;
        } finally {
            lock.unlock(stamp);
        }
    }

    // lock all read operations

    /**
     * Accessor method.
     * 
     * @return sum value
     */
    public double getSum() {
        long stamp = lock.readLock();
        try {
            return sum;
        } finally {
            lock.unlock(stamp);
        }
    }

    /**
     * Accessor method.
     * 
     * @return max value
     */
    public double getMax() {
        long stamp = lock.readLock();
        try {
            return max;
        } finally {
            lock.unlock(stamp);
        }
    }

    /**
     * Accessor method.
     * 
     * @return min value
     */
    public double getMin() {
        long stamp = lock.readLock();
        try {
            return min;
        } finally {
            lock.unlock(stamp);
        }
    }

    /**
     * Accessor method.
     * 
     * @return count value
     */
    public long getCount() {
        long stamp = lock.readLock();
        try {
            return count;
        } finally {
            lock.unlock(stamp);
        }
    }
}

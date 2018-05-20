package com.github.mensetsu.nannmon.service;

import java.util.concurrent.locks.StampedLock;

import lombok.AllArgsConstructor;

/**
 * Value of all statistics that has occurred during one second.
 * Handles all the synchronization of read and writes.
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
	
	public void clear() {
		long stamp = lock.writeLock();
		try {
			sum = max = min = count = 0;
		} finally {
			lock.unlock(stamp);
		}
	}
	
	// lock all read operations
	
	public double getSum() {
		long stamp = lock.readLock();
		try {
			return sum;
		} finally {
			lock.unlock(stamp);
		}
	}

	public double getMax() {
		long stamp = lock.readLock();
		try {
			return max;
		} finally {
			lock.unlock(stamp);
		}
	}

	public double getMin() {
		long stamp = lock.readLock();
		try {
			return min;
		} finally {
			lock.unlock(stamp);
		}
	}

	public long getCount() {
		long stamp = lock.readLock();
		try {
			return count;
		} finally {
			lock.unlock(stamp);
		}
	}
}

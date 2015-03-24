package com.snapdeal.objectstore.service.impl;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This utility class is for generating uniqueId.
 * 
 * @author pdey
 *
 */
public class IDGenerator {

	private static AtomicInteger integer = new AtomicInteger();

	/**
	 * Returns next unique ID.
	 * 
	 * @return uniqueID.
	 */
	public static long getNextId() {

		return integer.incrementAndGet();
	}
}

package com.snapdeal.objectstore.impl;

import com.snapdeal.objectstore.api.IObjectStore;

/**
 * Implementation of IObjectStore.
 * 
 * @author pdey
 */
public class ConcurrentObjectStore implements IObjectStore {

	private StorageProcessorService inMemoryCacheStorage = new StorageProcessorService();

	@Override
	public long put(byte[] blob) {

		return inMemoryCacheStorage.store(blob);
	}

	@Override
	public byte[] get(long id) {
		return inMemoryCacheStorage.findAndReturn(id);
	}

	@Override
	public void delete(long id) {
		inMemoryCacheStorage.remove(id);
	}

	public void bgProcess() {
		inMemoryCacheStorage.process();
	}
}
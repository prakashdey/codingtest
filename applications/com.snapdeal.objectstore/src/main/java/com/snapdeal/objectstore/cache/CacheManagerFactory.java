package com.snapdeal.objectstore.cache;

/**
 * Factory class to get cache implementation.
 * 
 * @author pdey
 *
 * @param <K>
 * @param <V>
 */
public class CacheManagerFactory<K, V> {

	private static boolean useHashMap = false;

	public CacheManager<K, V> getCacheManager() {
		CacheManager<K, V> cacheManager = null;
		if (useHashMap) {
		} else {

			cacheManager = new EhCacheManagerImpl<K, V>();
		}
		return cacheManager;
	}

	// private static CacheManagerFactory uniqueInstance = new
	// CacheManagerFactory<K, V>();

}

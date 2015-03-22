package com.snapdeal.objectstore.cache;

public interface CacheManager<K, V> {

	/**
	 * Fetch data from cache using the key
	 * 
	 * @param iKey
	 * @param iCacheName
	 * @return V
	 */
	public V getFromCache(final K iKey, String iCacheName);

	/**
	 * Puts value into cache
	 * 
	 * @param iKey
	 * @param iValue
	 * @param iCacheName
	 */
	public void putIntoCache(final K iKey, final V iValue, String iCacheName);

	/**
	 * removes values from cache
	 * 
	 * @param iKey
	 * @param iCacheName
	 */
	public void removeObjectFromCacheForKey(final K iKey, String iCacheName);

	/**
	 * Clear the cache contents. <br>
	 * Note: Use this method with care.
	 * 
	 * @param iCacheName
	 */
	public void clearCache(String iCacheName);
}

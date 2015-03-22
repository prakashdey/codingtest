package com.snapdeal.objectstore.cache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * Implementation of CacheManager for EhCache
 * 
 * @param <K>
 *            Key type
 * @param <V>
 *            Value type
 */
public class EhCacheManagerImpl<K, V> implements CacheManager<K, V> {

	// =================================================
	// Class Variables
	// =================================================
	private static final String EHCACHE_DEFAULT_CONFIG_FILE = "ehcache.xml";

	// =================================================
	// Instance Variables
	// =================================================
	private final net.sf.ehcache.CacheManager cacheManager;

	// =================================================
	// Constructors
	// =================================================

	// =================================================
	// Class Methods
	// =================================================
	/**
	 * Constructor
	 */
	public EhCacheManagerImpl() {
		this(EHCACHE_DEFAULT_CONFIG_FILE);
	}

	/**
	 * Constructor
	 * 
	 * @param configFile
	 */
	public EhCacheManagerImpl(String configFile) {
		cacheManager = net.sf.ehcache.CacheManager.create(this.getClass().getResource("/" + configFile));
	}

	// =================================================
	// Accessors
	// =================================================

	// =================================================
	// Overridden Methods
	// =================================================

	// =================================================
	// Inner Class
	// =================================================

	// =================================================
	// Instance Methods
	// =================================================
	// NOTE: All functions below perform null check, if the cache is not
	// available, we either return (for void functions) or return null (empty
	// hash maps)
	// ensure applications continues to function even if cache is offline .

	/**
	 * @see com.apple.oto.common.CacheManager#getFromCache(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public V getFromCache(final K iKey, String iCacheName) {
		Ehcache aCache = cacheManager.getEhcache(iCacheName);
		V aValue = null;
		if (aCache != null) {
			Element anElement = aCache.get(iKey);
			if (null != anElement) {
				aValue = (V) anElement.getValue();
			}
		}
		return aValue;
	}

	/**
	 * @see com.apple.oto.common.CacheManager#putIntoCache(java.lang.Object,
	 *      java.lang.Object, java.lang.String)
	 */
	@Override
	public void putIntoCache(final K iKey, final V iValue, String iCacheName) {
		Ehcache aCache = cacheManager.addCacheIfAbsent(iCacheName);
		aCache.put(new Element(iKey, iValue));
	}

	/**
	 * @see com.apple.oto.common.CacheManager#removeObjectFromCacheForKey(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public void removeObjectFromCacheForKey(final K iKey, String iCacheName) {
		Ehcache aCache = cacheManager.getEhcache(iCacheName);
		if (aCache != null) {
			aCache.remove(iKey);
		}
	}

	/**
	 * @see com.apple.oto.common.CacheManager#clearCache(String)
	 */
	@Override
	public void clearCache(String iCacheName) {
		cacheManager.removeCache(iCacheName);
	}

	// =================================================
	// Private Methods
	// =================================================
}
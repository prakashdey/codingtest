package com.snapdeal.objectstore.cache;

/**
 * Factory class to get cache implementation.
 * 
 * @author pdey
 *
 * @param <K>
 * @param <V>
 */
public class CacheManagerFactory {

    private static boolean useHashMap = false;

    public <K, V> CacheManager<K, V> getCacheManager(String configFile) {
        CacheManager<K, V> cacheManager = null;
        if (useHashMap) {
            // TODO: yet to implement.
        } else {
            cacheManager = new EhCacheManagerImpl<K, V>(configFile);
        }
        return cacheManager;
    }

    public <K, V> CacheManager<K, V> getCacheManager() {
        CacheManager<K, V> cacheManager = null;
        if (useHashMap) {
            // TODO: yet to implement.
        } else {
            cacheManager = new EhCacheManagerImpl<K, V>();
        }
        return cacheManager;
    }
}

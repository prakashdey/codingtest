package com.snapdeal.objectstore.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import com.snapdeal.objectstore.cache.CacheManager;
import com.snapdeal.objectstore.cache.EhCacheManagerImpl;
import com.snapdeal.objectstore.dto.DataBean;
import com.snapdeal.objectstore.dto.MetaData;
import com.snapdeal.objectstore.utility.BlobToDataTransformer;
import com.snapdeal.objectstore.utility.FileSystemObjectStorage;

/**
 * This is the service class that is implements methods to:
 * <ul>
 * <li>Put data in the object store</li>
 * <li>Search and retrieve method</li>
 * <li>Delete object from object store</li>
 * <li>BackGround process to store/delete in file-system.</li>
 * </ul>
 * <br/>
 * EhCache is used as an Intermediate storage. All the actions are performed in
 * cache and in-memory. At a regular interval, data is synchronized with
 * filesystem.
 * 
 * @author pdey
 *
 */
public class StorageProcessorService {

	public StorageProcessorService() {

		/*Timer timer = new Timer();
		DeDuplicatingTask task = new DeDuplicatingTask();
		timer.schedule(task, 0, 3);
		Thread bgThread = new Thread(new BackgroundProcessor());
		bgThread.setDaemon(true);
		bgThread.start();*/
	}

	private static final Logger LOGGER = Logger.getLogger(StorageProcessorService.class.getName());

	private static final String INMEMORY_CACHE_NAME = "INMEMORY_CACHE_NAME";

	private final CacheManager<Long, DataBean> inMemoryStorageCache = new EhCacheManagerImpl<Long, DataBean>();

	private final ConcurrentMap<Long, MetaData> firstCache = new ConcurrentHashMap<Long, MetaData>();

	/**
	 * This method is used to store the data in memory. Later this data can be
	 * consolidated and stored in file system. Following operations are done:
	 * <ol>
	 * <li>Create metadata and databean.</li>
	 * <li>Add metadata into first level cache.</li>
	 * <li>Add the data into inMemory which will be later consolidated.</li>
	 * </ol>
	 * 
	 * @param data
	 * @return
	 */
	public long store(byte[] data) {

		DataBean dataBean = BlobToDataTransformer.getInstance().transform(data);
		MetaData metadata = metaDataForEntry(dataBean);
		firstCache.putIfAbsent(metadata.getInMemoryId(), metadata);
		inMemoryStorageCache.putIntoCache(dataBean.getId(), dataBean, INMEMORY_CACHE_NAME);
		return dataBean.getId();
	}

	/**
	 * Utility method used to create Metadata for new entry.
	 * 
	 * @param dataBean
	 * @return
	 */
	private MetaData metaDataForEntry(DataBean dataBean) {
		MetaData metaData = new MetaData(false, dataBean.getId(), true);
		return metaData;
	}

	/**
	 * Utility method to create metadata in case of delete.
	 * 
	 * @param dataBean
	 * @return
	 */
	private MetaData metaDataForDelete(DataBean dataBean) {
		MetaData metaData = new MetaData(true, dataBean.getId(), false);
		return metaData;
	}

	/**
	 * Utility method to create metadata in case of moving data to file system.
	 * 
	 * @param dataBean
	 * @return
	 */
	private MetaData metaDataAfterMovingToFS(DataBean dataBean) {
		MetaData metaData = new MetaData(false, dataBean.getId(), false);
		return metaData;
	}

	/**
	 * Removes the data for 'id' from first level cache by updating the metadata
	 * with "delete" flag as true.
	 * 
	 * @param id
	 */
	public void remove(long id) {

		DataBean dataBean = new DataBean();
		dataBean.setId(id);
		firstCache.replace(id, metaDataForDelete(dataBean));
	}

	/**
	 * Checks if the object is available to remove.
	 * 
	 * @param id
	 * @return
	 */
	private boolean exists(long id) {
		return firstCache.containsKey(id) && !firstCache.get(Long.valueOf(id)).isDelete();
	}

	/**
	 * This method is used to search the first level cache with details if a
	 * particular record is present or not. <br/>
	 * if present in first cache adn inMemory flag is true, data is retrieved
	 * from memory. else fetched from file system.
	 * 
	 * @param id
	 * @return
	 */
	public byte[] findAndReturn(long id) {
		MetaData metaData = firstCache.get(id);
		DataBean dataBean = null;
		// check if the data is ready for deletion, if not proceed to fetch from
		// memory/filesystem.
		if (null != metaData && !metaData.isDelete()) {
			if (metaData.isInMemory()) {
				dataBean = inMemoryStorageCache.getFromCache(id, INMEMORY_CACHE_NAME);
			} else {
				// TODO: Fetch from file system, test this flow.
				try {
					FileSystemObjectStorage fs = new FileSystemObjectStorage();
					dataBean = fs.read(id);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return dataBean != null ? dataBean.getBlob() : null;
	}

	/**
	 * This is the background process to move all the data from cache to file
	 * system and delete data that are not required. Following activities:
	 * <ul>
	 * <li>Iterate through fistCache and use metadata tof further process</li>
	 * <ol>
	 * <li>If isDelete flag is set then delete from inMemory cache/filesystem.</li>
	 * </ol>
	 * <li>If Data is in inMemory cache, then data is moved to filesystem.</li>
	 * </ul>
	 * 
	 * TODO: This method is now exposed, that can be invoked. Need to make a
	 * scheduled task that is repeated.
	 */
	public synchronized void process() {

		FileSystemObjectStorage fs = new FileSystemObjectStorage();
		// List contains all the deleted entry that needs to be deleted from
		// first cache.
		List<Long> listOfDeletedData = new LinkedList<Long>();
		// Iterate the first cache and process each entry.
		for (Entry<Long, MetaData> eachEntry : firstCache.entrySet()) {
			MetaData metaData = eachEntry.getValue();
			// if data is deleted in previous call
			if (metaData.isDelete()) {
				listOfDeletedData.add(eachEntry.getKey());
				if (metaData.isInMemory()) {
					inMemoryStorageCache.removeObjectFromCacheForKey(metaData.getInMemoryId(), INMEMORY_CACHE_NAME);
				} else {
					fs.delete(metaData.getInMemoryId());
				}
			} else if (metaData.isInMemory()) {
				try {
					DataBean dataBean = inMemoryStorageCache
							.getFromCache(metaData.getInMemoryId(), INMEMORY_CACHE_NAME);
					// Write into the file-system.
					fs.write(dataBean);
					// Update the metadata.
					firstCache.replace(eachEntry.getKey(), metaDataAfterMovingToFS(dataBean));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// Iterate the list of deleted data and remove each entry from first
		// cache.
		for (Long eachId : listOfDeletedData) {
			firstCache.remove(eachId);
		}
	}

	/**
	 * Task of DeDuplicating Task. <br/>
	 * TODO: Need to implement the process to be called.
	 */
	class DeDuplicatingTask extends TimerTask {

		@Override
		public void run() {
			process();
		}

	}

	/**
	 * Thread for running the background process.
	 * 
	 * TODO: Implementation pending.
	 */
	class BackgroundProcessor implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.currentThread().sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				process();
			}
		}

	}
}

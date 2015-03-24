package com.snapdeal.objectstore.impl;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import net.sf.ehcache.util.concurrent.ConcurrentHashMap;

import com.snapdeal.objectstore.api.IStorageProcessorService;
import com.snapdeal.objectstore.dto.MetaData;
import com.snapdeal.objectstore.service.IFileSystemStorageService;
import com.snapdeal.objectstore.service.impl.FileSystemStorageServiceImpl;
import com.snapdeal.objectstore.service.impl.IDGenerator;
import com.snapdeal.objectstore.utility.HashGenerator;

public class InlineStorageProcessorService implements IStorageProcessorService {

    private ConcurrentMap<Long, MetaData> lookupMap = new ConcurrentHashMap<Long, MetaData>();
    private ConcurrentMap<MetaData, AtomicLong> fileReference = new ConcurrentHashMap<MetaData, AtomicLong>();

    private IFileSystemStorageService fileSystemStorageServiceilesystem = new FileSystemStorageServiceImpl();

    /**
     * This method is used to reload all the metadata from file system.
     */
    @PostConstruct
    private void init() {

    }

    /**
     * This method is used to write data into
     */
    private void destroy() {

    }

    @Override
    public long put(byte[] iData) {

        String hashKey = HashGenerator.getHashCode(iData);
        boolean delete = false;
        long inMemoryId = 0;
        boolean inMemory = false;
        MetaData md = new MetaData(delete, inMemoryId, inMemory);
        md.setHashKey(hashKey);

        fileReference.computeIfPresent(md, (MD, oldV) -> {
            oldV.incrementAndGet();
            return oldV;
        });
        fileReference.computeIfAbsent(md, (V) -> {
            fileSystemStorageServiceilesystem.write(iData, hashKey);
            return new AtomicLong(0);
        });
        long nextId = IDGenerator.getNextId();
        lookupMap.put(nextId, md);
        return nextId;
    }

    @Override
    public byte[] get(long iId) {
        MetaData md = lookupMap.get(Long.valueOf(iId));
        if (null != md) {
            return fileSystemStorageServiceilesystem.read(md.getHashKey());
        }
        return null;
    }

    @Override
    public boolean delete(long iId) {
        MetaData md = lookupMap.remove(Long.valueOf(iId));
        if (null == md) {
            // no data present for id
        }
        fileReference.computeIfPresent(md, (MD, oldV) -> {
            long currentValue = oldV.decrementAndGet();
            if (currentValue == 0) {
                return null;
            }
            return oldV;
        });
        return false;
    }

    class ShutdownHook implements Runnable {

        private IStorageProcessorService storageProcessorService;

        public ShutdownHook(IStorageProcessorService iStorageProcessorService) {
            storageProcessorService = iStorageProcessorService;
        }

        @Override
        public void run() {
            // storageProcessorService.
        }
    }

    @Override
    public void process() {
        // TODO Auto-generated method stub

    }

}

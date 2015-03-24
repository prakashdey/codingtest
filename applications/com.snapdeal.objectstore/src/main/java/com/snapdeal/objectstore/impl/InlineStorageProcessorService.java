package com.snapdeal.objectstore.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.snapdeal.objectstore.api.IStorageProcessorService;
import com.snapdeal.objectstore.common.ConfigurationConstants;
import com.snapdeal.objectstore.common.ConfigurationConstants.ConfigurationConstantKeys;
import com.snapdeal.objectstore.dto.MetaData;
import com.snapdeal.objectstore.service.IFileSystemStorageService;
import com.snapdeal.objectstore.service.impl.FileSystemStorageServiceImpl;
import com.snapdeal.objectstore.service.impl.IDGenerator;
import com.snapdeal.objectstore.utility.HashGenerator;

public class InlineStorageProcessorService implements IStorageProcessorService {

    private static final Logger LOGGER = Logger.getLogger(InlineStorageProcessorService.class.getName());

    private ConcurrentMap<Long, MetaData> lookupMap = new ConcurrentHashMap<Long, MetaData>();
    private ConcurrentMap<MetaData, AtomicLong> fileReference = new ConcurrentHashMap<MetaData, AtomicLong>();

    private IFileSystemStorageService fileSystemStorageServiceilesystem = new FileSystemStorageServiceImpl();

    public InlineStorageProcessorService() {
        init();
    }
    /**
     * This method is used to reload all the metadata from file system.
     */
    // @PostConstruct
    private void init() {
        String metadataLookup = ConfigurationConstants.getProperty(ConfigurationConstantKeys.METADATA_FOLDER_LOCATION)
                + "lookup.ser";
        String metadataFileReference = ConfigurationConstants
                .getProperty(ConfigurationConstantKeys.METADATA_FOLDER_LOCATION) + "fileReference.ser";
        File metadataLookupFile = new File(metadataLookup);
        File fileReferenceFile = new File(metadataFileReference);

        if (null != metadataLookupFile && metadataLookupFile.exists() && metadataLookupFile.isFile()) {
            try (ObjectInputStream lookupOIS = new ObjectInputStream(new FileInputStream(metadataLookupFile))) {
                lookupMap = (ConcurrentMap<Long, MetaData>) lookupOIS.readObject();
            } catch (ClassNotFoundException | IOException e) {
                LOGGER.log(Level.SEVERE, "Metadata is not retrived.");
            }
        }
        if (null != fileReferenceFile && fileReferenceFile.exists() && fileReferenceFile.isFile()) {
            try (ObjectInputStream fileReferenceOIS = new ObjectInputStream(new FileInputStream(fileReferenceFile))) {
                fileReference = (ConcurrentMap<MetaData, AtomicLong>) fileReferenceOIS.readObject();
            } catch (ClassNotFoundException | IOException e) {
                LOGGER.log(Level.SEVERE, "Metadata is not retrived.");
            }
        }

        // Add the shutdown hook to store the metadata in server.
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(this)));
    }

    /**
     * This method is used to write metadata into filesystem.
     */
    void destroy() {

        String metadataLookupFile = ConfigurationConstants
                .getProperty(ConfigurationConstantKeys.METADATA_FOLDER_LOCATION) + "lookup.ser";
        String fileReferenceFile = ConfigurationConstants
                .getProperty(ConfigurationConstantKeys.METADATA_FOLDER_LOCATION) + "fileReference.ser";
        try (ObjectOutputStream lookupOOS = new ObjectOutputStream(new FileOutputStream(metadataLookupFile));
                ObjectOutputStream fileRefOOS = new ObjectOutputStream(new FileOutputStream(fileReferenceFile));) {
            lookupOOS.writeObject(lookupMap);
            fileRefOOS.writeObject(fileReference);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Metadata is not persisited.");
        }

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

    @Override
    public void process() {
        // TODO Auto-generated method stub

    }

}

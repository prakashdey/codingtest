package com.snapdeal.objectstore.impl;

import com.snapdeal.objectstore.api.IObjectStore;
import com.snapdeal.objectstore.api.IStorageProcessorService;
import com.snapdeal.objectstore.common.ConfigurationConstants;
import com.snapdeal.objectstore.common.ConfigurationConstants.ConfigurationConstantKeys;

/**
 * Implementation of IObjectStore api.
 * 
 * @author pdey
 */
public class ObjectStoreImpl implements IObjectStore {

    private IStorageProcessorService storageService;

    public ObjectStoreImpl() {
        getStorageService();
    }

    /**
     * TODO: Inject this via framework.
     * 
     * @return
     */
    private IStorageProcessorService getStorageService() {
        if (ConfigurationConstants.getProperty(ConfigurationConstantKeys.STORAGE_SERVICE_IMPLEMENTATION).equals("I")) {
            storageService = new InlineStorageProcessorService();
        } else {
            storageService = new OfflineStorageProcessorService();
        }
        return storageService;
    }

    @Override
    public long put(byte[] blob) {

        return storageService.put(blob);
    }

    @Override
    public byte[] get(long id) {
        return storageService.get(id);
    }

    @Override
    public void delete(long id) {
        storageService.delete(id);
    }

    public void bgProcess() {
        storageService.process();
    }
}
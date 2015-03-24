package com.snapdeal.objectstore.impl;

import com.snapdeal.objectstore.api.IStorageProcessorService;

public class ShutdownHook implements Runnable {

    private IStorageProcessorService storageProcessorService;

    public ShutdownHook(IStorageProcessorService iStorageProcessorService) {
        storageProcessorService = iStorageProcessorService;
    }

    @Override
    public void run() {
        ((InlineStorageProcessorService) storageProcessorService).destroy();
    }
}
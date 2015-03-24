package com.snapdeal.objectstore.dto;

import java.io.Serializable;

/**
 * This class holds metadata related to blob data. This information will be used
 * for read/write operations of data on disk.
 * 
 * @author pdey
 */

public class MetaData implements Serializable {

    private static final long serialVersionUID = 1L;

    public MetaData(boolean delete, long inMemoryId, boolean inMemory) {
        this.delete = delete;
        this.inMemoryId = inMemoryId;
        this.inMemory = inMemory;
    }

    // boolean flag if the data is ready for deletion.
    private boolean delete;
    // id which defines the id used to store in inMemorycache
    private long inMemoryId;
    // flag used to check if the data is in memory or file-system
    private boolean inMemory;
    // This is the computed key for each blob.
    private String hashKey;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public long getInMemoryId() {
        return inMemoryId;
    }

    public void setInMemoryId(long inMemoryId) {
        this.inMemoryId = inMemoryId;
    }

    public boolean isInMemory() {
        return inMemory;
    }

    public void setInMemory(boolean inMemory) {
        this.inMemory = inMemory;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String iHashKey) {
        hashKey = iHashKey;
    }

    @Override
    public boolean equals(Object iObj) {
        if (null == iObj || !(iObj instanceof MetaData)) {
            return false;
        }
        if (null != hashKey && null != ((MetaData) iObj).getHashKey() && hashKey.equals(((MetaData) iObj).getHashKey())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return null != hashKey ? hashKey.hashCode() : 0;
    }

}

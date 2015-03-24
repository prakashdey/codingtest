package com.snapdeal.objectstore.api;

import com.snapdeal.objectstore.dto.DataBean;

public interface IStorageProcessorService {

    /**
     * Add an object with 'blob' content into the system and return an 'id'.
     * 
     * @param blob
     * @return id
     */
    long put(DataBean data);

    /**
     * Retrieve the contents of an object identified by �id�. NULL if doesn�t
     * exist
     * 
     * @param id
     * @return
     */
    DataBean get(long id);

    /**
     * Delete an object identified by �id�
     * 
     * @param id
     */
    boolean delete(long id);

}

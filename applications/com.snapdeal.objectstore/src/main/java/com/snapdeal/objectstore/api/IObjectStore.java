package com.snapdeal.objectstore.api;

/**
 * Object Store API.
 * 
 * @author pdey
 */
public interface IObjectStore {

	/**
	 * Add an object with ‘blob’ content into the system and return an 'id'.
	 * 
	 * @param blob
	 * @return id
	 */
	long put(byte[] blob);

	/**
	 * Retrieve the contents of an object identified by ‘id’. NULL if doesn’t
	 * exist
	 * 
	 * @param id
	 * @return
	 */
	byte[] get(long id);

	/**
	 * Delete an object identified by ‘id’
	 * 
	 * @param id
	 */
	void delete(long id);
}
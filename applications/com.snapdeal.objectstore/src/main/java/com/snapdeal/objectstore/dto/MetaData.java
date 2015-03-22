package com.snapdeal.objectstore.dto;

/**
 * This class holds metadata related to blob data. This information will be used
 * for writing operations on the data.
 * 
 * @author pdey
 */

public class MetaData {

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

}

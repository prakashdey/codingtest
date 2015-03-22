package com.snapdeal.objectstore.dto;

import java.io.Serializable;

public class DataBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private long id;
	private byte[] blob;

	public byte[] getBlob() {
		return blob;
	}

	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
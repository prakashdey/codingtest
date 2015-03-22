package com.snapdeal.objectstore.utility;

import com.snapdeal.objectstore.dto.DataBean;

/**
 * This is an utility to create DataBean from blob data.
 * 
 * @author pdey
 *
 */
public class BlobToDataTransformer {

	private BlobToDataTransformer() {
	}

	private static final BlobToDataTransformer INSTANCE = new BlobToDataTransformer();

	public static BlobToDataTransformer getInstance() {
		return INSTANCE;
	}

	/**
	 * Following task is performed while creating the DataBean.
	 * <ul>
	 * <li>Set byte data into DataBean</li>
	 * <li>Generate unique id using IDGenerator</li>
	 * </ul>
	 *
	 * @param data
	 * @return
	 */
	public DataBean transform(byte[] data) {
		DataBean dataBean = new DataBean();
		dataBean.setBlob(data);
		dataBean.setId(IDGenerator.getNextId());
		return dataBean;
	}
}
package com.snapdeal.objectstore.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.snapdeal.objectstore.common.ConfigurationConstants;
import com.snapdeal.objectstore.common.ConfigurationConstants.ConfigurationConstantKeys;
import com.snapdeal.objectstore.dto.DataBean;

/**
 * This is an utility file processing file for file operations.
 * 
 * @author pdey
 *
 */
public class FileSystemObjectStorage {

    private static final Logger LOGGER = Logger.getLogger(FileSystemObjectStorage.class.getName());

    private static String FOLDER_LOCATION = ConfigurationConstants.getProperty(ConfigurationConstantKeys.FOLDER_LOCATION);

    /**
     * Reads the file content and returns the {@link DataBean}.
     * 
     * @param id
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public DataBean read(long id) throws IOException, ClassNotFoundException {
        try (FileInputStream fin = new FileInputStream(getFileName(id));
                ObjectInputStream ois = new ObjectInputStream(fin);) {
            Object dataBean = ois.readObject();
            if (dataBean != null && dataBean instanceof DataBean) {
                return (DataBean) dataBean;
            } else {
                LOGGER.log(Level.SEVERE, "Object for id ##" + id + "## not present.");
                throw new ClassNotFoundException("");
            }
        }
    }

    /**
     * Reads the file content and returns the {@link DataBean}.
     * 
     * @param id
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public DataBean read(String encodedKey) throws IOException, ClassNotFoundException {
        String fileName = getFileName(encodedKey);

        try (FileInputStream fin = new FileInputStream(fileName);
                ObjectInputStream ois = new ObjectInputStream(fin);) {
            Object dataBean = ois.readObject();
            if (dataBean != null && dataBean instanceof DataBean) {
                return (DataBean) dataBean;
            } else {
                LOGGER.log(Level.SEVERE, "Object for id ##" + encodedKey + "## not present.");
                throw new ClassNotFoundException("");
            }
        }
    }

    private String getFileName(String iEncodedKey) {
        return new String(FOLDER_LOCATION + "storage" + iEncodedKey + ".ser");
    }

    /**
     * Creates a new file and writes the contents.
     * 
     * @param id
     * @param blob
     * @throws IOException
     */
    public void write(DataBean blob) throws IOException {

        if (null != blob)
            try (FileOutputStream fout = new FileOutputStream(getFileName(blob.getId()), false);
                    ObjectOutputStream oos = new ObjectOutputStream(fout)) {
                oos.writeObject(blob);
            }
    }

    /**
     * Creates a new file and writes the contents.
     * 
     * @param id
     * @param blob
     * @throws IOException
     */
    public void write(String iEncodedKey, DataBean blob) throws IOException {

        if (null != blob)
            try (FileOutputStream fout = new FileOutputStream(getFileName(iEncodedKey), false);
                    ObjectOutputStream oos = new ObjectOutputStream(fout)) {
                oos.writeObject(blob);
            }
    }

    /**
     * Deletes the file for the corresponding 'id'.
     * 
     * @param id
     */
    public boolean delete(String id) {
        File file = new File(getFileName(id));
        try {
            FileUtils.forceDelete(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes the file for the corresponding 'id'.
     * 
     * @param id
     */
    public boolean delete(long id) {
        File file = new File(getFileName(id));
        try {
            FileUtils.forceDelete(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utility method to get file name from id. This used the folder location
     * above and creates for the complete location.
     * 
     * @return file name for a particular object.
     */
    private String getFileName(long id) {
        return new String(FOLDER_LOCATION + "storage" + id + ".ser");
    }
}
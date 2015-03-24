package com.snapdeal.objectstore.service;

public interface IFileSystemStorageService {

    public byte[] read(String fileName);

    public boolean write(byte[] data, String fileName);

    public boolean delete(String fileName);
}

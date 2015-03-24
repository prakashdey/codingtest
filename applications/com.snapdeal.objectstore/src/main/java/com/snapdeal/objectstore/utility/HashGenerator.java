package com.snapdeal.objectstore.utility;

import org.apache.commons.codec.digest.DigestUtils;

public class HashGenerator {

    public static String getHashCode(byte[] data) {

        assert (null != data);
        String checkSumEncodedHashCode = DigestUtils.md5Hex(data);
        return checkSumEncodedHashCode;
    }

}

package com.architjn.myapp.utils;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created by architjn on 10/10/2016.
 */

public class Utils {

    public static String getPassword(String username) {
        byte bytes[] = username.getBytes();
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        long checksumValue = checksum.getValue();
        return String.format("%08X", checksumValue);
    }
}

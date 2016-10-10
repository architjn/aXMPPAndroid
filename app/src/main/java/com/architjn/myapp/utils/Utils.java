package com.architjn.myapp.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by architjn on 10/10/2016.
 */

public class Utils {

    public static String getPassword(String username) {
        try {
            String key = "Bar12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(username.getBytes());
            return new String(encrypted);
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                NoSuchPaddingException | BadPaddingException |
                IllegalBlockSizeException e) {
            e.printStackTrace();
            return "keyfail";
        }
    }

}

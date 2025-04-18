package org.rapid.userservice.util;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

public class EncryptorUtil {
    @Autowired
    private StringEncryptor encryptor;

    public String encrypt(String input) {
        return encryptor.encrypt(input);
    }

    public String decrypt(String encrypted) {
        return encryptor.decrypt(encrypted);
    }
}

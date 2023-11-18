package com.tzi.lab.services.encrypt;

import com.tzi.lab.entities.EncryptionTemplate;
import com.tzi.lab.entities.KeyGenOut;

public interface EncryptionService {
    KeyGenOut generateKeys() throws Exception;
    String encryptRC5(byte[] input, int iv, String key);
    String encryptRSA(byte[] input, String publicKeyHex) throws Exception;
    String decryptRC5(byte[] input, String key, int iv);
    String decryptRSA(byte[] input, String privateKeyHex) throws Exception;
}

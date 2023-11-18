package com.tzi.lab.services.encrypt;

import com.tzi.lab.entities.EncryptionTemplate;

public interface EncryptionService {
    String generateKeys() throws Exception;
    String encryptRC5(byte[] input, int iv, String key);
    String encryptRSA(byte[] input, String publicKeyHex) throws Exception;
    String decryptRC5(byte[] input, String key, int iv);
    String decryptRSA(byte[] input, String privateKeyHex);
}

package com.tzi.lab.services.encrypt;

import com.tzi.lab.entities.EncryptionTemplate;

public interface EncryptionService {
    String generateKeys(String type);
    String encrypt(String type, EncryptionTemplate encryptionTemplate);
    byte[] encryptFile(String type, byte[] file);
}

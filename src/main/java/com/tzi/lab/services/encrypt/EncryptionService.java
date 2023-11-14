package com.tzi.lab.services.encrypt;

import com.tzi.lab.entities.EncryptionTemplate;

public interface EncryptionService {
    String generateKeys() throws Exception;
    String encryptRC5(EncryptionTemplate encryptionTemplate);
    String encryptRSA(EncryptionTemplate encryptionTemplate);
    byte[] encryptFileRC5(byte[] file);
    byte[] encryptFileRSA(byte[] file);
}

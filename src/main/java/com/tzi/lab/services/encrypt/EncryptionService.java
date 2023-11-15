package com.tzi.lab.services.encrypt;

import com.tzi.lab.entities.DecryptionTemplate;
import com.tzi.lab.entities.EncryptionTemplate;

public interface EncryptionService {
    String generateKeys() throws Exception;
    String encryptRC5(EncryptionTemplate encryptionTemplate);
    String encryptRSA(EncryptionTemplate encryptionTemplate);
    String decryptRC5(DecryptionTemplate encryptionTemplate);
    String encryptFileRC5(byte[] file, String key);
    String encryptFileRSA(byte[] file, String key);
    String decryptFileRC5(byte[] file, String key, int iv);
}

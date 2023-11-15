package com.tzi.lab.services.encrypt;

import com.tzi.lab.entities.EncryptionTemplate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tzi_lib.*;

import java.security.KeyPair;
import static org.tzi_lib.ByteConverter.bytesToHex;

@Service
@AllArgsConstructor
public class EncryptionServiceImpl implements EncryptionService{

    @Autowired
    private final PseudoRandom pseudoRandom;

    @Autowired
    private final MD5Hash md5Hash;

    @Autowired
    private final RC5CBCPad rc5CBCPad;

    @Autowired
    private final RSA rsa;
    @Override
    public String generateKeys() throws Exception {
        KeyPair keyPair = rsa.generateKeyPair();
        byte[] publicKey = keyPair.getPublic().getEncoded();
        byte[] privateKey = keyPair.getPrivate().getEncoded();
        return "PublicKey: " +
                bytesToHex(publicKey) +
                "\n" +
                "PrivateKey: " +
                bytesToHex(privateKey);
    }

    @Override
    public String encryptRC5(EncryptionTemplate encryptionTemplate) {
        byte[] iv = rc5CBCPad.intToByteArray(pseudoRandom.generateRandomInt(16807,
                2147483447,
                0,
                (int)(System.currentTimeMillis()%Integer.MAX_VALUE)));
        byte[] key = bytesToHex(md5Hash.computeMD5(encryptionTemplate.getKey().getBytes())).getBytes();
        byte[] input = encryptionTemplate.getMessage().getBytes();
        return rc5CBCPad.encryptBlocks(input, key, iv);
    }

    @Override
    public String encryptRSA(EncryptionTemplate encryptionTemplate) {
        return null;
    }

    @Override
    public String decryptRC5(EncryptionTemplate encryptionTemplate) {
        return null;
    }

    @Override
    public String encryptFileRC5(byte[] file, String key) {
        byte[] iv = rc5CBCPad.intToByteArray(pseudoRandom.generateRandomInt(16807,
                2147483447,
                0,
                (int)(System.currentTimeMillis()%Integer.MAX_VALUE)));
        byte[] keyBytes = bytesToHex(md5Hash.computeMD5(key.getBytes())).getBytes();
        return rc5CBCPad.encryptBlocks(file, keyBytes, iv);
    }

    @Override
    public String encryptFileRSA(byte[] file, String key) {
        return null;
    }

    @Override
    public String decryptFileRC5(byte[] file, String key) {
        return null;
    }
}

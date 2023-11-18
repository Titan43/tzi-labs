package com.tzi.lab.services.encrypt;

import com.tzi.lab.entities.DecryptionTemplate;
import com.tzi.lab.entities.EncryptionTemplate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tzi_lib.*;

import java.security.KeyPair;
import java.util.Arrays;

import static org.tzi_lib.ByteConverter.bytesToHex;
import static org.tzi_lib.ByteConverter.hexStringToBytes;

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
        return bytesToHex(publicKey) +
                "\n" +
                bytesToHex(privateKey);
    }

    @Override
    public String encryptRC5(byte[] input, String key) {
        int iv = pseudoRandom.generateRandomInt(16807,
                2147483447,
                0,
                (int)(System.currentTimeMillis()%Integer.MAX_VALUE));
        byte[] keyBytes = bytesToHex(md5Hash.computeMD5(key.getBytes())).getBytes();
        return iv+"\n"+rc5CBCPad.encryptBlocks(input, keyBytes, rc5CBCPad.intToByteArray(iv));
    }

    @Override
    public String encryptRSA(EncryptionTemplate encryptionTemplate) {
        return null;
    }

    @Override
    public String decryptRC5(byte[] input, String key, int iv) {
        byte[] inputBytes = hexStringToBytes(new String(input));
        byte[] keyBytes = bytesToHex(md5Hash.computeMD5(key.getBytes())).getBytes();
        byte[] ivBytes = rc5CBCPad.intToByteArray(iv);
        return rc5CBCPad.decryptBlocks(inputBytes, keyBytes, ivBytes);
    }
}

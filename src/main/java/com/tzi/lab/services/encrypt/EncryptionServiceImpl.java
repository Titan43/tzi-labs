package com.tzi.lab.services.encrypt;

import com.tzi.lab.entities.KeyGenOut;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tzi_lib.*;

import java.security.KeyPair;

import static org.tzi_lib.ByteConverter.bytesToHex;
import static org.tzi_lib.ByteConverter.hexStringToBytes;

@Service
@AllArgsConstructor
public class EncryptionServiceImpl implements EncryptionService{

    @Autowired
    private final MD5Hash md5Hash;

    @Autowired
    private final RC5CBCPad rc5CBCPad;

    @Autowired
    private final RSA rsa;
    @Override
    public KeyGenOut generateKeys() throws Exception {
        KeyPair keyPair = rsa.generateKeyPair();
        byte[] publicKey = keyPair.getPublic().getEncoded();
        byte[] privateKey = keyPair.getPrivate().getEncoded();
        return new KeyGenOut(bytesToHex(publicKey),
                bytesToHex(privateKey));
    }

    @Override
    public String encryptRC5(byte[] input, int iv, String key) {
        byte[] keyBytes = bytesToHex(md5Hash.computeMD5(key.getBytes())).getBytes();
        return rc5CBCPad.encryptBlocks(input, keyBytes, rc5CBCPad.intToByteArray(iv));
    }

    @Override
    public String encryptRSA(byte[] input, String publicKeyHex) throws Exception {
        byte[] publicKey = hexStringToBytes(publicKeyHex);
        return rsa.encryptBlocks(input, publicKey);
    }

    @Override
    public String decryptRC5(byte[] input, String key, int iv) {
        byte[] inputBytes = hexStringToBytes(new String(input));
        byte[] keyBytes = bytesToHex(md5Hash.computeMD5(key.getBytes())).getBytes();
        byte[] ivBytes = rc5CBCPad.intToByteArray(iv);
        return rc5CBCPad.decryptBlocks(inputBytes, keyBytes, ivBytes);
    }

    @Override
    public String decryptRSA(byte[] input, String privateKeyHex) throws Exception {
        byte[] inputBytes = hexStringToBytes(new String(input));
        byte[] keyBytes = hexStringToBytes(privateKeyHex);
        return rsa.decryptBlocks(inputBytes, keyBytes);
    }
}
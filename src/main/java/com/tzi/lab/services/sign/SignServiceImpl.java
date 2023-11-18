package com.tzi.lab.services.sign;

import com.tzi.lab.entities.KeyGenOut;
import com.tzi.lab.entities.Signature;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tzi_lib.DSA;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import static org.tzi_lib.ByteConverter.bytesToHex;
import static org.tzi_lib.ByteConverter.hexStringToBytes;

@Service
@AllArgsConstructor
public class SignServiceImpl implements SignService{
    @Autowired
    private final DSA dsa;
    @Override
    public KeyGenOut generateKeys() throws NoSuchAlgorithmException {
        KeyPair keyPair = dsa.generateDSAKeyPair();
        byte[] publicKey = keyPair.getPublic().getEncoded();
        byte[] privateKey = keyPair.getPrivate().getEncoded();
        return new KeyGenOut(bytesToHex(publicKey),
                bytesToHex(privateKey));
    }

    @Override
    public Signature sign(byte[] input, String key) throws Exception {
        byte[] keyBytes = hexStringToBytes(key);
        String signature = dsa.sign(input, keyBytes);
        return new Signature(signature);
    }

    @Override
    public boolean verify(byte[] input, String signature, String key) throws Exception {
        byte[] signatureBytes = hexStringToBytes(signature);
        byte[] keyBytes = hexStringToBytes(key);
        return dsa.verify(input, signatureBytes, keyBytes);
    }
}

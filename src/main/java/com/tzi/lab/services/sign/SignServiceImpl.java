package com.tzi.lab.services.sign;

import com.tzi.lab.entities.KeyGenOut;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tzi_lib.DSA;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import static org.tzi_lib.ByteConverter.bytesToHex;

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
}

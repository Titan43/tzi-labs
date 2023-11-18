package com.tzi.lab.services.sign;

import com.tzi.lab.entities.KeyGenOut;
import com.tzi.lab.entities.Signature;

import java.security.NoSuchAlgorithmException;

public interface SignService {
    KeyGenOut generateKeys() throws NoSuchAlgorithmException;

    Signature sign(byte[] input, String key) throws Exception;

    boolean verify(byte[] input, String signature, String key) throws Exception;
}

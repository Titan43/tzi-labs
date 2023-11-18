package com.tzi.lab.services.sign;

import com.tzi.lab.entities.KeyGenOut;

import java.security.NoSuchAlgorithmException;

public interface SignService {
    KeyGenOut generateKeys() throws NoSuchAlgorithmException;
}

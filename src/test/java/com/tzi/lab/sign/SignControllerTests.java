package com.tzi.lab.sign;

import com.tzi.lab.controllers.SignController;
import com.tzi.lab.entities.EncryptionTemplate;
import com.tzi.lab.entities.HashInputTemplate;
import com.tzi.lab.entities.KeyGenOut;
import com.tzi.lab.entities.Signature;
import com.tzi.lab.services.sign.SignService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SignControllerTests {

    @Mock
    private SignService signService;

    @InjectMocks
    private SignController signController;

    @Test
    void generateKeys() throws NoSuchAlgorithmException {
        when(signService.generateKeys()).thenReturn(new KeyGenOut("mockedPublicKey", "mockedPrivateKey"));
        ResponseEntity<?> responseEntity = signController.generateKeys();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void signWithString() throws Exception {
        when(signService.sign(any(byte[].class), anyString())).thenReturn(new Signature("mockedSignature"));

        ResponseEntity<?> responseEntity = signController.sign(new EncryptionTemplate("testMessage", "testKey", "123"));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void signWithFile() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("testFileContent".getBytes());

        when(signService.sign(any(byte[].class), anyString())).thenReturn(new Signature("mockedSignature"));

        ResponseEntity<?> responseEntity = signController.sign(file, new EncryptionTemplate("testMessage", "testKey", "123"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void verifyWithString() throws Exception {
        when(signService.verify(any(byte[].class), anyString(), anyString())).thenReturn(true);

        ResponseEntity<?> responseEntity = signController.verify(
                new HashInputTemplate("testMessage"), new Signature("mockedSignature", "mockedKey"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void verifyWithFile() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("testFileContent".getBytes());

        when(signService.verify(any(byte[].class), anyString(), anyString())).thenReturn(true);

        ResponseEntity<?> responseEntity = signController.verify(file, new Signature("mockedSignature", "mockedKey"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void generateKeysFailure() throws Exception {
        doThrow(new NoSuchAlgorithmException("Test Exception")).when(signService).generateKeys();
        ResponseEntity<?> responseEntity = signController.generateKeys();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Keys cannot be generated right now", responseEntity.getBody());
    }

    @Test
    void signFailure() throws Exception {
        doThrow(new Exception("Test Exception")).when(signService).sign(any(byte[].class), anyString());

        ResponseEntity<?> responseEntity = signController.sign(new EncryptionTemplate("key", "message", "123"));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid key passed", responseEntity.getBody());
    }

    @Test
    void signFileFailure() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "fileContent".getBytes());

        doThrow(new Exception("Test Exception")).when(signService).sign(any(byte[].class), anyString());

        ResponseEntity<?> responseEntity = signController.sign(file, new EncryptionTemplate("msg", "key", "123"));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid key passed", responseEntity.getBody());
    }
    @Test
    void verifyFailure() throws Exception {
        HashInputTemplate message = new HashInputTemplate("testMessage");
        Signature signature = new Signature("testSignature", "testKey");

        doThrow(new Exception("Test Exception")).when(signService)
                .verify(any(byte[].class), eq(signature.getSignature()), eq(signature.getKey()));

        ResponseEntity<?> responseEntity = signController.verify(message, signature);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid key or signature passed", responseEntity.getBody());
    }
    @Test
    void verifyFileFailure() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "fileContent".getBytes());
        Signature signature = new Signature("testSignature", "testKey");

        doThrow(new Exception("Test Exception")).when(signService)
                .verify(any(byte[].class), eq(signature.getSignature()), eq(signature.getKey()));
        ResponseEntity<?> responseEntity = signController.verify(file, signature);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid key or signature passed", responseEntity.getBody());
    }
}

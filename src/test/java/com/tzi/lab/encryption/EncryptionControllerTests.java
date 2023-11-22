package com.tzi.lab.encryption;

import com.tzi.lab.controllers.EncryptionController;
import com.tzi.lab.entities.EncryptionTemplate;
import com.tzi.lab.entities.KeyGenOut;
import com.tzi.lab.services.encrypt.EncryptionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EncryptionControllerTests {

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private EncryptionController encryptionController;

    @Test
    public void testGenerateKeys() throws Exception {
        KeyGenOut keyGenOut = new KeyGenOut("mockedPublic", "mockedPrivate");
        when(encryptionService.generateKeys()).thenReturn(keyGenOut);

        ResponseEntity<?> responseEntity = encryptionController.generateKeys();

        assertEquals(keyGenOut, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testEncryptRC5() {
        when(encryptionService.encryptRC5(any(byte[].class), anyInt(), anyString())).thenReturn("Mocked RC5 Encryption");

        EncryptionTemplate encryptionTemplate = new EncryptionTemplate();
        encryptionTemplate.setKey("testKey");
        encryptionTemplate.setIv("123");
        encryptionTemplate.setMessage("testMessage");
        ResponseEntity<String> responseEntity = encryptionController.encrypt("rc5", encryptionTemplate);

        assertEquals("Mocked RC5 Encryption", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDecryptRC5() {
        when(encryptionService.decryptRC5(any(byte[].class), anyString(), anyInt())).thenReturn("Mocked RC5 Decryption");

        EncryptionTemplate decryptionTemplate = new EncryptionTemplate();
        decryptionTemplate.setKey("testKey");
        decryptionTemplate.setMessage("testMessage");
        decryptionTemplate.setIv("123");
        ResponseEntity<String> responseEntity = encryptionController.decrypt("rc5", decryptionTemplate);

        assertEquals("Mocked RC5 Decryption", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void testFileEncryptRC5() throws IOException {
        when(encryptionService.encryptRC5(any(byte[].class), anyInt(), anyString())).thenReturn("Mocked RC5 File Encryption");

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("testFileContent".getBytes());

        EncryptionTemplate encryptionTemplate = new EncryptionTemplate();
        encryptionTemplate.setKey("testKey");
        encryptionTemplate.setIv("123");
        ResponseEntity<String> responseEntity = encryptionController.encryptFile("rc5", file, encryptionTemplate);

        assertEquals("Mocked RC5 File Encryption", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testEncryptRSA() throws Exception {
        when(encryptionService.encryptRSA(any(byte[].class), anyString())).thenReturn("Mocked RSA Encryption");

        EncryptionTemplate encryptionTemplate = new EncryptionTemplate();
        encryptionTemplate.setKey("testKey");
        encryptionTemplate.setMessage("testMessage");
        ResponseEntity<String> responseEntity = encryptionController.encrypt("rsa", encryptionTemplate);

        assertEquals("Mocked RSA Encryption", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void testEncryptFileRSA() throws Exception {
        when(encryptionService.encryptRSA(any(byte[].class), anyString())).thenReturn("Mocked RSA File Encryption");

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("testFileContent".getBytes());

        EncryptionTemplate encryptionTemplate = new EncryptionTemplate();
        encryptionTemplate.setKey("testKey");
        ResponseEntity<String> responseEntity = encryptionController.encryptFile("rsa", file, encryptionTemplate);

        assertEquals("Mocked RSA File Encryption", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDecryptRSA() throws Exception {
        when(encryptionService.decryptRSA(any(byte[].class), anyString())).thenReturn("Mocked RSA Decryption");

        EncryptionTemplate decryptionTemplate = new EncryptionTemplate();
        decryptionTemplate.setKey("testKey");
        decryptionTemplate.setMessage("testMessage");
        ResponseEntity<String> responseEntity = encryptionController.decrypt("rsa", decryptionTemplate);

        assertEquals("Mocked RSA Decryption", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDecryptFileRSA() throws Exception {
        when(encryptionService.decryptRSA(any(byte[].class), anyString())).thenReturn("Mocked RSA File Decryption");

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("testFileContent".getBytes());

        EncryptionTemplate decryptionTemplate = new EncryptionTemplate();
        decryptionTemplate.setKey("testKey");
        ResponseEntity<String> responseEntity = encryptionController.decryptFile("rsa", file, decryptionTemplate);

        assertEquals("Mocked RSA File Decryption", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    void generateKeysFailure() throws Exception {
        doThrow(new NoSuchAlgorithmException("Test Exception")).when(encryptionService).generateKeys();
        ResponseEntity<?> responseEntity = encryptionController.generateKeys();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Keys cannot be generated right now", responseEntity.getBody());
    }
    @Test
    void encryptFailure(){
        doThrow(new NumberFormatException()).when(encryptionService).encryptRC5(any(byte[].class), anyInt(), anyString());

        ResponseEntity<String> responseEntity = encryptionController.encrypt("rc5", new EncryptionTemplate("key", "message", "123"));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid starting vector passed", responseEntity.getBody());
    }

    @Test
    void encryptFileFailure() {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "fileContent".getBytes());

        doThrow(new NumberFormatException()).when(encryptionService).encryptRC5(any(byte[].class), anyInt(), anyString());

        ResponseEntity<String> responseEntity = encryptionController.encryptFile("rc5", file, new EncryptionTemplate("msg","key", "123"));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid starting vector passed", responseEntity.getBody());
    }
}



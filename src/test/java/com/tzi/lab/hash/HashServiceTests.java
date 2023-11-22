package com.tzi.lab.hash;

import com.tzi.lab.entities.HashInputTemplate;
import com.tzi.lab.services.hash.HashServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.tzi_lib.MD5Hash;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class HashServiceTests {

    @Mock
    private MD5Hash customMD5Hash;

    @InjectMocks
    private HashServiceImpl hashService;

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.reset(customMD5Hash);
    }

    @Test
    void hashCustomMD5Success() {
        when(customMD5Hash.computeMD5(any())).thenReturn(new byte[]{1, 2, 3});

        ResponseEntity<?> responseEntity = hashService.hashCustomMD5(new HashInputTemplate("test message"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("010203", responseEntity.getBody());
    }

    @Test
    void hashCustomMD5NullMessage() {
        ResponseEntity<?> responseEntity = hashService.hashCustomMD5(new HashInputTemplate(null));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Message cannot be null", responseEntity.getBody());
    }

    @Test
    void hashCustomMD5FileSuccess() throws IOException {
        MultipartFile file = org.mockito.Mockito.mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        when(customMD5Hash.computeMD5(any())).thenReturn(new byte[]{1, 2, 3});

        ResponseEntity<?> responseEntity = hashService.hashCustomMD5File(file);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("010203", responseEntity.getBody());
    }

    @Test
    void hashCustomMD5FileEmptyFile() {
        MultipartFile file = org.mockito.Mockito.mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        ResponseEntity<?> responseEntity = hashService.hashCustomMD5File(file);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("File cannot be empty", responseEntity.getBody());
    }

    @Test
    void hashCustomMD5FileIOException() throws IOException {
        MultipartFile file = org.mockito.Mockito.mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenThrow(new IOException("Test IOException"));

        ResponseEntity<?> responseEntity = hashService.hashCustomMD5File(file);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("File cannot be read", responseEntity.getBody());
    }
}

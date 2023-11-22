package com.tzi.lab.hash;

import com.tzi.lab.controllers.HashController;
import com.tzi.lab.entities.HashInputTemplate;
import com.tzi.lab.services.hash.HashService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class HashControllerTests {

    @Mock
    private HashService hashService;

    @InjectMocks
    private HashController hashController;

    @Test
    void hashCustomMD5() {
        when(hashService.hashCustomMD5(any(HashInputTemplate.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        HashInputTemplate inputTemplate = new HashInputTemplate("inputMessage");
        ResponseEntity<?> responseEntity = hashController.hashCustomMD5(inputTemplate);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void hashCustomMD5File() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "fileContent".getBytes());
        when(hashService.hashCustomMD5File(file))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> responseEntity = hashController.hashCustomMD5File(file);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}

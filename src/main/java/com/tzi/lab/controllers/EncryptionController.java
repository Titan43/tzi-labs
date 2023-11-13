package com.tzi.lab.controllers;

import com.tzi.lab.entities.EncryptionTemplate;
import com.tzi.lab.services.encrypt.EncryptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/encryption")
public class EncryptionController {
    @Autowired
    private final EncryptionService encryptionService;

    @GetMapping(path = "/generate-key/{type}")
    public ResponseEntity<?> generateKeys(@PathVariable String type){
        return null;
    }
    @PostMapping(path = "/encrypt/{type}")
    public ResponseEntity<?> encrypt(@PathVariable String type, @RequestBody EncryptionTemplate encryptionTemplate){
        return null;
    }

    @PostMapping(path = "/file-encrypt/{type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> encryptFile(@PathVariable String type, @RequestParam("file") MultipartFile file){
        return null;
    }
}

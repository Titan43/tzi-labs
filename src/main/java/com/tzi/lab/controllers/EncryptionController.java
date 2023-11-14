package com.tzi.lab.controllers;

import com.tzi.lab.entities.EncryptionTemplate;
import com.tzi.lab.services.encrypt.EncryptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping(path = "/generate-keys")
    public ResponseEntity<?> generateKeys(){
        try{
            return new ResponseEntity<>(encryptionService.generateKeys(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Keys cannot be generated right now", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(path = "/encrypt/{type}")
    public ResponseEntity<?> encrypt(@PathVariable String type, @RequestBody EncryptionTemplate encryptionTemplate){
        String encryptedText;
        if(type.equals("rc5")){
            encryptedText = encryptionService.encryptRC5(encryptionTemplate);
        }
        else if(type.equals("rsa")){
            encryptedText = encryptionService.encryptRSA(encryptionTemplate);
        }
        else return new ResponseEntity<>("Type should be RC5 or RSA", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(encryptedText, HttpStatus.OK);
    }

    @PostMapping(path = "/file-encrypt/{type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> encryptFile(@PathVariable String type, @RequestParam("file") MultipartFile file){
        return null;
    }
}

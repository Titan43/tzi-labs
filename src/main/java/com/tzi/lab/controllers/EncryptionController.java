package com.tzi.lab.controllers;

import com.tzi.lab.entities.DecryptionTemplate;
import com.tzi.lab.entities.EncryptionTemplate;
import com.tzi.lab.services.encrypt.EncryptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        if(encryptionTemplate.getKey() == null || encryptionTemplate.getMessage() == null){
            return new ResponseEntity<>("Message and key values cannot be null", HttpStatus.BAD_REQUEST);
        }
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

    @PostMapping(path = "/decrypt/{type}")
    public ResponseEntity<?> decrypt(@PathVariable String type, @RequestBody DecryptionTemplate decryptionTemplate){
        if(decryptionTemplate.getKey() == null || decryptionTemplate.getMessage() == null){
            return new ResponseEntity<>("Message and key values cannot be null", HttpStatus.BAD_REQUEST);
        }
        String encryptedText;
        if(type.equals("rc5")){
            try {
                Integer.parseInt(decryptionTemplate.getIv());
                encryptedText = encryptionService.decryptRC5(decryptionTemplate);
            }
            catch (NumberFormatException e){
                return new ResponseEntity<>("Invalid starting vector passed", HttpStatus.BAD_REQUEST);
            }
        }
        else if(type.equals("rsa")){
            throw new IllegalArgumentException("RSA currently unsupported");
        }
        else return new ResponseEntity<>("Type should be RC5 or RSA", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(encryptedText, HttpStatus.OK);
    }

    @PostMapping(path = "/file-encrypt/{type}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> encryptFile(@PathVariable String type,
                                              @RequestPart("file") MultipartFile file,
                                              @RequestPart("key") EncryptionTemplate encryptionTemplate){
        byte[] data;
        String encryptedText;
        if(encryptionTemplate.getKey() == null)
            return new ResponseEntity<>("Key has to be provided", HttpStatus.BAD_REQUEST);
        try{
            data = file.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>("Invalid file passed", HttpStatus.BAD_REQUEST);
        }
        if(type.equals("rc5")){
            encryptedText = encryptionService.encryptFileRC5(data, encryptionTemplate.getKey());
        }
        else if(type.equals("rsa")){
            encryptedText = encryptionService.encryptFileRSA(data, encryptionTemplate.getKey());
        }
        else return new ResponseEntity<>("Type should be RC5 or RSA", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(encryptedText, HttpStatus.OK);
    }

    @PostMapping(path = "/file-decrypt/{type}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> decryptFile(@PathVariable String type,
                                              @RequestPart("file") MultipartFile file,
                                              @RequestPart("key") DecryptionTemplate decryptionTemplate){
        byte[] data;
        String encryptedText;
        if(decryptionTemplate.getKey() == null)
            return new ResponseEntity<>("Key has to be provided", HttpStatus.BAD_REQUEST);
        try{
            data = file.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>("Invalid file passed", HttpStatus.BAD_REQUEST);
        }
        if(type.equals("rc5")){
            try{
                int iv = Integer.parseInt(decryptionTemplate.getIv());
                encryptedText = encryptionService.decryptFileRC5(data, decryptionTemplate.getKey(), iv);
            }
            catch (NumberFormatException e){
                return new ResponseEntity<>("Invalid starting vector passed", HttpStatus.BAD_REQUEST);
            }
        }
        else if(type.equals("rsa")){
            throw new IllegalArgumentException("RSA currently unsupported");
        }
        else return new ResponseEntity<>("Type should be RC5 or RSA", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(encryptedText, HttpStatus.OK);
    }
}

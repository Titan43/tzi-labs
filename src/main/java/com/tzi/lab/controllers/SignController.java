package com.tzi.lab.controllers;

import com.tzi.lab.entities.EncryptionTemplate;
import com.tzi.lab.entities.HashInputTemplate;
import com.tzi.lab.entities.Signature;
import com.tzi.lab.services.sign.SignService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/esign")
public class SignController {
    private final SignService signService;
    @GetMapping(path = "/generate-keys", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateKeys(){
        try{
            return new ResponseEntity<>(signService.generateKeys(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Keys cannot be generated right now", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(path = "/sign", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sign(@RequestBody EncryptionTemplate encryptionTemplate){
        try {
            return new ResponseEntity<>(signService.sign(encryptionTemplate.getMessage().getBytes(),
                    encryptionTemplate.getKey()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid key passed", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(path = "/file-sign", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> sign(@RequestPart("file") MultipartFile file,
                                  @RequestPart("key") EncryptionTemplate encryptionTemplate){
        byte[] data;
        try{
            data = file.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>("Invalid file passed", HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(signService.sign(data,
                    encryptionTemplate.getKey()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid key passed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/verify", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> verify(@RequestPart("message") HashInputTemplate message,
                                    @RequestPart Signature signature){
        try {
            return new ResponseEntity<>(signService.verify(message.getMessage().getBytes(),
                    signature.getSignature(), signature.getKey()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid key or signature passed", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(path = "/file-verify", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> verify(@RequestPart("file") MultipartFile file,
                                    @RequestPart("signature") Signature signature){
        byte[] data;
        try{
            data = file.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>("Invalid file passed", HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(signService.verify(data,
                    signature.getSignature(), signature.getKey()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid key or signature passed", HttpStatus.BAD_REQUEST);
        }
    }
}

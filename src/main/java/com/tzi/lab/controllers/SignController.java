package com.tzi.lab.controllers;

import com.tzi.lab.entities.EncryptionTemplate;
import com.tzi.lab.services.sign.SignService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(path = "/sign")
    public ResponseEntity<?> sign(@RequestBody EncryptionTemplate encryptionTemplate){
        return null;
    }
    @PostMapping(path = "/file-sign")
    public ResponseEntity<?> sign(@RequestPart("file") MultipartFile file,
                                  @RequestPart("key") EncryptionTemplate encryptionTemplate){
        return null;
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<?> verify(@RequestBody EncryptionTemplate encryptionTemplate){
        return null;
    }
    @PostMapping(path = "/file-verify")
    public ResponseEntity<?> verify(@RequestPart("file") MultipartFile file,
                                    @RequestPart("key") EncryptionTemplate encryptionTemplate){
        return null;
    }
}

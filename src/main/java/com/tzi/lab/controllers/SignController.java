package com.tzi.lab.controllers;

import com.tzi.lab.entities.EncryptionTemplate;
import com.tzi.lab.services.sign.SignService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping(path = "/sign-message")
    public ResponseEntity<?> sign(EncryptionTemplate encryptionTemplate){
        return null;
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<?> verify(){
        return null;
    }
}

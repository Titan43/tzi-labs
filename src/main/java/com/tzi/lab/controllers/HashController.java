package com.tzi.lab.controllers;

import com.tzi.lab.entities.HashInputTemplate;
import com.tzi.lab.services.hash.HashService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/hash")
public class HashController {

    @Autowired
    public final HashService hashService;
    @PostMapping(path = "/custom-md5")
    public ResponseEntity<?> hashCustomMD5(@RequestBody HashInputTemplate hashInputTemplate){
        return hashService.hashCustomMD5(hashInputTemplate);
    }

    @PostMapping(path = "/custom-md5/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> hashCustomMD5File(@RequestParam("file") MultipartFile file){
        return hashService.hashCustomMD5File(file);
    }
}

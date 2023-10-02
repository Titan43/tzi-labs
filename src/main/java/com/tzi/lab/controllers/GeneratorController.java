package com.tzi.lab.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/generate")
public class GeneratorController {
    @PostMapping(path = "/pseudorandom")
    public ResponseEntity<?> generatePseudorandom(){
        return null;
    }
}

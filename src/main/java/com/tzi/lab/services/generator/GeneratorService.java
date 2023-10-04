package com.tzi.lab.services.generator;

import org.springframework.http.ResponseEntity;

public interface GeneratorService {
    ResponseEntity<?> generateSequence(String a, String b, String c, String seed, String size);
}

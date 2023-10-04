package com.tzi.lab.services.generator;

import org.springframework.http.ResponseEntity;

public interface GeneratorService {
    ResponseEntity<?> generateSequence(String a, String b, String c, String initialValue, String size);
    ResponseEntity<String> findPeriod(String a, String b, String c, String initialValue);
}

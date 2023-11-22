package com.tzi.lab.generator;

import com.tzi.lab.services.generator.GeneratorServiceImpl;
import com.tzi.lab.services.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tzi_lib.PseudoRandom;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class GeneratorServiceTests {

    @Mock
    private PseudoRandom pseudoRandom;

    @Mock
    private Validator validator;

    @InjectMocks
    private GeneratorServiceImpl generatorService;

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.reset(pseudoRandom, validator);
    }

    @Test
    void generateSequenceSuccess() {
        when(validator.isValidInt(anyString())).thenReturn(true);
        List<Integer> out = IntStream.rangeClosed(1,5).boxed().toList();
        when(pseudoRandom.generateRandomSequence(anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn(out);
        ResponseEntity<?> responseEntity = generatorService.generateSequence("1", "2", "3", "4", "5");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(out, responseEntity.getBody());
    }

    @Test
    void generateSequenceInvalidValues() {
        when(validator.isValidInt(anyString())).thenReturn(false);

        ResponseEntity<?> responseEntity = generatorService.generateSequence("a", "b", "c", "d", "e");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Incorrect values passed", responseEntity.getBody());
    }

    @Test
    void findPeriodSuccess() {
        when(validator.isValidInt(anyString())).thenReturn(true);

        when(pseudoRandom.getPeriod(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(42);

        ResponseEntity<String> responseEntity = generatorService.findPeriod("1", "2", "3", "4");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Period: 42", responseEntity.getBody());
    }

    @Test
    void findPeriodInvalidValues() {
        when(validator.isValidInt(anyString())).thenReturn(false);

        ResponseEntity<String> responseEntity = generatorService.findPeriod("a", "b", "c", "d");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Incorrect values passed", responseEntity.getBody());
    }
}

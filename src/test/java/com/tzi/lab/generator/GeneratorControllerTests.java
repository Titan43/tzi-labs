package com.tzi.lab.generator;

import com.tzi.lab.controllers.GeneratorController;
import com.tzi.lab.entities.GeneratorInputTemplate;
import com.tzi.lab.services.generator.GeneratorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GeneratorControllerTests {

    @Mock
    private GeneratorService generatorService;

    @InjectMocks
    private GeneratorController generatorController;

    @Test
    void generatePseudorandom() {
        // Mock the behavior of GeneratorService
        when(generatorService.generateSequence(any(), any(), any(), any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Call the controller method
        GeneratorInputTemplate inputTemplate = new GeneratorInputTemplate("1", "2", "3", "4");
        ResponseEntity<?> responseEntity = generatorController.generatePseudorandom(inputTemplate, "10");

        // Verify the result
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void findPseudorandomPeriod() {
        // Mock the behavior of GeneratorService
        when(generatorService.findPeriod(any(), any(), any(), any()))
                .thenReturn(new ResponseEntity<>("pseudorandomPeriod", HttpStatus.OK));
        GeneratorInputTemplate inputTemplate = new GeneratorInputTemplate("1", "2", "3", "4");
        ResponseEntity<?> responseEntity = generatorController.findPseudorandomPeriod(inputTemplate);

        // Verify the result
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("pseudorandomPeriod", responseEntity.getBody());
    }
}

package com.tzi.lab.controllers;

import com.tzi.lab.entities.GeneratorInputTemplate;
import com.tzi.lab.services.generator.GeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/generate")
public class GeneratorController {
    @Autowired
    private final GeneratorService generatorService;
    @PostMapping(path = "/pseudorandom")
    public ResponseEntity<?> generatePseudorandom(
            @RequestBody GeneratorInputTemplate inputTemplate, @RequestParam(name = "size") String size
    ){
        return generatorService.generateSequence(
                inputTemplate.getA(), inputTemplate.getM(), inputTemplate.getC(), inputTemplate.getInitialValue(),
                size);
    }

    @PostMapping(path = "/pseudorandom/period")
    public ResponseEntity<?> findPseudorandomPeriod(
            @RequestBody GeneratorInputTemplate inputTemplate
    ){
        return generatorService.findPeriod(
                inputTemplate.getA(), inputTemplate.getM(), inputTemplate.getC(), inputTemplate.getInitialValue());
    }
}

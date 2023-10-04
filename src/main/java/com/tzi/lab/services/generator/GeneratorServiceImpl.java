package com.tzi.lab.services.generator;

import com.tzi.lab.services.validator.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tzi_lib.PseudoRandom;

@Service
@AllArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private final PseudoRandom pseudoRandom;

    @Autowired
    private final Validator validator;
    @Override
    public ResponseEntity<?> generateSequence(String a, String b, String c, String seed, String size) {

        if(validator.isValidInt(a)
                && validator.isValidInt(b)
                && validator.isValidInt(c)
                && validator.isValidInt(seed)
                && validator.isValidInt(size))
            return new ResponseEntity<>(pseudoRandom.generateRandomSequence(Integer.valueOf(a),
                    Integer.valueOf(b),
                    Integer.valueOf(c),
                    Integer.valueOf(seed),
                    Integer.valueOf(size)), HttpStatus.OK);
        return new ResponseEntity<>("Incorrect values passed", HttpStatus.BAD_REQUEST);
    }
}

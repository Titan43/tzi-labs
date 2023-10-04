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
    public ResponseEntity<?> generateSequence(String a, String m, String c, String initialValue, String size) {

        if(validator.isValidInt(a)
                && validator.isValidInt(m)
                && validator.isValidInt(c)
                && validator.isValidInt(initialValue)
                && validator.isValidInt(size))
            return new ResponseEntity<>(pseudoRandom.generateRandomSequence(Integer.valueOf(a),
                    Integer.valueOf(m),
                    Integer.valueOf(c),
                    Integer.valueOf(initialValue),
                    Integer.valueOf(size)), HttpStatus.OK);
        return new ResponseEntity<>("Incorrect values passed", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> findPeriod(String a, String m, String c, String initialValue) {
        if(validator.isValidInt(a)
                && validator.isValidInt(m)
                && validator.isValidInt(c)
                && validator.isValidInt(initialValue))
            return new ResponseEntity<>("Period: "+pseudoRandom.getPeriod(
                    Integer.valueOf(a),
                    Integer.valueOf(m),
                    Integer.valueOf(c),
                    Integer.valueOf(initialValue)), HttpStatus.OK
            );
        return new ResponseEntity<>("Incorrect values passed", HttpStatus.BAD_REQUEST);
    }
}

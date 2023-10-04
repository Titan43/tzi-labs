package com.tzi.lab.services.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ValidatorImpl implements Validator{
    @Override
    public boolean isValidInt(String sequence) {
        try{
            Integer.parseInt(sequence);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }
}

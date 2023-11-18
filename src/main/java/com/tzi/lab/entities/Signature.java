package com.tzi.lab.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Signature {
    private String signature;
    private String key;
    public Signature(String signature){
        this.signature=signature;
    }
}

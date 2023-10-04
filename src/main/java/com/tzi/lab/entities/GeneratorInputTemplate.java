package com.tzi.lab.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneratorInputTemplate {
    private String a;
    private String m;
    private String c;
    private String initialValue;
}

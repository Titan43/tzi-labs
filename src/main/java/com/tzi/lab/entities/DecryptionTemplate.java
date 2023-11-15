package com.tzi.lab.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DecryptionTemplate {
    private String message;
    private String key;
    private String iv;
}

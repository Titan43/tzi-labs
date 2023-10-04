package com.tzi.lab.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tzi_lib.PseudoRandom;

@Configuration
@AllArgsConstructor
public class UtilConfig {
    @Bean
    public PseudoRandom pseudoRandom(){
        return new PseudoRandom();
    }
}

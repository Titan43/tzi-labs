package com.tzi.lab.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tzi_lib.*;

@Configuration
@AllArgsConstructor
public class UtilConfig {
    @Bean
    public PseudoRandom pseudoRandom(){
        return new PseudoRandom();
    }

    @Bean
    public MD5Hash customMD5Hash(){
        return new MD5Hash();
    }

    @Bean
    public RC5CBCPad rc5CBCPad(){
        return new RC5CBCPad();
    }

    @Bean
    public RSA rsa(){
        return new RSA();
    }

    @Bean
    public DSA dsa(){
        return new DSA();
    }
}

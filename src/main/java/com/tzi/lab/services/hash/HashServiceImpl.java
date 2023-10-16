package com.tzi.lab.services.hash;

import com.tzi.lab.entities.HashInputTemplate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tzi_lib.MD5Hash;

import java.io.IOException;

@Service
@AllArgsConstructor
public class HashServiceImpl implements HashService{

    @Autowired
    public final MD5Hash customMD5Hash;
    @Override
    public ResponseEntity<?> hashCustomMD5(HashInputTemplate hashInputTemplate) {
        String message = hashInputTemplate.getMessage();
        if(message == null){
            return new ResponseEntity<>("Message cannot be null", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                customMD5Hash.toHexString(customMD5Hash.computeMD5(message.getBytes())),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<?> hashCustomMD5File(MultipartFile file) {
        if(file == null || file.isEmpty()){
            return new ResponseEntity<>("File cannot be empty", HttpStatus.BAD_REQUEST);
        }

        byte[] bytes;
        try{
            bytes = file.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>("File cannot be read", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                customMD5Hash.toHexString(customMD5Hash.computeMD5(bytes)),
                HttpStatus.OK
        );
    }
}

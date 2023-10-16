package com.tzi.lab.services.hash;

import com.tzi.lab.entities.HashInputTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface HashService {
    ResponseEntity<?> hashCustomMD5(HashInputTemplate hashInputTemplate);
    ResponseEntity<?> hashCustomMD5File(MultipartFile file);

}

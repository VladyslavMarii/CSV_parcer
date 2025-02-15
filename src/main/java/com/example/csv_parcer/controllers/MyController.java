package com.example.csv_parcer.controllers;

import com.example.csv_parcer.Entities.CsvEntity;
import com.example.csv_parcer.exceptions.FailToUploadException;
import com.example.csv_parcer.services.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author $ {Vladyslav Marii}
 **/
@Controller
@RequestMapping("/")
public class MyController {
    @Autowired
    private CsvService csvService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }
        try {
            csvService.save(file);
        } catch (FailToUploadException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("File uploaded and data saved");
    }

    @GetMapping("/get")
    public ResponseEntity<List<CsvEntity>> search(@RequestParam("keyword") String keyword) {
        List<CsvEntity> results = csvService.search(keyword);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Application is running", HttpStatus.OK);
    }

}

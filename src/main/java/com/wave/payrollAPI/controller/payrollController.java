package com.wave.payrollAPI.controller;

import com.wave.payrollAPI.services.ReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1")
public class payrollController {


  @Autowired
  private ReportService reportService;


  @PostMapping(value = "/payroll_upload", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> handleFileUpload(@RequestParam("file") MultipartFile file)
            throws IOException {
         if(!reportService.fileValidate(file)){
            return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body("Uploading the same file ID is not allowed");
         }else{
             reportService.storeReport(file);
         }
        return reportService.generateReport(file);
    }



}

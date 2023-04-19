package com.bitGpt.bitGpt.backtest.controller;

import com.bitGpt.bitGpt.backtest.service.BackTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/upbit/test")
@CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"}, allowedHeaders = "*")
public class BackTestController {
    @Autowired
    private BackTestService backTestService;


    @GetMapping
    public Object getBackTestResult() {
        return backTestService.getBackTestResult();
    }
}

package com.bitGpt.bitGpt.backtest.controller;

import com.bitGpt.bitGpt.backtest.dto.BackTestResultResponseDto;
import com.bitGpt.bitGpt.backtest.dto.TestRequestDto;
import com.bitGpt.bitGpt.backtest.service.BackTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/upbit/test")
@CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"}, allowedHeaders = "*")
public class BackTestController {
    @Autowired
    private BackTestService backTestService;


    @PostMapping
    public List<BackTestResultResponseDto> getBackTestResult(@RequestBody TestRequestDto requestDto) {
        return backTestService.getBackTestResult(requestDto);
    }
}

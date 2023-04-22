package com.bitGpt.bitGpt.backtest.service;

import com.bitGpt.bitGpt.backtest.dto.BackTestResultResponseDto;
import com.bitGpt.bitGpt.backtest.dto.TestRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BackTestService {

    List<BackTestResultResponseDto> getBackTestResult(TestRequestDto request);
}

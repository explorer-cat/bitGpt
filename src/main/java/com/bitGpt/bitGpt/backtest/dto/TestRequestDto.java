package com.bitGpt.bitGpt.backtest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@ToString
@AllArgsConstructor
@Data
public class TestRequestDto {
    private LocalDateTime startDate; //총 테스트 기간
    private LocalDateTime endDate; //총 테스트 기간
    private List<Map<String,Object>> testCoin = new ArrayList<>(); //테스트 할 코인 {ticker : "KRW-BTC", rate : 20}
}

package com.bitGpt.bitGpt.backtest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
@AllArgsConstructor
public class BackTestResultResponseDto {
    private String test_start_date; //총 테스트 기간
    private String test_end_date; //총 테스트 기간
    private String ticker; //체결 종목
    private double start_balance; //테스트 시작가
    private int enter_count; //매매 횟수
    private int profit_count; //익절 횟수
    private int loss_count; //손절 횟수
    private double max_profit_percent; //테스트 기간 중 최대 수익
    private double max_loss_percent; //테스트 기간 중 원금 대비 최대 손실
    private double total_result_percent;//최종 수익
    private double simple_holding_percent;//단순 보유 수익
    List<ConclusionTestResponseDto> conclusionTestResponseDto = new ArrayList<>(); //세부 체결 내역

    public String getTestStartDate() {
        return test_start_date;
    }

    public void setTestStartDate(String testStartDate) {
        this.test_start_date = testStartDate;
    }

    public String getTestEndDate() {
        return test_end_date;
    }

    public void setTestEndDate(String testEndDate) {
        this.test_end_date = testEndDate;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getStartBalance() {
        return start_balance;
    }

    public void setStartBalance(double start_balance) {
        this.start_balance = start_balance;
    }

    public int getEnterCount() {
        return enter_count;
    }

    public void setEnterCount(int enter_count) {
        this.enter_count = enter_count;
    }

    public int getProfitCount() {
        return profit_count;
    }

    public void setProfitCount(int profit_count) {
        this.profit_count = profit_count;
    }

    public int getLossCount() {
        return loss_count;
    }

    public void setLossCount(int loss_count) {
        this.loss_count = loss_count;
    }

    public double getMaxProfitPercent() {
        return max_profit_percent;
    }

    public void setMaxProfitPercent(double max_profit_percent) {
        this.max_profit_percent = max_profit_percent;
    }

    public double getMaxLossPercent() {
        return max_loss_percent;
    }

    public void setMaxLossPercent(double max_loss_percent) {
        this.max_loss_percent = max_loss_percent;
    }

    public double getTotalResultPercent() {
        return total_result_percent;
    }

    public void setTotalResultPercent(double total_result_percent) {
        this.total_result_percent = total_result_percent;
    }

    public double getSimpleHoldingPercent() {
        return simple_holding_percent;
    }

    public void setSimpleHoldingPercent(double simple_holding_percent) {
        this.simple_holding_percent = simple_holding_percent;
    }

    public List<ConclusionTestResponseDto> getConclusionTestResponseDto() {
        return conclusionTestResponseDto;
    }

    public void setConclusionTestResponseDto(ConclusionTestResponseDto conclusionTestResponseDto) {
        this.conclusionTestResponseDto.add(conclusionTestResponseDto);
    }
}

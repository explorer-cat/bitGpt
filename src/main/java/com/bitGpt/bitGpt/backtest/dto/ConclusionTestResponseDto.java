package com.bitGpt.bitGpt.backtest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ConclusionTestResponseDto {
    private String enter_date; //체결 시간
    private String enter_type;//체결 타입 -> 매수 or 매도 bid or ask
    private double enter_price;//체결 가격 매수가 or 매도가
    private double single_rate;//해당 체결의 대한 수익률
    private double total_rate; //현재 원금대비 총 수익률
    private double current_balance_krw; //현재 원화 금액

    public String getEnterDate() {
        return enter_date;
    }

    public void setEnterDate(String enter_date) {
        this.enter_date = enter_date;
    }

    public String getEnterType() {
        return enter_type;
    }

    public void setEnterType(String enter_type) {
        this.enter_type = enter_type;
    }

    public double getEnterPrice() {
        return enter_price;
    }

    public void setEnterPrice(double enter_price) {
        this.enter_price = enter_price;
    }

    public double getSingleRate() {
        return single_rate;
    }

    public void setSingleRate(double single_rate) {
        this.single_rate = single_rate;
    }

    public double getTotalRate() {
        return total_rate;
    }

    public void setTotalRate(double total_rate) {
        this.total_rate = total_rate;
    }

    public double getCurrentBalanceKrw() {
        return current_balance_krw;
    }

    public void setCurrentBalanceKrw(double current_balance_krw) {
        this.current_balance_krw = current_balance_krw;
    }
}

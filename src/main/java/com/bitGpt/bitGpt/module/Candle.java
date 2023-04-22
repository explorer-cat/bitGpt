package com.bitGpt.bitGpt.module;


import lombok.Data;

@Data
public class Candle {
    private String dateTime;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private double prev_close;

    public Candle(String dateTime, double open, double high, double low, double close, double volume,double prev_close) {
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.prev_close = prev_close;
    }

}

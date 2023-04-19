package com.bitGpt.bitGpt.backtest.service;


import com.bitGpt.bitGpt.module.Upbit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class BackTestServiceImpl implements BackTestService {
    @Override
    public Object getBackTestResult() {
        Upbit upbit = new Upbit();
        upbit.getBalance();
        List ohlcv = upbit.getOhlcv("KRW-XRP", "2000", 2000, 0.3);

        for(int i = 1; i < ohlcv.size(); i++) {
            Map<String,Object> prev_value = (Map<String, Object>) ohlcv.get(i-1);
            Map<String,Object> current_value = (Map<String, Object>) ohlcv.get(i);


            System.out.println("current_value" + current_value);
            double prev_open_price = (double) prev_value.get("opening_price");
            double prev_close_price = (double) prev_value.get("prev_closing_price");
            double current_open_price = (double) current_value.get("opening_price");
            double current_trade_price = (double) current_value.get("trade_price");
            double current_change_rate = ((prev_close_price - prev_open_price) / prev_close_price) * 100;

            System.out.println("current_change_rate : " + current_change_rate + "%");
            System.out.println("current_close_price : " + current_open_price);
            System.out.println("current_trade_price : " + current_trade_price);
            System.out.println("prev_open_price : " + prev_open_price);
            System.out.println("prev_close_price : " + prev_close_price);
        }


        return upbit.getOhlcv("KRW-BTC","2000",2000,0.3);
    }

}

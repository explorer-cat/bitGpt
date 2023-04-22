package com.bitGpt.bitGpt.module;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class UpbitBackTest {
    public double[] getTestMA(List<Candle> ohlcv, int ma) {
        double[] ma_price = new double[ohlcv.size()];
        for (int i = 0; i < ohlcv.size(); i++) {
            double avg = 0.0;

            if (i >= (ma - 1)) {
                for (int j = i; j > i - ma; j--) {
                    Candle value = ohlcv.get(j);
                    double close_price = value.getClose();
                    avg += close_price;
                }
                ma_price[i] = avg / ma;
            }
        }
        return ma_price;
    }

}


package com.bitGpt.bitGpt.module;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Value;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import org.json.simple.JSONObject;

@Component
public class Upbit {

    //사용자 upbit 객체를 생성합니다.
    public void initMyUpbit() {
        String accessKey = "gUdxpoV4JieeqNyNnWJvc6gKK4xjzigd8ScCP5x1";
        String secretKey = "xGxjqCGMTtP2NqnxOvJIwJUMrQz5yIpBkOTLcFrX";
        String serverUrl = "https://api.upbit.com";

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getBalance() {
        System.out.println("upbit = balance");
    }


    public List getOhlcv(String ticker, String interval, int count, double period) {
        int MAX_CALL_COUNT = 200;
        try {
            ArrayList ohlcvList = new ArrayList<>();

            String url = getUrlOhlcv(interval);
            LocalDateTime to = LocalDateTime.now();

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url + "?market=" + ticker + "&count=" + count);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(EntityUtils.toString(entity, "UTF-8"));

            return (ArrayList) obj;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }




    /**
     * @title getOhlcv 요청을 위한 URL를 리턴하는 함수.
     * @param interval
     * @return
     */
    public  String getUrlOhlcv(String interval) {
        String url;
        if(interval.equals("day") || interval.equals("days")) {
            url = "https://api.upbit.com/v1/candles/days";
        } else if(interval.equals("minute1") || interval.equals("minutes1")) {
            url = "https://api.upbit.com/v1/candles/minutes/1";
        } else if(interval.equals("minute3") || interval.equals("minutes3")) {
            url = "https://api.upbit.com/v1/candles/minutes/3";
        } else if(interval.equals("minute5") || interval.equals("minutes5")) {
            url = "https://api.upbit.com/v1/candles/minutes/5";
        } else if(interval.equals("minute10") || interval.equals("minutes10")) {
            url = "https://api.upbit.com/v1/candles/minutes/10";
        } else if(interval.equals("minute15") || interval.equals("minutes15")) {
            url = "https://api.upbit.com/v1/candles/minutes/15";
        } else if(interval.equals("minute30") || interval.equals("minutes30")) {
            url = "https://api.upbit.com/v1/candles/minutes/30";
        } else if(interval.equals("minute60") || interval.equals("minutes60")) {
            url = "https://api.upbit.com/v1/candles/minutes/60";
        } else if(interval.equals("minute240") || interval.equals("minutes240")) {
            url = "https://api.upbit.com/v1/candles/minutes/240";
        } else if(interval.equals("week") || interval.equals("weeks")) {
            url = "https://api.upbit.com/v1/candles/weeks";
        } else if(interval.equals("month") || interval.equals("months")) {
            url = "https://api.upbit.com/v1/candles/months";
        } else {
            url = "https://api.upbit.com/v1/candles/days";
        }
        return url;
    }

}


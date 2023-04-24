package com.bitGpt.bitGpt.module;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.simple.JSONObject;

@Component
public class Upbit {
    private static final int MAX_COUNT = 200;
    //사용자 upbit 객체를 생성합니다.
    public void initMyUpbit() {
        String accessKey = "";
        String secretKey = "";
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


//    public List getOhlcv(String ticker, String interval, int count, double period) {
//        try {
//            ArrayList ohlcvList = new ArrayList<>();
//
//            String url = getUrlOhlcv(interval);
//            LocalDateTime to = LocalDateTime.now();
//
//            HttpClient client = HttpClientBuilder.create().build();
//            HttpGet request = new HttpGet(url + "?market=" + ticker + "&count=" + count);
//
//            HttpResponse response = client.execute(request);
//            HttpEntity entity = response.getEntity();
//            JSONParser parser = new JSONParser();
//            Object obj = parser.parse(EntityUtils.toString(entity, "UTF-8"));
//
//            return (ArrayList) obj;
//        } catch (Exception e) {
//            System.out.println(e);
//            return null;
//        }
//    }

    public List getOhlcv(String ticker, String interval, int count, double period) {
        try {
            ArrayList ohlcvList = new ArrayList<>();
            String url = getUrlOhlcv(interval);
            LocalDateTime to = LocalDateTime.now();
            HttpClient client = HttpClientBuilder.create().build();
            int offset = 0;

            while (offset < count) {
                int loopCount = Math.min(MAX_COUNT, count - offset);
                HttpGet request = new HttpGet(url + "?market=" + ticker + "&count=" + loopCount + "&to=" + to + ".000Z");
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(EntityUtils.toString(entity, "UTF-8"));
                ohlcvList.addAll((ArrayList) obj);
                offset += MAX_COUNT;
                to = LocalDateTime.parse(((ArrayList) obj).get(0).toString().split(" ")[0] + "T00:00:00");
            }
            System.out.println("ohlcvList" + ohlcvList);
            return ohlcvList;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    private static final String API_URL = "https://api.upbit.com/v1/candles/days";

    public List<Candle> getDailyCandles(String market, long count) throws Exception {
        LocalDateTime to = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        List<Candle> candles = new ArrayList<>();

        while (candles.size() < count) {
            long remaining = count - candles.size();
            String url = String.format("%s?market=%s&count=%d&to=%s", API_URL, market, remaining, to.format(formatter));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readValue(new URL(url), JsonNode.class);

            for (JsonNode node : rootNode) {
                Candle candle = new Candle(
                        node.get("candle_date_time_utc").asText(),
                        node.get("opening_price").asDouble(),
                        node.get("high_price").asDouble(),
                        node.get("low_price").asDouble(),
                        node.get("trade_price").asDouble(),
                        node.get("candle_acc_trade_volume").asDouble(),
                        node.get("prev_closing_price").asDouble()
                );
                candles.add(candle);
            }

            to = LocalDateTime.parse(rootNode.get(rootNode.size() - 1).get("candle_date_time_utc").asText(), formatter);
        }

        return candles.subList(0, (int) count);
    }


    public double getMA() {
        return 0;
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


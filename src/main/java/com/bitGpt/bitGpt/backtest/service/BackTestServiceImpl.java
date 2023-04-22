package com.bitGpt.bitGpt.backtest.service;


import com.bitGpt.bitGpt.backtest.dto.BackTestResultResponseDto;
import com.bitGpt.bitGpt.backtest.dto.ConclusionTestResponseDto;
import com.bitGpt.bitGpt.backtest.dto.TestRequestDto;
import com.bitGpt.bitGpt.module.Candle;
import com.bitGpt.bitGpt.module.Upbit;
import com.bitGpt.bitGpt.module.UpbitBackTest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BackTestServiceImpl implements BackTestService {
    private static String[] ticker_list = new String[]{"KRW-BTC"};
    private static int[] buy_MA_list = new int[]{4, 7, 10};
    private static int[] sell_MA_list = new int[]{4, 8};
    int investTotalMoney = 1000000; //테스트 금액
    private static int prevDay_limit_buy_percent = 20;//전일 캔들의 퍼센테이지에 따라 살껀지 안살껀지
    private double fee = 0.0005; //수수료
    private int try_count = 0; //매매 횟수
    private int loss_count = 0; //손절 횟수
    private int stock_count = 0; //익절
    DecimalFormat df = new DecimalFormat("0.00");


    @Override
    public List<BackTestResultResponseDto> getBackTestResult(TestRequestDto request) {
        int candleCount = 400;
        int endCount = 400; // 0~endCount까지
        List<BackTestResultResponseDto> result = new ArrayList<>();
        List<Map<String, Object>> coin_list = new ArrayList<>();

        for(int i = 0; i < request.getTestCoin().size(); i++) {
            Map<String, Object> coin_test_info = new HashMap<>();
            coin_test_info.put("ticker", request.getTestCoin().get(i).get("ticker"));
            coin_test_info.put("rate",  request.getTestCoin().get(i).get("rate"));
            coin_list.add(coin_test_info);
        }

        Upbit upbit = new Upbit();
        UpbitBackTest upbitBackTest = new UpbitBackTest();
        BackTestResultResponseDto backTestResultResponseDto = null;
        for (Map<String, Object> coin : coin_list) {
            backTestResultResponseDto = new BackTestResultResponseDto();
            boolean isBuy = false;
            double buy_price = 0;
            double invenstMoney; // 총 보유금액
            List total_money = new ArrayList();

            String ticker = (String) coin.get("ticker");
            double invest_rate = (Double) coin.get("rate");

            invenstMoney = investTotalMoney * invest_rate;

            //최초 시작 원금을 변수에 저장해둔다.
            double origin_invenstMoney = invenstMoney;
            try {

//              List ohlcv = upbit.getOhlcv(ticker, "day", candleCount, 0.3);
                List<Candle> ohlcv = upbit.getDailyCandles(ticker, candleCount);

                //과거부터 계산해야함으로 list를 한번 뒤집어준다.
                Collections.reverse(ohlcv);

                ohlcv = ohlcv.subList(0, endCount);


                double[] buy_ma1 = upbitBackTest.getTestMA(ohlcv, buy_MA_list[0]);
                double[] buy_ma2 = upbitBackTest.getTestMA(ohlcv, buy_MA_list[1]);
                double[] buy_ma3 = upbitBackTest.getTestMA(ohlcv, buy_MA_list[2]);


                double[] sell_ma1 = upbitBackTest.getTestMA(ohlcv, sell_MA_list[0]);
                double[] sell_ma2 = upbitBackTest.getTestMA(ohlcv, sell_MA_list[1]);

                //이평선 기준이되는 제일 마지막 캔들 일수 만큼은 제외하고 계산해야함으로 i를 제일 마지막 이평선을 기준으로 해준다.

                for (int i = buy_MA_list[buy_MA_list.length - 1]; i < ohlcv.size(); i++) {
                    ConclusionTestResponseDto conclusionRes = new ConclusionTestResponseDto();
                    double current_buy_ma1 = buy_ma1[i - 1];
                    double current_buy_ma2 = buy_ma2[i - 1];
                    double current_buy_ma3 = buy_ma3[i - 1];

                    double current_sell_ma1 = sell_ma1[i - 1];
                    double current_sell_ma2 = sell_ma2[i - 1];

                    Candle prev_value = ohlcv.get(i - 1);
                    Candle current_value = ohlcv.get(i);

                    double prev_open_price = prev_value.getOpen();
                    double prev_close_price = current_value.getPrev_close();
                    double current_open_price = current_value.getOpen();
                    double current_trade_price = current_value.getClose();
                    double current_change_rate = ((prev_close_price - prev_open_price) / prev_close_price) * 100;
                    String current_date = current_value.getDateTime();
                    boolean isJungBeyeul = (current_buy_ma1 > current_buy_ma2 && current_buy_ma2 > current_buy_ma3) && (current_buy_ma1 > current_buy_ma3);


//
//                    System.out.println("--------" + current_date + "-----------");
//                System.out.println("정배열 여부 " + isJungBeyeul);
//                System.out.println("current_ma5 : " + current_ma1);
//                System.out.println("current_ma10 : " + current_ma2);
//                System.out.println("current_ma20 : " + current_ma3);
//                System.out.println("current_change_rate : " + current_change_rate + "%");
//                System.out.println("current_close_price : " + current_open_price);
//                System.out.println("current_trade_price : " + current_trade_price);
//                System.out.println("prev_open_price : " + prev_open_price);
//                System.out.println("prev_close_price : " + prev_close_price);

                    conclusionRes.setEnterDate(current_date);
                    //해당 코인을 보유하고있을 경우 매도할지 확인함.
                    if (isBuy) {
                        //현재 수익률 계속 계산하기
                        invenstMoney = Math.floor(invenstMoney * (1.0 + ((current_open_price - prev_open_price) / prev_open_price)));
                        conclusionRes.setCurrentBalanceKrw(invest_rate);
                        //매도 이평선 조건에 맞는지 확인
                        if (prev_close_price < current_sell_ma1 && prev_close_price < current_sell_ma2) {
                            double rate = (current_open_price - buy_price) / buy_price;
                            double revenue_rate = Math.floor((rate - fee) * 100.0);
                            //수수료 반영
                            invenstMoney = Math.floor(invenstMoney * (1.0 - fee));
                            //매매 횟수 증가
                            try_count += 1;

                            //수익률이 0보다 크다면 익절 숫자 증가
                            if (revenue_rate > 0) {
                                stock_count += 1;
                            } else {
                                loss_count += 1;
                            }

                            conclusionRes.setEnterType("sell");
                            conclusionRes.setEnterPrice(current_open_price);
                            conclusionRes.setCurrentBalanceKrw(invenstMoney);
                            conclusionRes.setSingleRate(Math.floor(revenue_rate));
                            conclusionRes.setTotalRate(((invenstMoney - origin_invenstMoney) / origin_invenstMoney) * 100.0);

                            System.out.println("--------" + current_date + "-----------");
                            System.out.println("[매도] " + ticker + " 종목 잔고 : " + invenstMoney + " 매도가 : " + current_open_price + " 수익률 : " + Math.floor(revenue_rate) + "%");
                            isBuy = false;
                        } else {
//                        System.out.println("매도 이평선 조건이 아닙니다.");
                        }
                    }

                    if (!isBuy) {
                        if (prev_close_price > current_buy_ma1 && prev_close_price > current_buy_ma2 && prev_close_price > current_buy_ma3) {
                            if (current_change_rate < prevDay_limit_buy_percent) {

                                System.out.println("--------" + current_date + "-----------");
                                buy_price = current_open_price;
                                //수수료 반영
                                invenstMoney = invenstMoney * (1.0 - fee);

                                //매매 횟수 증가
                                try_count += 1;

                                conclusionRes.setEnterType("buy");
                                conclusionRes.setEnterPrice(current_open_price);
                                conclusionRes.setCurrentBalanceKrw(invenstMoney);
                                conclusionRes.setSingleRate(-9999);
                                conclusionRes.setTotalRate(((invenstMoney - origin_invenstMoney) / origin_invenstMoney) * 100.0);

                                System.out.println("[매수]" + ticker + " 종목 잔고 : " + invenstMoney + " 매수가 : " + current_open_price);
                                isBuy = true;
                            } else {
                                System.out.println("전일 20% 이상 급등으로 매수하지 않았습니다.");
                            }
                        } else {
                            System.out.println("매수 이평선 조건이 아닙니다.");
                        }
                    }

                    //final result를 만들기 위해 매일매일 ticker의 자산액을 total_money에 저장한다.
                    total_money.add(invenstMoney);

                    if(conclusionRes.getEnterType() != null) {
                        backTestResultResponseDto.getConclusionTestResponseDto().add(conclusionRes);
                    }
                }
                System.out.println("total_money count " + total_money.size());

                double mdd = ((double) Collections.min(total_money) - origin_invenstMoney) / origin_invenstMoney;
                double mr = ((double) Collections.max(total_money) - origin_invenstMoney) / origin_invenstMoney;
                double result_percent = ((double) total_money.get(total_money.size() - 1) - origin_invenstMoney) / origin_invenstMoney;

                backTestResultResponseDto.setTestStartDate(ohlcv.get(0).getDateTime());
                backTestResultResponseDto.setTestEndDate(ohlcv.get(ohlcv.size() - 1).getDateTime());
                backTestResultResponseDto.setTicker(ticker);
                backTestResultResponseDto.setStartBalance(origin_invenstMoney);
                backTestResultResponseDto.setEnterCount(try_count);
                backTestResultResponseDto.setLossCount(loss_count);
                backTestResultResponseDto.setProfitCount(stock_count);
                backTestResultResponseDto.setMaxProfitPercent(mr * 100.0);
                backTestResultResponseDto.setMaxLossPercent(mdd * 100.0);
                backTestResultResponseDto.setTotalResultPercent(result_percent * 100.0);

                System.out.println("=======" + ticker + " 결과========");
                System.out.println("total_money" + total_money.size());
                System.out.println("(double) Collections.min(total_money)" + (double) Collections.min(total_money));
                System.out.println("매매 횟수 : " + try_count);
                System.out.println("익절 횟수 : " + stock_count);
                System.out.println("손절 횟수 : " + loss_count);
                System.out.println("원금대비 기간내 최대 손실 : " + df.format(mdd * 100.0) + "%");
                System.out.println("원금대비 기간내 최대 수익 : " + df.format(mr * 100.0) + "%");
                System.out.println("최종 수익 : " + df.format(result_percent * 100.0) + "%");

                result.add(backTestResultResponseDto);
            } catch (Exception e) {
                System.err.println(e);
            }
        }


        System.out.println("=======최종 총 결과========");


        return result;
    }
}

package com.bitGpt.bitGpt.backtest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class OhlcvDto {
    private String title;
    private String content;
    private Long subcategory_id;
}

package com.progressSoft.fx_deals.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DealResponseDto {
    private String id;
    private String fromCurrency;
    private String toCurrency;
    private LocalDateTime dealTimestamp;
    private Double dealAmount;

}

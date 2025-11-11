package com.progressSoft.fx_deals.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DealRequestDto {

    @NotBlank(message = "Deal ID is required")
    private String id;

    @NotBlank(message = "From currency is required")
    @Size(min = 3, max = 3, message = "From currency must be a 3-letter ISO code")
    private String fromCurrency;

    @NotBlank(message = "To currency is required")
    @Size(min = 3, max = 3, message = "To currency must be a 3-letter ISO code")
    private String toCurrency;

    @NotNull(message = "Deal timestamp is required")
    private LocalDateTime dealTimestamp;

    @NotNull(message = "Deal amount is required")
    @Positive(message = "Deal amount must be positive")
    private Double dealAmount;
}

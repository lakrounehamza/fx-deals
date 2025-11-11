package com.progressSoft.fx_deals.service;

import com.progressSoft.fx_deals.dto.DealRequestDto;
import com.progressSoft.fx_deals.dto.DealResponseDto;

public interface IDealService {
    DealResponseDto save(DealRequestDto dealRequestDto);
    DealResponseDto getById(String id);
}

package com.progressSoft.fx_deals.service;

import com.progressSoft.fx_deals.dto.DealRequestDto;
import com.progressSoft.fx_deals.dto.DealResponseDto;

import java.util.List;

public interface IDealService {
    DealResponseDto save(DealRequestDto dealRequestDto);
    DealResponseDto getById(String id);
    void saveDeals(List<DealRequestDto> dealRequestDtoList);
}

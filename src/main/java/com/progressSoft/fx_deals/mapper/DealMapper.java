package com.progressSoft.fx_deals.mapper;

import com.progressSoft.fx_deals.dto.DealRequestDto;
import com.progressSoft.fx_deals.dto.DealResponseDto;
import com.progressSoft.fx_deals.entity.Deal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealMapper {
    DealResponseDto toDto(Deal deal);
    Deal toEntity(DealRequestDto dealRequestDto);
}

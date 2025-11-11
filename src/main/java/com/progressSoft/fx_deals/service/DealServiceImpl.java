package com.progressSoft.fx_deals.service;

import com.progressSoft.fx_deals.dto.DealRequestDto;
import com.progressSoft.fx_deals.dto.DealResponseDto;
import com.progressSoft.fx_deals.entity.Deal;
import com.progressSoft.fx_deals.exception.DuplicateDealException;
import com.progressSoft.fx_deals.exception.InvalidDealDataException;
import com.progressSoft.fx_deals.mapper.DealMapper;
import com.progressSoft.fx_deals.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements IDealService {

    private final DealRepository dealRepository;
    private final DealMapper dealMapper;

    @Override
    public DealResponseDto save(DealRequestDto dealRequestDto) {
        if (dealRepository.existsById(dealRequestDto.getId())) {
            throw new DuplicateDealException(dealRequestDto.getId());
        }
        if(dealRequestDto.getToCurrency().equals(dealRequestDto.getFromCurrency())) {
            throw new InvalidDealDataException("FromCurrency and ToCurrency must be different");
        }
        Deal deal = dealMapper.toEntity(dealRequestDto);
        Deal savedDeal = dealRepository.save(deal);
        return dealMapper.toDto(savedDeal);
    }

    @Override
    public DealResponseDto getById(String id) {
        return null;
    }
}

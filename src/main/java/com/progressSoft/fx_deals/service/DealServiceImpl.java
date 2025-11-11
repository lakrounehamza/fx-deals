package com.progressSoft.fx_deals.service;

import com.progressSoft.fx_deals.dto.DealRequestDto;
import com.progressSoft.fx_deals.dto.DealResponseDto;
import com.progressSoft.fx_deals.entity.Deal;
import com.progressSoft.fx_deals.exception.DealNotFoundException;
import com.progressSoft.fx_deals.exception.DuplicateDealException;
import com.progressSoft.fx_deals.exception.InvalidDealDataException;
import com.progressSoft.fx_deals.mapper.DealMapper;
import com.progressSoft.fx_deals.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Deal> deal = dealRepository.findById(id);
        if(deal.isPresent()) {
            return dealMapper.toDto(deal.get());
        }
        else
            throw new DealNotFoundException( "Deal not found with ID:"+id);
    }

    @Override
    public void saveDeals(List<DealRequestDto> dealRequestDtoList) {
        for (DealRequestDto dto : dealRequestDtoList) {
            try {
                save(dto);
            } catch (Exception e) {
                System.err.println("Error while importing the deal " + dto.getId() + ": " + e.getMessage());
            }
        }
    }

}

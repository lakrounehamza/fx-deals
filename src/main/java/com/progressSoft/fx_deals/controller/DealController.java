package com.progressSoft.fx_deals.controller;

import com.progressSoft.fx_deals.dto.DealRequestDto;
import com.progressSoft.fx_deals.dto.DealResponseDto;
import com.progressSoft.fx_deals.service.IDealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/deals")
@RequiredArgsConstructor
public class DealController {
    private final IDealService dealService;

    @PostMapping
    public ResponseEntity<DealResponseDto> saveDeal(@RequestBody @Valid DealRequestDto dealRequestDto) {
        DealResponseDto savedDeal = dealService.save(dealRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDeal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealResponseDto> getDealById(@PathVariable String id) {
        DealResponseDto deal = dealService.getById(id);
        return ResponseEntity.ok(deal);
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> importDeals(@RequestBody @Valid List<DealRequestDto> dealRequestDtoList) {
        dealService.saveDeals(dealRequestDtoList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

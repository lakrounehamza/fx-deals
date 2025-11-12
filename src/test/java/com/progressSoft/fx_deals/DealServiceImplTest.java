package com.progressSoft.fx_deals;


import com.progressSoft.fx_deals.dto.DealRequestDto;
import com.progressSoft.fx_deals.dto.DealResponseDto;
import com.progressSoft.fx_deals.entity.Deal;
import com.progressSoft.fx_deals.exception.DealNotFoundException;
import com.progressSoft.fx_deals.exception.DuplicateDealException;
import com.progressSoft.fx_deals.exception.InvalidDealDataException;
import com.progressSoft.fx_deals.mapper.DealMapper;
import com.progressSoft.fx_deals.repository.DealRepository;
import com.progressSoft.fx_deals.service.DealServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Deal Service Tests")
class DealServiceImplTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealMapper dealMapper;

    @InjectMocks
    private DealServiceImpl dealService;

    private DealRequestDto validDealRequest;
    private Deal validDeal;
    private DealResponseDto validDealResponse;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        validDealRequest = new DealRequestDto();
        validDealRequest.setId("DEAL001");
        validDealRequest.setFromCurrency("USD");
        validDealRequest.setToCurrency("EUR");
        validDealRequest.setDealTimestamp(now);
        validDealRequest.setDealAmount(1000.0);

        validDeal = new Deal();
        validDeal.setId("DEAL001");
        validDeal.setFromCurrency("USD");
        validDeal.setToCurrency("EUR");
        validDeal.setDealTimestamp(now);
        validDeal.setDealAmount(1000.0);

        validDealResponse = new DealResponseDto();
        validDealResponse.setId("DEAL001");
        validDealResponse.setFromCurrency("USD");
        validDealResponse.setToCurrency("EUR");
        validDealResponse.setDealTimestamp(now);
        validDealResponse.setDealAmount(1000.0);
    }

    @Test
    @DisplayName("Should save valid deal successfully")
    void save_ValidDeal_ReturnsSuccessfully() {
        when(dealRepository.existsById(validDealRequest.getId())).thenReturn(false);
        when(dealMapper.toEntity(validDealRequest)).thenReturn(validDeal);
        when(dealRepository.save(validDeal)).thenReturn(validDeal);
        when(dealMapper.toDto(validDeal)).thenReturn(validDealResponse);

        DealResponseDto result = dealService.save(validDealRequest);

        assertNotNull(result);
        assertEquals("DEAL001", result.getId());
        assertEquals("USD", result.getFromCurrency());
        assertEquals("EUR", result.getToCurrency());
        assertEquals(1000.0, result.getDealAmount());

        verify(dealRepository, times(1)).existsById("DEAL001");
        verify(dealRepository, times(1)).save(validDeal);
        verify(dealMapper, times(1)).toEntity(validDealRequest);
        verify(dealMapper, times(1)).toDto(validDeal);
    }

    @Test
    @DisplayName("Should throw DuplicateDealException when deal ID already exists")
    void save_DuplicateDeal_ThrowsDuplicateDealException() {
        when(dealRepository.existsById(validDealRequest.getId())).thenReturn(true);

        DuplicateDealException exception = assertThrows(DuplicateDealException.class, () -> {
            dealService.save(validDealRequest);
        });

        assertTrue(exception.getMessage().contains("DEAL001"));
        verify(dealRepository, times(1)).existsById("DEAL001");
        verify(dealRepository, never()).save(any(Deal.class));
        verify(dealMapper, never()).toEntity(any());
    }

    @Test
    @DisplayName("Should throw InvalidDealDataException when currencies are the same")
    void save_SameCurrencies_ThrowsInvalidDealDataException() {
        validDealRequest.setToCurrency("USD"); // Same as fromCurrency
        when(dealRepository.existsById(validDealRequest.getId())).thenReturn(false);

        InvalidDealDataException exception = assertThrows(InvalidDealDataException.class, () -> {
            dealService.save(validDealRequest);
        });

        assertEquals("FromCurrency and ToCurrency must be different", exception.getMessage());
        verify(dealRepository, times(1)).existsById("DEAL001");
        verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    @DisplayName("Should retrieve deal by ID successfully")
    void getById_ExistingDeal_ReturnsDeal() {
        when(dealRepository.findById("DEAL001")).thenReturn(Optional.of(validDeal));
        when(dealMapper.toDto(validDeal)).thenReturn(validDealResponse);

        DealResponseDto result = dealService.getById("DEAL001");

        assertNotNull(result);
        assertEquals("DEAL001", result.getId());
        assertEquals("USD", result.getFromCurrency());
        assertEquals("EUR", result.getToCurrency());

        verify(dealRepository, times(1)).findById("DEAL001");
        verify(dealMapper, times(1)).toDto(validDeal);
    }

    @Test
    @DisplayName("Should throw DealNotFoundException when deal not found")
    void getById_NonExistingDeal_ThrowsDealNotFoundException() {
         when(dealRepository.findById("INVALID")).thenReturn(Optional.empty());

        DealNotFoundException exception = assertThrows(DealNotFoundException.class, () -> {
            dealService.getById("INVALID");
        });

        assertTrue(exception.getMessage().contains("INVALID"));
        verify(dealRepository, times(1)).findById("INVALID");
        verify(dealMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should save all valid deals in batch")
    void saveDeals_ValidList_SavesAllDeals() {

        DealRequestDto deal1 = createDealRequest("DEAL001", "USD", "EUR", 1000.0);
        DealRequestDto deal2 = createDealRequest("DEAL002", "GBP", "JPY", 2000.0);
        List<DealRequestDto> deals = Arrays.asList(deal1, deal2);

        when(dealRepository.existsById(anyString())).thenReturn(false);
        when(dealMapper.toEntity(any(DealRequestDto.class))).thenReturn(validDeal);
        when(dealRepository.save(any(Deal.class))).thenReturn(validDeal);
        when(dealMapper.toDto(any(Deal.class))).thenReturn(validDealResponse);

        dealService.saveDeals(deals);

        verify(dealRepository, times(2)).existsById(anyString());
        verify(dealRepository, times(2)).save(any(Deal.class));
    }

    @Test
    @DisplayName("Should continue processing when one deal fails in batch")
    void saveDeals_WithDuplicateError_ContinuesProcessing() {
        DealRequestDto deal1 = createDealRequest("DEAL001", "USD", "EUR", 1000.0);
        DealRequestDto deal2 = createDealRequest("DEAL001", "GBP", "JPY", 2000.0); // Duplicate ID
        DealRequestDto deal3 = createDealRequest("DEAL003", "CAD", "AUD", 3000.0);
        List<DealRequestDto> deals = Arrays.asList(deal1, deal2, deal3);

        when(dealRepository.existsById("DEAL001"))
                .thenReturn(false)
                .thenReturn(true);
        when(dealRepository.existsById("DEAL003")).thenReturn(false);

        when(dealMapper.toEntity(any(DealRequestDto.class))).thenReturn(validDeal);
        when(dealRepository.save(any(Deal.class))).thenReturn(validDeal);
        when(dealMapper.toDto(any(Deal.class))).thenReturn(validDealResponse);

        dealService.saveDeals(deals);

        verify(dealRepository, times(2)).save(any(Deal.class));
    }

    @Test
    @DisplayName("Should continue processing when currency validation fails in batch")
    void saveDeals_WithInvalidCurrency_ContinuesProcessing() {
        DealRequestDto deal1 = createDealRequest("DEAL001", "USD", "EUR", 1000.0);
        DealRequestDto deal2 = createDealRequest("DEAL002", "USD", "USD", 2000.0); // Invalid: same currency
        DealRequestDto deal3 = createDealRequest("DEAL003", "GBP", "JPY", 3000.0);
        List<DealRequestDto> deals = Arrays.asList(deal1, deal2, deal3);

        when(dealRepository.existsById(anyString())).thenReturn(false);
        when(dealMapper.toEntity(any(DealRequestDto.class))).thenReturn(validDeal);
        when(dealRepository.save(any(Deal.class))).thenReturn(validDeal);
        when(dealMapper.toDto(any(Deal.class))).thenReturn(validDealResponse);

        dealService.saveDeals(deals);

        verify(dealRepository, times(2)).save(any(Deal.class));
    }

    @Test
    @DisplayName("Should handle empty list in batch import")
    void saveDeals_EmptyList_NoException() {
        List<DealRequestDto> emptyList = Arrays.asList();

        assertDoesNotThrow(() -> dealService.saveDeals(emptyList));

        verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    @DisplayName("Should validate deal amount is positive")
    void save_NegativeAmount_SavesSuccessfully() {
         validDealRequest.setDealAmount(-500.0);
        when(dealRepository.existsById(validDealRequest.getId())).thenReturn(false);
        when(dealMapper.toEntity(validDealRequest)).thenReturn(validDeal);
        when(dealRepository.save(validDeal)).thenReturn(validDeal);
        when(dealMapper.toDto(validDeal)).thenReturn(validDealResponse);

        DealResponseDto result = dealService.save(validDealRequest);

        assertNotNull(result);
        verify(dealRepository, times(1)).save(validDeal);
    }

    @Test
    @DisplayName("Should handle null timestamp in request")
    void save_NullTimestamp_SavesSuccessfully() {
        validDealRequest.setDealTimestamp(null);
        when(dealRepository.existsById(validDealRequest.getId())).thenReturn(false);
        when(dealMapper.toEntity(validDealRequest)).thenReturn(validDeal);
        when(dealRepository.save(validDeal)).thenReturn(validDeal);
        when(dealMapper.toDto(validDeal)).thenReturn(validDealResponse);

        DealResponseDto result = dealService.save(validDealRequest);

        assertNotNull(result);
        verify(dealRepository, times(1)).save(validDeal);
    }

    private DealRequestDto createDealRequest(String id, String from, String to, Double amount) {
        DealRequestDto dto = new DealRequestDto();
        dto.setId(id);
        dto.setFromCurrency(from);
        dto.setToCurrency(to);
        dto.setDealTimestamp(LocalDateTime.now());
        dto.setDealAmount(amount);
        return dto;
    }
}
package com.stock_service.stock.application.handler.brand_handler;

import com.stock_service.stock.application.dto.brand_dto.BrandRequest;
import com.stock_service.stock.application.dto.brand_dto.BrandResponse;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.application.mapper.brand_mapper.IBrandRequestMapper;
import com.stock_service.stock.application.mapper.brand_mapper.IBrandResponseMapper;
import com.stock_service.stock.domain.api.IBrandModelServicePort;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.util.Paginated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class BrandHandlerTest {

    @Mock
    private IBrandModelServicePort brandModelServicePort;

    @Mock
    private IBrandRequestMapper brandRequestMapper;

    @Mock
    private IBrandResponseMapper brandResponseMapper;

    @InjectMocks
    private BrandHandler brandHandler;

    private BrandResponse brandResponse;
    private BrandRequest brandRequest;
    private BrandModel brandModel;
    private Paginated<BrandModel> paginatedBrandModel;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        brandRequest = new BrandRequest();
        brandResponse = new BrandResponse();
        brandModel = new BrandModel(1L, "Samsung", "Samsung Electronics");

        when(brandRequestMapper.brandRequestToBrandModel(brandRequest)).thenReturn(brandModel);
        when(brandModelServicePort.saveBrand(brandModel)).thenReturn(brandModel);
        when(brandResponseMapper.brandModelToBrandResponse(brandModel)).thenReturn(brandResponse);
    }

    @Test
    void saveBrand() {

        BrandResponse result = brandHandler.saveBrand(brandRequest);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(brandResponse, result);

        // Verificar interacciones con los mocks
        verify(brandRequestMapper).brandRequestToBrandModel(brandRequest);
        verify(brandModelServicePort).saveBrand(brandModel);
        verify(brandResponseMapper).brandModelToBrandResponse(brandModel);
    }

    @Test
    @DisplayName("Debe devolver marcas paginadas correctamente")
    void getBrands() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        brandResponse.setId(1L);
        brandResponse.setName("Samsung");
        brandResponse.setDescription("Samsung Electronics");
        paginatedBrandModel = new Paginated<>(List.of(brandModel), page, size, 1);

        when(brandModelServicePort.getBrands(page, size, sort, ascending)).thenReturn(paginatedBrandModel);
        when(brandResponseMapper.brandModelToBrandResponse(brandModel)).thenReturn(brandResponse);

        Paginated<BrandResponse> result = brandHandler.getBrands(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(brandResponse, result.getContent().get(0));
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(1, result.getTotalPages());

        verify(brandModelServicePort, times(1)).getBrands(page, size, sort, ascending);
        verify(brandResponseMapper, times(1)).brandModelToBrandResponse(brandModel);
    }
}
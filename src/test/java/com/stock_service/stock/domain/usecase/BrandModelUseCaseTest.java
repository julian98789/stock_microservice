package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BrandModelUseCaseTest {

    @Mock
    private IBrandModelPersistencePort brandModelPersistencePort;

    BrandModel brandModel;

    @InjectMocks
    private BrandModelUseCase brandModelUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        brandModel = new BrandModel(1L, "Samsung", "Samsung Electronics");
    }

    @Test
    @DisplayName("Should throw NameAlreadyExistsException when brand name already exists")
    void shouldThrowNameAlreadyExistsExceptionWhenBrandNameAlreadyExists() {
        String existingBrandName = "Samsung";

        when(brandModelPersistencePort.existsByName(existingBrandName)).thenReturn(true);

        NameAlreadyExistsException exception = assertThrows(
                NameAlreadyExistsException.class,
                () -> brandModelUseCase.saveBrand(brandModel)
        );

        assertEquals(Util.BRAND_NAME_ALREADY_EXISTS, exception.getMessage());
        verify(brandModelPersistencePort, never()).saveBrand(brandModel);
    }

    @Test
    @DisplayName("Should return paginated brands correctly")
    void shouldReturnPaginatedBrandsCorrectly() {
        int page = 1;
        int size = 5;
        String sort = "name";
        boolean ascending = true;

        BrandModel brand2 = new BrandModel(2L, "Brand B", "Brand B description");

        List<BrandModel> brandList = Arrays.asList(brandModel, brand2);
        Paginated<BrandModel> paginatedResponse = new Paginated<>(brandList, page, size, 10);

        when(brandModelPersistencePort.getBrandsPaginated(page, size, sort, ascending)).thenReturn(paginatedResponse);

        Paginated<BrandModel> result = brandModelUseCase.getBrandsPaginated(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getPageNumber());
        assertEquals(5, result.getPageSize());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        verify(brandModelPersistencePort, times(1)).getBrandsPaginated(page, size, sort, ascending);
    }
}
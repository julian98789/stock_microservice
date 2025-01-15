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
    @DisplayName("Debe lanzar NameAlreadyExistsException cuando el nombre de la marca ya existe")
    void saveBrand() {
        String existingCategoryName = "Samsung";

        when(brandModelPersistencePort.existsByName(existingCategoryName)).thenReturn(true);

        NameAlreadyExistsException exception = assertThrows(
                NameAlreadyExistsException.class,
                () -> brandModelUseCase.saveBrand(brandModel)
        );

        assertEquals(Util.BRAND_NAME_ALREADY_EXISTS, exception.getMessage());
        verify(brandModelPersistencePort, never()).saveBrand(brandModel);

    }


    @Test
    @DisplayName("Deberían devolverse las marcas paginadas correctamente")
    void getBrandsPaginated() {
        int page = 1;
        int size = 5;
        String sort = "name";
        boolean ascending = true;

        BrandModel brand1 = new BrandModel(1l, "Brand A", "Brand A description");

        BrandModel brand2 = new BrandModel(2l, "Brand B", "Brand B description");


        List<BrandModel> brandList = Arrays.asList(brand1, brand2);
        Paginated<BrandModel> paginatedResponse = new Paginated<>(brandList, page, size, 10);

        when(brandModelPersistencePort.getBrandsPaginated(page, size, sort, ascending)).thenReturn(paginatedResponse);

        // Act
        Paginated<BrandModel> result = brandModelUseCase.getBrandsPaginated(page, size, sort, ascending);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getPageNumber());
        assertEquals(5, result.getPageSize());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        verify(brandModelPersistencePort, times(1)).getBrandsPaginated(page, size, sort, ascending);
    }
}
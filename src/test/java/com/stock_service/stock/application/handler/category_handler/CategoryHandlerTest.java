package com.stock_service.stock.application.handler.category_handler;

import com.stock_service.stock.application.dto.category_dto.CategoryRequest;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.application.mapper.category_mapper.ICategoryRequestMapper;
import com.stock_service.stock.application.mapper.category_mapper.ICategoryResponseMapper;
import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.model.CategoryModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryHandlerTest {

    @Mock
    private  ICategoryModelServicePort categoryModelServicePort;

    @Mock
    private  ICategoryRequestMapper categoryRequestMapper;

    @Mock
    private  ICategoryResponseMapper categoryResponseMapper;

    private CategoryRequest categoryRequest;
    private CategoryModel categoryModel;
    private CategoryResponse categoryResponse;

    @InjectMocks
    CategoryHandler categoryHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializa los objetos necesarios
        categoryRequest = new CategoryRequest();
        categoryModel = new CategoryModel(1L, "Electronics", "Electronic devices");
        categoryResponse = new CategoryResponse();

        // Mock de las respuestas esperadas
        when(categoryRequestMapper.categoryrequestToCategoryModel(categoryRequest)).thenReturn(categoryModel);
        when(categoryModelServicePort.saveCategory(categoryModel)).thenReturn(categoryModel);
        when(categoryResponseMapper.categoryModelToCategoryResponse(categoryModel)).thenReturn(categoryResponse);
    }

    @Test
    void saveCategory() {

        CategoryResponse result = categoryHandler.saveCategory(categoryRequest);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(categoryResponse, result);

        // Verificar interacciones con los mocks
        verify(categoryRequestMapper).categoryrequestToCategoryModel(categoryRequest);
        verify(categoryModelServicePort).saveCategory(categoryModel);
        verify(categoryResponseMapper).categoryModelToCategoryResponse(categoryModel);
    }
}
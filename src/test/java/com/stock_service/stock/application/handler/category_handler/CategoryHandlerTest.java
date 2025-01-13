package com.stock_service.stock.application.handler.category_handler;

import com.stock_service.stock.application.dto.category_dto.CategoryRequest;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.application.mapper.category_mapper.ICategoryRequestMapper;
import com.stock_service.stock.application.mapper.category_mapper.ICategoryResponseMapper;
import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.model.CategoryModel;
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

class CategoryHandlerTest {

    @Mock
    private  ICategoryModelServicePort categoryModelServicePort;

    @Mock
    private  ICategoryRequestMapper categoryRequestMapper;

    @Mock
    private  ICategoryResponseMapper categoryResponseMapper;

    @InjectMocks
    CategoryHandler categoryHandler;

    private CategoryRequest categoryRequest;
    private CategoryModel categoryModel;
    private CategoryResponse categoryResponse;
    private Paginated<CategoryModel> paginatedCategoryModel;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializa los objetos necesarios
        categoryRequest = new CategoryRequest();
        categoryModel = new CategoryModel(1L, "Electronics", "Electronic devices");
        categoryResponse = new CategoryResponse();

        // Mock de las respuestas esperadas
        when(categoryRequestMapper.categoryRequestToCategoryModel(categoryRequest)).thenReturn(categoryModel);
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
        verify(categoryRequestMapper).categoryRequestToCategoryModel(categoryRequest);
        verify(categoryModelServicePort).saveCategory(categoryModel);
        verify(categoryResponseMapper).categoryModelToCategoryResponse(categoryModel);
    }

    @Test
    @DisplayName("Debe devolver categorías paginadas correctamente")
    void getCategories() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        categoryResponse.setId(1L);
        categoryResponse.setName("Books");
        categoryResponse.setDescription("Various books");
        paginatedCategoryModel = new Paginated<>(List.of(categoryModel), page, size, 1);

        when(categoryModelServicePort.getCategoriesPaginated(page, size, sort, ascending)).thenReturn(paginatedCategoryModel);
        when(categoryResponseMapper.categoryModelToCategoryResponse(categoryModel)).thenReturn(categoryResponse);

        Paginated<CategoryResponse> result = categoryHandler.getCategories(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(categoryResponse, result.getContent().get(0));
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(1, result.getTotalPages());

        verify(categoryModelServicePort, times(1)).getCategoriesPaginated(page, size, sort, ascending);
        verify(categoryResponseMapper, times(1)).categoryModelToCategoryResponse(categoryModel);
    }
}
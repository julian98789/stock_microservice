package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
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

class CategoryModelUseCaseTest {

    @Mock
    private ICategoryModelPersistencePort categoryModelPersistencePort;

    @InjectMocks
    private CategoryModelUseCase categoryModelUseCase;

    @Mock
    private CategoryModel categoryModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryModel = new CategoryModel(6L, "Electronics", "Electronic devices");
    }

    @Test
    @DisplayName("Debe lanzar NameAlreadyExistsException cuando el nombre de la categoría ya existe")
    void saveCategory() {
        String existingCategoryName = "Electronics";

        when(categoryModelPersistencePort.existByName(existingCategoryName)).thenReturn(true);

        // Act & Assert
        NameAlreadyExistsException exception = assertThrows(
                NameAlreadyExistsException.class,
                () -> categoryModelUseCase.saveCategory(categoryModel)
        );

        assertEquals(Util.CATEGORY_NAME_ALREADY_EXISTS, exception.getMessage());
        verify(categoryModelPersistencePort, never()).saveCategory(any(CategoryModel.class));
    }

    @Test
    @DisplayName("Debe devolver false cuando el nombre de la categoría no existe")
    void existByName() {
        String existingCategoryName = "Books";

        when(categoryModelPersistencePort.existByName(existingCategoryName)).thenReturn(false);

        categoryModelUseCase.existByName(existingCategoryName);

        verify(categoryModelPersistencePort, times(1)).existByName(existingCategoryName);
    }


    @Test
    @DisplayName("Deberían devolverse las categorías paginadas correctamente")
    void getCategories() {
        int page = 1;
        int size = 5;
        String sort = "name";
        boolean ascending = true;

        CategoryModel categoryModel1 = new CategoryModel(1L, "Electronics", "Electronic devices");
        CategoryModel categoryModel2 = new CategoryModel(2L, "Books", "Books and novels");

        List<CategoryModel> categoryList = Arrays.asList(categoryModel1, categoryModel2);
        Paginated<CategoryModel> paginatedResponse = new Paginated<>(categoryList, page, size, 10);

        when(categoryModelPersistencePort.getCategories(page, size, sort, ascending)).thenReturn(paginatedResponse);

        Paginated<CategoryModel>  result = categoryModelUseCase.getCategories(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getPageNumber());
        assertEquals(5, result.getPageSize());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());

        verify(categoryModelPersistencePort, times(1)).getCategories(page, size, sort, ascending);

    }
}
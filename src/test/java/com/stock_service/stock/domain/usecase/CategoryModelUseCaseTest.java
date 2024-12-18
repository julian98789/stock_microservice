package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.util.UtilMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

        assertEquals(UtilMessage.CATEGORY_NAME_ALREADY_EXISTS, exception.getMessage());
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
    @DisplayName("Debe llamar a getCategories con los parámetros correctos")
    void getCategories() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        categoryModelUseCase.getCategories(page, size, sort, ascending);

        verify(categoryModelPersistencePort, times(1)).getCategories(page, size, sort, ascending);
    }
}
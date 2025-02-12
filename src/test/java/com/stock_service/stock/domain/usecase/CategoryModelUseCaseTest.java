package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.IArticleModelServicePort;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.ArticleModel;
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

    @Mock
    private IArticleModelServicePort articleModelServicePort;

    @Mock
    private ArticleModel articleModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryModel = new CategoryModel(6L, "Electronics", "Electronic devices");
         articleModel = new ArticleModel();
    }
    @Test
    @DisplayName("Should throw NameAlreadyExistsException when category name already exists")
    void shouldThrowNameAlreadyExistsExceptionWhenCategoryNameAlreadyExists() {
        String existingCategoryName = "Electronics";

        when(categoryModelPersistencePort.existByName(existingCategoryName)).thenReturn(true);

        NameAlreadyExistsException exception = assertThrows(
                NameAlreadyExistsException.class,
                () -> categoryModelUseCase.saveCategory(categoryModel)
        );

        assertEquals(Util.CATEGORY_NAME_ALREADY_EXISTS, exception.getMessage());
        verify(categoryModelPersistencePort, never()).saveCategory(any(CategoryModel.class));
    }

    @Test
    @DisplayName("Should return paginated categories correctly")
    void shouldReturnPaginatedCategoriesCorrectly() {
        int page = 1;
        int size = 5;
        String sort = "name";
        boolean ascending = true;

        CategoryModel categoryModel2 = new CategoryModel(2L, "Books", "Books and novels");

        List<CategoryModel> categoryList = Arrays.asList(categoryModel, categoryModel2);
        Paginated<CategoryModel> paginatedResponse = new Paginated<>(categoryList, page, size, 10);

        when(categoryModelPersistencePort.getCategoriesPaginated(page, size, sort, ascending))
                .thenReturn(paginatedResponse);

        Paginated<CategoryModel> result = categoryModelUseCase.getCategoriesPaginated(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getPageNumber());
        assertEquals(5, result.getPageSize());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());

        verify(categoryModelPersistencePort, times(1)).getCategoriesPaginated(page, size, sort, ascending);
    }

    @Test
    @DisplayName("Should return category names by article ID correctly")
    void shouldReturnCategoryNamesByArticleIdCorrectly() {
        Long articleId = 1L;
        articleModel.setCategories(List.of(
                new CategoryModel(1L, "Electronics", "Electronic devices"),
                new CategoryModel(2L, "Books", "Books and novels")
        ));

        when(articleModelServicePort.getArticleById(articleId)).thenReturn(articleModel);

        List<String> result = categoryModelUseCase.getCategoryNamesByArticleId(articleId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(List.of("Electronics", "Books"), result);

        verify(articleModelServicePort, times(1)).getArticleById(articleId);
    }

}
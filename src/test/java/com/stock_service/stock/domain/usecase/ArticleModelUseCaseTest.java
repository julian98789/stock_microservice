package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
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

class ArticleModelUseCaseTest {

    @Mock
    private IArticleModelPersistencePort articleModelPersistencePort;

    ArticleModel articleModel;

    @InjectMocks
    private ArticleModelUseCase articleModelUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        BrandModel brand = new BrandModel(1L, "BrandName", "BrandDescription");
        CategoryModel category = new CategoryModel(1L, "CategoryName", "CategoryDescription");
        articleModel = new ArticleModel(1L, "ArticleName", "ArticleDescription", 10, 100.0, brand, List.of(category));
    }

    @Test
    @DisplayName("Debe lanzar NameAlreadyExistsException cuando el nombre del artículo ya existe")
    void saveArticle() {
        String existingArticleName = "ArticleName";

        when(articleModelPersistencePort.existByName(existingArticleName)).thenReturn(true);

        NameAlreadyExistsException exception = assertThrows(
                NameAlreadyExistsException.class,
                () -> articleModelUseCase.saveArticle(articleModel)
        );

        assertEquals(Util.ARTICLE_NAME_ALREADY_EXISTS, exception.getMessage());
        verify(articleModelPersistencePort, never()).saveArticle(articleModel);
    }

    @Test
    @DisplayName("Deberían devolverse los artículos paginados correctamente")
    void getArticlesPaginated() {
        int page = 1;
        int size = 5;
        String sort = "name";
        boolean ascending = true;

        BrandModel brand = new BrandModel(1L, "BrandName", "BrandDescription");
        CategoryModel category = new CategoryModel(1L, "CategoryName", "CategoryDescription");

        ArticleModel article1 = new ArticleModel(1L, "Article A", "Description A", 10, 100.0, brand, List.of(category));
        ArticleModel article2 = new ArticleModel(2L, "Article B", "Description B", 20, 200.0, brand, List.of(category));

        List<ArticleModel> articleList = Arrays.asList(article1, article2);
        Paginated<ArticleModel> paginatedResponse = new Paginated<>(articleList, page, size, 10);

        when(articleModelPersistencePort.getArticlesPaginated(page, size, sort, ascending)).thenReturn(paginatedResponse);

        Paginated<ArticleModel> result = articleModelUseCase.getArticlesPaginated(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getPageNumber());
        assertEquals(5, result.getPageSize());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        verify(articleModelPersistencePort, times(1)).getArticlesPaginated(page, size, sort, ascending);
    }

    @Test
    @DisplayName("Debe reducir la cantidad del artículo correctamente")
    void reduceStock() {
        Long articleId = 1L;
        int quantityToReduce = 5;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);
        doNothing().when(articleModelPersistencePort).reduceArticleQuantity(articleId, quantityToReduce);

        articleModelUseCase.reduceStock(articleId, quantityToReduce);

        verify(articleModelPersistencePort, times(1)).getArticleById(articleId);
        verify(articleModelPersistencePort, times(1)).reduceArticleQuantity(articleId, quantityToReduce);
    }

    @Test
    @DisplayName("Debe obtener el precio del artículo por ID correctamente")
    void getArticlePriceById() {
        Long articleId = 1L;
        Double expectedPrice = 100.0;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        Double result = articleModelUseCase.getArticlePriceById(articleId);

        assertNotNull(result);
        assertEquals(expectedPrice, result);
        verify(articleModelPersistencePort, times(1)).getArticleById(articleId);
    }

    @Test
    @DisplayName("Debe obtener el artículo por ID correctamente")
    void existsArticleById() {
        Long articleId = 1L;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        boolean result = articleModelUseCase.existsArticleById(articleId);

        assertTrue(result);
        verify(articleModelPersistencePort, times(1)).getArticleById(articleId);
    }

    @Test
    @DisplayName("Debe actualizar la cantidad del artículo correctamente")
    void updateArticleQuantity() {
        Long articleId = 1L;
        int newQuantity = 20;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);
        when(articleModelPersistencePort.saveArticle(articleModel)).thenReturn(articleModel);

        ArticleModel result = articleModelUseCase.updateArticleQuantity(articleId, newQuantity);

        assertNotNull(result);
        assertEquals(newQuantity, result.getQuantity());
        verify(articleModelPersistencePort, times(1)).getArticleById(articleId);
        verify(articleModelPersistencePort, times(1)).saveArticle(articleModel);
    }

    @Test
    @DisplayName("Debe verificar si el stock es suficiente correctamente")
    void isStockAvailable() {
        Long articleId = 1L;
        int requestedQuantity = 5;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        boolean result = articleModelUseCase.isStockAvailable(articleId, requestedQuantity);

        assertTrue(result);
        verify(articleModelPersistencePort, times(1)).getArticleById(articleId);
    }


}
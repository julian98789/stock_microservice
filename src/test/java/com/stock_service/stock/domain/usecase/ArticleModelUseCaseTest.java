package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.exception.NotFoundException;
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



    @Test
    @DisplayName("Debe obtener artículo por ID correctamente")
    void getArticleById() {
        Long articleId = 1L;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        ArticleModel result = articleModelUseCase.getArticleById(articleId);

        assertNotNull(result);
        assertEquals(articleModel, result);
        verify(articleModelPersistencePort, times(1)).getArticleById(articleId);
    }


    @Test
    @DisplayName("Debe obtener todos los artículos por IDs correctamente")
    void getAllArticlesByIds() {
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleModel> articleModels = List.of(articleModel, new ArticleModel(2L, "AnotherArticle", "AnotherDescription", 5, 50.0, null, null));

        when(articleModelPersistencePort.getAllArticlesByIds(articleIds)).thenReturn(articleModels);

        List<ArticleModel> result = articleModelUseCase.getAllArticlesByIds(articleIds);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(articleModels, result);
        verify(articleModelPersistencePort, times(1)).getAllArticlesByIds(articleIds);
    }


}
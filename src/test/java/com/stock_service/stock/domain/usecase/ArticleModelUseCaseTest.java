package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.exception.InsufficientStockException;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleModelUseCaseTest {

    @Mock
    private IArticleModelPersistencePort articleModelPersistencePort;


    @InjectMocks
    private ArticleModelUseCase articleModelUseCase;

    ArticleModel articleModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        articleModel = new ArticleModel();
        articleModel.setId(1L);
        articleModel.setName("ArticleName");
        articleModel.setQuantity(10);
        articleModel.setPrice(100.0);
    }

    @Test
    @DisplayName("Should throw NameAlreadyExistsException when article name already exists")
    void shouldThrowNameAlreadyExistsExceptionWhenArticleNameAlreadyExists() {
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
    @DisplayName("Should save article correctly when name does not exist")
    void shouldSaveArticleCorrectlyWhenNameDoesNotExist() {
        when(articleModelPersistencePort.existByName(articleModel.getName())).thenReturn(false);
        when(articleModelPersistencePort.saveArticle(articleModel)).thenReturn(articleModel);

        ArticleModel result = articleModelUseCase.saveArticle(articleModel);

        assertNotNull(result);
        assertEquals(articleModel, result);
        verify(articleModelPersistencePort).saveArticle(articleModel);
    }

    @Test
    @DisplayName("Should reduce article quantity correctly")
    void shouldReduceArticleQuantityCorrectly() {
        Long articleId = 1L;
        int quantityToReduce = 5;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);
        doNothing().when(articleModelPersistencePort).reduceArticleQuantity(articleId, quantityToReduce);

        articleModelUseCase.reduceStock(articleId, quantityToReduce);

        verify(articleModelPersistencePort).getArticleById(articleId);
        verify(articleModelPersistencePort).reduceArticleQuantity(articleId, quantityToReduce);
    }

    @Test
    @DisplayName("Should throw InsufficientStockException when reducing more than available stock")
    void shouldThrowInsufficientStockExceptionWhenReducingMoreThanAvailableStock() {
        Long articleId = 1L;
        int quantityToReduce = 15;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> articleModelUseCase.reduceStock(articleId, quantityToReduce)
        );
        assertEquals(Util.INSUFFICIENT_STOCK, exception.getMessage());
        verify(articleModelPersistencePort, never()).reduceArticleQuantity(articleId, quantityToReduce);
    }

    @Test
    @DisplayName("Should return article price by ID correctly")
    void shouldReturnArticlePriceByIdCorrectly() {
        Long articleId = 1L;
        Double expectedPrice = 100.0;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        Double result = articleModelUseCase.getArticlePriceById(articleId);

        assertNotNull(result);
        assertEquals(expectedPrice, result);
        verify(articleModelPersistencePort).getArticleById(articleId);
    }

    @Test
    @DisplayName("Should return true when article exists by ID")
    void shouldReturnTrueWhenArticleExistsById() {
        Long articleId = 1L;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        boolean result = articleModelUseCase.existsArticleById(articleId);

        assertTrue(result);
        verify(articleModelPersistencePort).getArticleById(articleId);
    }

    @Test
    @DisplayName("Should return false when article does not exist by ID")
    void shouldReturnFalseWhenArticleDoesNotExistById() {
        Long articleId = 1L;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(null);

        boolean result = articleModelUseCase.existsArticleById(articleId);

        assertFalse(result);
        verify(articleModelPersistencePort).getArticleById(articleId);
    }

    @Test
    @DisplayName("Should update article quantity correctly")
    void shouldUpdateArticleQuantityCorrectly() {
        Long articleId = 1L;
        int newQuantity = 20;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);
        when(articleModelPersistencePort.saveArticle(articleModel)).thenReturn(articleModel);

        ArticleModel result = articleModelUseCase.updateArticleQuantity(articleId, newQuantity);

        assertNotNull(result);
        assertEquals(newQuantity, result.getQuantity());
        verify(articleModelPersistencePort).getArticleById(articleId);
        verify(articleModelPersistencePort).saveArticle(articleModel);
    }

    @Test
    @DisplayName("Should verify if stock is sufficient correctly")
    void shouldVerifyIfStockIsSufficientCorrectly() {
        Long articleId = 1L;
        int requestedQuantity = 5;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        boolean result = articleModelUseCase.isStockAvailable(articleId, requestedQuantity);

        assertTrue(result);
        verify(articleModelPersistencePort).getArticleById(articleId);
    }

    @Test
    @DisplayName("Should return false if stock is insufficient")
    void shouldReturnFalseIfStockIsInsufficient() {
        Long articleId = 1L;
        int requestedQuantity = 15;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        boolean result = articleModelUseCase.isStockAvailable(articleId, requestedQuantity);

        assertFalse(result);
        verify(articleModelPersistencePort).getArticleById(articleId);
    }

    @Test
    @DisplayName("Should return article by ID correctly")
    void shouldReturnArticleByIdCorrectly() {
        Long articleId = 1L;

        when(articleModelPersistencePort.getArticleById(articleId)).thenReturn(articleModel);

        ArticleModel result = articleModelUseCase.getArticleById(articleId);

        assertNotNull(result);
        assertEquals(articleModel, result);
        verify(articleModelPersistencePort).getArticleById(articleId);
    }


    @Test
    @DisplayName("Should return all articles by IDs correctly")
    void shouldReturnAllArticlesByIdsCorrectly() {
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleModel> articleModels = List.of(articleModel, new ArticleModel());

        when(articleModelPersistencePort.getAllArticlesByIds(articleIds)).thenReturn(articleModels);

        List<ArticleModel> result = articleModelUseCase.getAllArticlesByIds(articleIds);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(articleModels, result);
        verify(articleModelPersistencePort).getAllArticlesByIds(articleIds);
    }
}

package com.stock_service.stock.application.handler.articlehandler;

import com.stock_service.stock.application.dto.articledto.ArticleQuantityRequest;
import com.stock_service.stock.application.dto.articledto.ArticleRequest;
import com.stock_service.stock.application.dto.articledto.ArticleResponse;
import com.stock_service.stock.application.dto.categorydto.CategoryResponseForArticle;
import com.stock_service.stock.application.mapper.article_mapper.IArticleRequestMapper;
import com.stock_service.stock.application.mapper.article_mapper.IArticleResponseMapper;
import com.stock_service.stock.domain.api.IArticleModelServicePort;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
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

class ArticleHandlerTest {

    @Mock
    private IArticleModelServicePort articleModelServicePort;

    @Mock
    private IArticleResponseMapper articleResponseMapper;

    @Mock
    private IArticleRequestMapper articleRequestMapper;

    @Mock
    private IBrandModelPersistencePort brandModelPersistencePort;

    @Mock
    private ICategoryModelPersistencePort categoryModelPersistencePort;

    @Mock
    private IArticleModelPersistencePort articleModelPersistencePort;

    @InjectMocks
    private ArticleHandler articleHandler;

    private ArticleRequest articleRequest;
    private ArticleResponse articleResponse;
    private ArticleModel articleModel;
    private BrandModel brandModel;
    private List<CategoryModel> categoryModels;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        articleRequest = new ArticleRequest();
        articleRequest.setBrandId(1L);
        articleRequest.setCategoryIds(List.of(1L, 2L));

        articleResponse = new ArticleResponse();
        articleModel = new ArticleModel();

    }

    @Test
    @DisplayName("Should save article correctly")
    void shouldSaveArticleCorrectly() {
        brandModel = new BrandModel();
        brandModel.setId(1L);
        categoryModels = Arrays.asList(
                new CategoryModel(1L, "Smartphones", "Phones and Accessories"),
                new CategoryModel(2L, "Electronics", "Electronic Devices")
        );

        when(articleRequestMapper.articleRequestToArticleModel(articleRequest)).thenReturn(articleModel);
        when(brandModelPersistencePort.getBrandById(1L)).thenReturn(brandModel);
        when(categoryModelPersistencePort.getCategoriesByIds(List.of(1L, 2L))).thenReturn(categoryModels);
        when(articleModelServicePort.saveArticle(articleModel)).thenReturn(articleModel);
        when(articleResponseMapper.articleModelToArticleResponse(articleModel)).thenReturn(articleResponse);

        ArticleResponse result = articleHandler.saveArticle(articleRequest);

        assertNotNull(result);
        assertEquals(articleResponse, result);

        verify(articleRequestMapper).articleRequestToArticleModel(articleRequest);
        verify(brandModelPersistencePort).getBrandById(1L);
        verify(categoryModelPersistencePort).getCategoriesByIds(List.of(1L, 2L));
        verify(articleModelServicePort).saveArticle(articleModel);
        verify(articleResponseMapper).articleModelToArticleResponse(articleModel);
    }

    @Test
    @DisplayName("Should return paginated articles correctly")
    void shouldReturnPaginatedArticlesCorrectly() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        CategoryResponseForArticle categoryResponse1 = new CategoryResponseForArticle();
        categoryResponse1.setName("Smartphones");

        articleResponse.setCategories(List.of(categoryResponse1));

        Paginated<ArticleModel> paginatedArticleModel = new Paginated<>(List.of(articleModel), page, size, 1);

        when(articleModelPersistencePort.getArticlesPaginated(page, size, sort, ascending)).thenReturn(paginatedArticleModel);
        when(articleResponseMapper.articleModelToArticleResponse(articleModel)).thenReturn(articleResponse);

        Paginated<ArticleResponse> result = articleHandler.getArticlesPaginated(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(articleResponse, result.getContent().get(0));
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(1, result.getTotalPages());

        verify(articleModelPersistencePort, times(1)).getArticlesPaginated(page, size, sort, ascending);
        verify(articleResponseMapper, times(1)).articleModelToArticleResponse(articleModel);
    }

    @Test
    @DisplayName("Should get article by ID correctly")
    void shouldGetArticleByIdCorrectly() {
        when(articleModelServicePort.existsArticleById(1L)).thenReturn(true);

        boolean result = articleHandler.getArticleById(1L);

        assertTrue(result);
        verify(articleModelServicePort, times(1)).existsArticleById(1L);
    }

    @Test
    @DisplayName("Should update article quantity correctly")
    void shouldUpdateArticleQuantityCorrectly() {
        ArticleQuantityRequest request = new ArticleQuantityRequest();
        request.setQuantity(5);

        ArticleModel updatedArticle = new ArticleModel();
        updatedArticle.setId(1L);
        updatedArticle.setQuantity(7);

        when(articleModelServicePort.updateArticleQuantity(1L, 5)).thenReturn(updatedArticle);
        when(articleResponseMapper.articleModelToArticleResponse(updatedArticle)).thenReturn(articleResponse);

        ArticleResponse result = articleHandler.updateArticleQuantity(1L, request);

        assertNotNull(result);
        assertEquals(articleResponse, result);
        verify(articleModelServicePort, times(1)).updateArticleQuantity(1L, 5);
        verify(articleResponseMapper, times(1)).articleModelToArticleResponse(updatedArticle);
    }

    @Test
    @DisplayName("Should check article availability correctly")
    void shouldCheckArticleAvailabilityCorrectly() {
        when(articleModelServicePort.isStockAvailable(1L, 10)).thenReturn(true);

        boolean result = articleHandler.checkAvailabilityArticle(1L, 10);

        assertTrue(result);
        verify(articleModelServicePort, times(1)).isStockAvailable(1L, 10);
    }

    @Test
    @DisplayName("Should reduce article stock correctly")
    void shouldReduceArticleStockCorrectly() {
        ArticleQuantityRequest request = new ArticleQuantityRequest();
        request.setQuantity(5);

        doNothing().when(articleModelServicePort).reduceStock(1L, 5);

        articleHandler.reduceStock(1L, request);

        verify(articleModelServicePort, times(1)).reduceStock(1L, 5);
    }

    @Test
    @DisplayName("Should get article price by ID correctly")
    void shouldGetArticlePriceByIdCorrectly() {
        when(articleModelServicePort.getArticlePriceById(1L)).thenReturn(100.0);

        Double result = articleHandler.getArtclePriceById(1L);

        assertNotNull(result);
        assertEquals(100.0, result);
        verify(articleModelServicePort, times(1)).getArticlePriceById(1L);
    }

    @Test
    @DisplayName("Should return all articles by IDs correctly")
    void shouldReturnAllArticlesByIdsCorrectly() {
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleModel> articleModels = List.of(articleModel);

        when(articleModelServicePort.getAllArticlesByIds(articleIds)).thenReturn(articleModels);
        when(articleResponseMapper.articleModelToArticleResponse(articleModel)).thenReturn(articleResponse);

        List<ArticleResponse> result = articleHandler.getAllArticlesByIds(articleIds);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(articleResponse, result.get(0));

        verify(articleModelServicePort, times(1)).getAllArticlesByIds(articleIds);
        verify(articleResponseMapper, times(1)).articleModelToArticleResponse(articleModel);
    }

    @Test
    @DisplayName("Should return paginated articles by IDs correctly")
    void shouldReturnPaginatedArticlesByIdsCorrectly() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;
        String categoryName = "Smartphones";
        String brandName = "Samsung";
        List<Long> articleIds = Arrays.asList(1L, 2L);

        Paginated<ArticleModel> paginatedArticleModel = new Paginated<>(List.of(articleModel), page, size, 1);

        when(articleModelPersistencePort.getArticlesPaginatedByFilters(page, size, sort, ascending, categoryName,
                brandName, articleIds)).thenReturn(paginatedArticleModel);

        when(articleResponseMapper.articleModelToArticleResponse(articleModel)).thenReturn(articleResponse);

        CategoryResponseForArticle categoryResponse1 = new CategoryResponseForArticle();
        categoryResponse1.setName("Smartphones");

        articleResponse.setCategories(List.of(categoryResponse1));

        Paginated<ArticleResponse> result = articleHandler.getAllArticlesPaginatedByIds(page, size, sort, ascending, categoryName, brandName, articleIds);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(articleResponse, result.getContent().get(0));
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(1, result.getTotalPages());
        assertEquals("Smartphones", result.getContent().get(0).getCategories().get(0).getName());

        verify(articleModelPersistencePort, times(1)).getArticlesPaginatedByFilters(page, size, sort, ascending, categoryName, brandName, articleIds);
        verify(articleResponseMapper, times(1)).articleModelToArticleResponse(articleModel);
    }
}
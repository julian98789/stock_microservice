package com.stock_service.stock.application.handler.article_handler;

import com.stock_service.stock.application.dto.article_dto.ArticleQuantityRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleResponse;
import com.stock_service.stock.application.dto.category_dto.CategoryResponseForArticle;
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

        brandModel = new BrandModel(1L, "Samsung", "Samsung Electronics");
        categoryModels = Arrays.asList(
                new CategoryModel(1L, "Smartphones", "Phones and Accessories"),
                new CategoryModel(2L, "Electronics", "Electronic Devices")
        );

        articleRequest = new ArticleRequest();
        articleRequest.setBrandId(1L);
        articleRequest.setCategoryIds(List.of(1L, 2L));

        articleResponse = new ArticleResponse();
        articleModel = new ArticleModel(1L, "Galaxy S23", "Latest Samsung smartphone", 100, 999.99, brandModel, categoryModels);

        when(articleRequestMapper.articleRequestToArticleModel(articleRequest)).thenReturn(articleModel);
        when(brandModelPersistencePort.getBrandById(1L)).thenReturn(brandModel);
        when(categoryModelPersistencePort.getCategoriesByIds(List.of(1L, 2L))).thenReturn(categoryModels);
        when(articleModelServicePort.saveArticle(articleModel)).thenReturn(articleModel);
        when(articleResponseMapper.articleModelToArticleResponse(articleModel)).thenReturn(articleResponse);
    }

    @Test
    void saveArticle() {

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
    @DisplayName("Debe devolver artículos paginados correctamente")
    void getArticlesPaginated() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        articleResponse.setId(1L);
        articleResponse.setName("Galaxy S23");
        articleResponse.setDescription("Latest Samsung smartphone");
        CategoryResponseForArticle categoryResponse1 = new CategoryResponseForArticle();
        categoryResponse1.setId(1L);
        categoryResponse1.setName("Smartphones");

        CategoryResponseForArticle categoryResponse2 = new CategoryResponseForArticle();
        categoryResponse2.setId(2L);
        categoryResponse2.setName("Electronics");

        articleResponse.setCategories(Arrays.asList(categoryResponse1, categoryResponse2));

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
    @DisplayName("Debe obtener el artículo por ID correctamente")
    void getArticleById() {
        when(articleModelServicePort.existsArticleById(1L)).thenReturn(true);

        boolean result = articleHandler.getArticleById(1L);

        assertTrue(result);
        verify(articleModelServicePort, times(1)).existsArticleById(1L);
    }

    @Test
    @DisplayName("Debe actualizar la cantidad del artículo correctamente")
    void updateArticleQuantity() {
        ArticleQuantityRequest request = new ArticleQuantityRequest();
        request.setQuantity(5);

        ArticleModel updatedArticle = new ArticleModel(1L, "Galaxy S23", "Latest Samsung smartphone", 105, 999.99, brandModel, categoryModels);
        when(articleModelServicePort.updateArticleQuantity(1L, 5)).thenReturn(updatedArticle);
        when(articleResponseMapper.articleModelToArticleResponse(updatedArticle)).thenReturn(articleResponse);

        ArticleResponse result = articleHandler.updateArticleQuantity(1L, request);

        assertNotNull(result);
        assertEquals(articleResponse, result);
        verify(articleModelServicePort, times(1)).updateArticleQuantity(1L, 5);
        verify(articleResponseMapper, times(1)).articleModelToArticleResponse(updatedArticle);
    }

    @Test
    @DisplayName("Debe verificar si el stock es suficiente correctamente")
    void checkAvailabilityArticle() {
        when(articleModelServicePort.isStockAvailable(1L, 10)).thenReturn(true);

        boolean result = articleHandler.checkAvailabilityArticle(1L, 10);

        assertTrue(result);
        verify(articleModelServicePort, times(1)).isStockAvailable(1L, 10);
    }

    @Test
    @DisplayName("Debe reducir la cantidad del artículo correctamente")
    void reduceStock() {
        ArticleQuantityRequest request = new ArticleQuantityRequest();
        request.setQuantity(5);

        doNothing().when(articleModelServicePort).reduceStock(1L, 5);

        articleHandler.reduceStock(1L, request);

        verify(articleModelServicePort, times(1)).reduceStock(1L, 5);
    }

    @Test
    @DisplayName("Debe obtener el precio del artículo por ID correctamente")
    void getArticlePriceById() {
        when(articleModelServicePort.getArticlePriceById(1L)).thenReturn(100.0);

        Double result = articleHandler.getArtclePriceById(1L);

        assertNotNull(result);
        assertEquals(100.0, result);
        verify(articleModelServicePort, times(1)).getArticlePriceById(1L);
    }
}

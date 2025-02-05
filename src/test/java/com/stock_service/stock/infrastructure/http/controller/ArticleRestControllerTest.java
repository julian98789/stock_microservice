package com.stock_service.stock.infrastructure.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock_service.stock.application.dto.articledto.ArticleCartRequest;
import com.stock_service.stock.application.dto.articledto.ArticleQuantityRequest;
import com.stock_service.stock.application.dto.articledto.ArticleRequest;
import com.stock_service.stock.application.dto.articledto.ArticleResponse;
import com.stock_service.stock.application.dto.branddto.BrandResponse;
import com.stock_service.stock.application.dto.categorydto.CategoryResponseForArticle;
import com.stock_service.stock.application.handler.articlehandler.ArticleHandler;

import com.stock_service.stock.domain.util.Paginated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ArticleRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArticleHandler articleHandler;

    @InjectMocks
    private ArticleRestController articleRestController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(articleRestController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should save article correctly")
    void shouldSaveArticleCorrectly() throws Exception {

        BrandResponse brandResponse = new BrandResponse();
        List<CategoryResponseForArticle> categories = List.of(new CategoryResponseForArticle());

        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setName("Laptop");
        articleRequest.setDescription("Laptop de alta gama");
        articleRequest.setPrice(1500.00);
        articleRequest.setBrandId(1L);
        articleRequest.setCategoryIds(List.of(2L, 3L));

        ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.setId(1L);
        articleResponse.setName(articleRequest.getName());
        articleResponse.setDescription(articleRequest.getDescription());
        articleResponse.setPrice(articleRequest.getPrice());
        articleResponse.setBrand(brandResponse);
        articleResponse.setCategories(categories);

        when(articleHandler.saveArticle(any(ArticleRequest.class))).thenReturn(articleResponse);

        mockMvc.perform(post("/api/article/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(articleResponse)));

        verify(articleHandler, times(1)).saveArticle(any(ArticleRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return paginated articles correctly")
    void shouldReturnPaginatedArticlesCorrectly() throws Exception {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        ArticleResponse articleResponse = new ArticleResponse();
        Paginated<ArticleResponse> paginatedResponse = new Paginated<>(List.of(articleResponse), page, size, 1);

        when(articleHandler.getArticlesPaginated(page, size, sort, ascending)).thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/article/listar")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .param("ascending", String.valueOf(ascending))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(articleHandler, times(1)).getArticlesPaginated(page, size, sort, ascending);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return article by ID correctly")
    void shouldReturnArticleByIdCorrectly() throws Exception {
        when(articleHandler.getArticleById(anyLong())).thenReturn(true);

        mockMvc.perform(get("/api/article/{articleId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));

        verify(articleHandler, times(1)).getArticleById(1L);
    }

    @Test
    @WithMockUser(roles = "AUX_BODEGA")
    @DisplayName("Should update article quantity correctly")
    void shouldUpdateArticleQuantityCorrectly() throws Exception {
        ArticleQuantityRequest request = new ArticleQuantityRequest();
        request.setQuantity(5);

        mockMvc.perform(patch("/api/article/quantity/{articleId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        ArgumentCaptor<ArticleQuantityRequest> captor = ArgumentCaptor.forClass(ArticleQuantityRequest.class);
        verify(articleHandler, times(1)).updateArticleQuantity(eq(1L), captor.capture());
        assertEquals(5, captor.getValue().getQuantity());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    @DisplayName("Should verify if stock is sufficient correctly")
    void shouldVerifyIfStockIsSufficientCorrectly() throws Exception {
        when(articleHandler.checkAvailabilityArticle(anyLong(), anyInt())).thenReturn(true);

        mockMvc.perform(get("/api/article/{articleId}/check-quantity/{quantity}", 1L, 10))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));

        verify(articleHandler, times(1)).checkAvailabilityArticle(1L, 10);
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    @DisplayName("Should reduce article quantity correctly")
    void shouldReduceArticleQuantityCorrectly() throws Exception {
        ArticleQuantityRequest request = new ArticleQuantityRequest();
        request.setQuantity(5);

        mockMvc.perform(patch("/api/article/{articleId}/subtract-stock", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        ArgumentCaptor<ArticleQuantityRequest> captor = ArgumentCaptor.forClass(ArticleQuantityRequest.class);
        verify(articleHandler, times(1)).reduceStock(eq(1L), captor.capture());
        assertEquals(5, captor.getValue().getQuantity());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    @DisplayName("Should return article price by ID correctly")
    void shouldReturnArticlePriceByIdCorrectly() throws Exception {
        when(articleHandler.getArtclePriceById(anyLong())).thenReturn(100.0);

        mockMvc.perform(get("/api/article/{articleId}/price", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("100.0"));

        verify(articleHandler, times(1)).getArtclePriceById(1L);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "CLIENTE", "AUX_BODEGA"})
    @DisplayName("Should return paginated articles by IDs correctly")
    void shouldReturnPaginatedArticlesByIdsCorrectly() throws Exception {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;
        ArticleCartRequest articleCartRequest = new ArticleCartRequest();
        articleCartRequest.setArticleIds(List.of(1L, 2L));

        Paginated<ArticleResponse> paginatedResponse = new Paginated<>(List.of(new ArticleResponse()), page, size, 1);

        when(articleHandler.getAllArticlesPaginatedByIds(page, size, sort, ascending, null, null, articleCartRequest.getArticleIds()))
                .thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/article/article-cart")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .param("ascending", String.valueOf(ascending))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCartRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(paginatedResponse)));

        verify(articleHandler, times(1)).getAllArticlesPaginatedByIds(page, size, sort, ascending, null, null, articleCartRequest.getArticleIds());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    @DisplayName("Should return all articles by IDs correctly")
    void shouldReturnAllArticlesByIdsCorrectly() throws Exception {
        ArticleCartRequest articleCartRequest = new ArticleCartRequest();
        articleCartRequest.setArticleIds(List.of(1L, 2L));

        List<ArticleResponse> articleResponses = List.of(new ArticleResponse());

        when(articleHandler.getAllArticlesByIds(articleCartRequest.getArticleIds())).thenReturn(articleResponses);

        mockMvc.perform(get("/api/article/get-all-articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleCartRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(articleResponses)));

        verify(articleHandler, times(1)).getAllArticlesByIds(articleCartRequest.getArticleIds());
    }
}
package com.stock_service.stock.infrastructure.input.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock_service.stock.application.dto.article_dto.ArticleRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleResponse;
import com.stock_service.stock.application.dto.brand_dto.BrandResponse;
import com.stock_service.stock.application.dto.category_dto.CategoryResponseForArticle;
import com.stock_service.stock.application.handler.article_handler.ArticleHandler;
import com.stock_service.stock.domain.util.Paginated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @DisplayName("Debe guardar el artículo correctamente")
    void saveArticle() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setName("ArticleName");
        articleRequest.setDescription("ArticleDescription");
        articleRequest.setQuantity(10);
        articleRequest.setPrice(100.0);
        articleRequest.setBrandId(1L);
        articleRequest.setCategoryIds(List.of(1L, 2L));

        ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.setId(1L);
        articleResponse.setName("ArticleName");
        articleResponse.setDescription("ArticleDescription");
        articleResponse.setQuantity(10);
        articleResponse.setPrice(100.0);
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(1L);
        brandResponse.setName("BrandName");
        brandResponse.setDescription("BrandDescription");
        articleResponse.setBrand(brandResponse);
        articleResponse.setCategories(List.of(
                new CategoryResponseForArticle() {{
                    setId(1L);
                    setName("Category1");
                }},
                new CategoryResponseForArticle() {{
                    setId(2L);
                    setName("Category2");
                }}
        ));

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
    @DisplayName("Debe devolver artículos paginados correctamente")
    void getArticles() throws Exception {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        ArticleResponse articleResponse = new ArticleResponse();
        Paginated<ArticleResponse> paginatedResponse = new Paginated<>(List.of(articleResponse), page, size, 1);

        when(articleHandler.getArticles(page, size, sort, ascending)).thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/article/listar")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .param("ascending", String.valueOf(ascending))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(articleHandler, times(1)).getArticles(page, size, sort, ascending);
    }
}
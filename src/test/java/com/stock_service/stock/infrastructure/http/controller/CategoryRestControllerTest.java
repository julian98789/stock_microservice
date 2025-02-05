package com.stock_service.stock.infrastructure.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock_service.stock.application.dto.categorydto.CategoryRequest;
import com.stock_service.stock.application.dto.categorydto.CategoryResponse;
import com.stock_service.stock.application.handler.categoryhandler.CategoryHandler;
import com.stock_service.stock.domain.util.Paginated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class CategoryRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryHandler categoryHandler;

    @InjectMocks
    private CategoryRestController categoryRestController;

    private ObjectMapper objectMapper;

    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(categoryRestController).build();

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Electronics");
        categoryRequest.setDescription("Electronic devices");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("New Category");
        categoryResponse.setDescription("New Description");
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should save category correctly")
    void shouldSaveCategoryCorrectly() throws Exception {
        when(categoryHandler.saveCategory(any(CategoryRequest.class))).thenReturn(categoryResponse);

        mockMvc.perform(post("/api/category/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(categoryResponse)));

        verify(categoryHandler, times(1)).saveCategory(any(CategoryRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return paginated categories correctly")
    void shouldReturnPaginatedCategoriesCorrectly() throws Exception {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        Paginated<CategoryResponse> paginatedResponse = new Paginated<>(List.of(categoryResponse), page, size, 1);

        when(categoryHandler.getCategories(page, size, sort, ascending)).thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/category/listar")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .param("ascending", String.valueOf(ascending))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(paginatedResponse)));

        verify(categoryHandler, times(1)).getCategories(page, size, sort, ascending);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "CLIENT", "AUX_BODEGA"})
    @DisplayName("Should return category names by article ID correctly")
    void shouldReturnCategoryNamesByArticleIdCorrectly() throws Exception {
        Long articleId = 1L;
        List<String> categoryNames = List.of("Electronics", "Home Appliances");

        String expectedResponse = objectMapper.writeValueAsString(categoryNames);

        when(categoryHandler.getCategoryNamesByArticleId(articleId)).thenReturn(categoryNames);

        mockMvc.perform(get("/api/category/names-by-article/{articleId}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse));

        verify(categoryHandler, times(1)).getCategoryNamesByArticleId(articleId);
    }
}

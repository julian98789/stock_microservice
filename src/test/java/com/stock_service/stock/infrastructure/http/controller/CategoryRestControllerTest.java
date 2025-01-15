package com.stock_service.stock.infrastructure.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock_service.stock.application.dto.category_dto.CategoryRequest;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.application.handler.category_handler.CategoryHandler;
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

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(categoryRestController).build();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("New Category");
        categoryResponse.setDescription("New Description");
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe guardar la categoría correctamente")
    void saveCategoryCase1() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Electronics");
        categoryRequest.setDescription("Electronic devices");
        CategoryResponse categoryResponse = new CategoryResponse();


        when(categoryHandler.saveCategory(any(CategoryRequest.class))).thenReturn(categoryResponse);

        mockMvc.perform(post("/api/category/crear")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                        .andExpect(status().isCreated())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(categoryHandler, times(1)).saveCategory(any(CategoryRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe devolver categorías paginadas correctamente")
    void getCategorie() throws Exception {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        CategoryResponse categoryResponse = new CategoryResponse();
        Paginated<CategoryResponse> paginatedResponse = new Paginated<>(List.of(categoryResponse), page, size, 1);

        when(categoryHandler.getCategories(page, size, sort, ascending)).thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/category/listar")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .param("ascending", String.valueOf(ascending))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(categoryHandler, times(1)).getCategories(page, size, sort, ascending);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "CLIENTE", "AUX_BODEGA"})
    @DisplayName("Debe devolver nombres de categorías por ID de artículo correctamente")
    void getCategoryNamesByArticleId() throws Exception {
        Long articleId = 1L;
        List<String> categoryNames = List.of("Electronics", "Home Appliances");

        when(categoryHandler.getCategoryNamesByArticleId(articleId)).thenReturn(categoryNames);

        mockMvc.perform(get("/api/category/names-by-article/{articleId}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(categoryHandler, times(1)).getCategoryNamesByArticleId(articleId);
    }

}

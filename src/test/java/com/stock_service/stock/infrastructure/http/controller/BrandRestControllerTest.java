package com.stock_service.stock.infrastructure.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock_service.stock.application.dto.branddto.BrandRequest;
import com.stock_service.stock.application.dto.branddto.BrandResponse;
import com.stock_service.stock.application.handler.brandhandler.BrandHandler;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class BrandRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BrandHandler brandHandler;

    @InjectMocks
    private BrandRestController brandRestController;

    private ObjectMapper objectMapper;

    private BrandRequest brandRequest;
    private BrandResponse brandResponse;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(brandRestController).build();

        brandResponse = new BrandResponse();
        brandResponse.setId(1L);
        brandResponse.setName("New Category");
        brandResponse.setDescription("New Description");

        brandRequest = new BrandRequest();
        brandRequest.setName("Electronics");
        brandRequest.setDescription("Electronic devices");

    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should save brand correctly")
    void shouldSaveBrandCorrectly() throws Exception {
        when(brandHandler.saveBrand(any(BrandRequest.class))).thenReturn(brandResponse);

        mockMvc.perform(post("/api/brand/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(brandResponse)));

        verify(brandHandler, times(1)).saveBrand(any(BrandRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return paginated brands correctly")
    void shouldReturnPaginatedBrandsCorrectly() throws Exception {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        Paginated<BrandResponse> paginatedResponse = new Paginated<>(List.of(brandResponse), page, size, 1);

        when(brandHandler.getBrandsPaginated(page, size, sort, ascending)).thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/brand/listar")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .param("ascending", String.valueOf(ascending))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(paginatedResponse)));

        verify(brandHandler, times(1)).getBrandsPaginated(page, size, sort, ascending);
    }
}
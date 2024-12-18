package com.stock_service.stock.infrastructure.output.adapter;

import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.infrastructure.output.entity.CategoryEntity;
import com.stock_service.stock.infrastructure.output.mapper.ICategoryEntityMapper;
import com.stock_service.stock.infrastructure.output.repository.ICategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryJpaAdapterTest {

    @InjectMocks
    private CategoryJpaAdapter categoryJpaAdapter;

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private ICategoryEntityMapper categoryEntityMapper;

    private CategoryModel categoryModel;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryModel = new CategoryModel(1L, "Electronics", "Electronic devices");
        categoryEntity = new CategoryEntity();

        when(categoryEntityMapper.categoryModelToCategoryEntity(categoryModel)).thenReturn(categoryEntity);
        when(categoryEntityMapper.categoryEntityToCategoryModel(categoryEntity)).thenReturn(categoryModel);
    }


    @Test
    void saveCategory() {
        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);

        CategoryModel result = categoryJpaAdapter.saveCategory(categoryModel);

        assertNotNull(result);
        assertEquals(categoryModel, result);

        verify(categoryRepository).save(categoryEntity);

        verify(categoryEntityMapper).categoryModelToCategoryEntity(categoryModel);
        verify(categoryEntityMapper).categoryEntityToCategoryModel(categoryEntity);
    }

    @Test
    void existByNameCase1() {
        when(categoryRepository.findByName("categoryName")).thenReturn(Optional.of(categoryEntity));

        boolean result = categoryJpaAdapter.existByName("categoryName");

        assertTrue(result);

        verify(categoryRepository).findByName("categoryName");
    }

    @Test
    void existByNameCase2() {
        when(categoryRepository.findByName("nonExistentCategory")).thenReturn(Optional.empty());

        boolean result = categoryJpaAdapter.existByName("nonExistentCategory");

        assertFalse(result);

        verify(categoryRepository).findByName("nonExistentCategory");
    }
}
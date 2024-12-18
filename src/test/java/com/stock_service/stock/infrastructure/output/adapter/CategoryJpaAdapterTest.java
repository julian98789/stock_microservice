package com.stock_service.stock.infrastructure.output.adapter;

import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.output.entity.CategoryEntity;
import com.stock_service.stock.infrastructure.output.mapper.ICategoryEntityMapper;
import com.stock_service.stock.infrastructure.output.repository.ICategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("Debe devolver categorías paginadas correctamente")
    void getCategories() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        List<CategoryEntity> categoryEntitiesList = List.of(categoryEntity);
        Page<CategoryEntity> categoryEntities = new PageImpl<>(categoryEntitiesList, pageRequest, categoryEntitiesList.size());

        when(categoryRepository.findAll(pageRequest)).thenReturn(categoryEntities);
        when(categoryEntityMapper.categoryEntityToCategoryModel(categoryEntity)).thenReturn(categoryModel);

        Paginated<CategoryModel> result = categoryJpaAdapter.getCategories(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(categoryModel, result.getContent().get(0));
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(1, result.getTotalPages());

        verify(categoryRepository, times(1)).findAll(pageRequest);
        verify(categoryEntityMapper, times(1)).categoryEntityToCategoryModel(categoryEntity);
    }
}
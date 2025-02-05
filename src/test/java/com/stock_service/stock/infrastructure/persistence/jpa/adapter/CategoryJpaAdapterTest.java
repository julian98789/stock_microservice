package com.stock_service.stock.infrastructure.persistence.jpa.adapter;

import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.persistence.jpa.entity.CategoryEntity;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.ICategoryEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.ICategoryRepository;
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

        categoryModel = new CategoryModel();
        categoryEntity = new CategoryEntity();

        when(categoryEntityMapper.categoryModelToCategoryEntity(categoryModel)).thenReturn(categoryEntity);
        when(categoryEntityMapper.categoryEntityToCategoryModel(categoryEntity)).thenReturn(categoryModel);
    }


    @Test
    @DisplayName("Should save category correctly")
    void shouldSaveCategoryCorrectly() {
        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);

        CategoryModel result = categoryJpaAdapter.saveCategory(categoryModel);

        assertNotNull(result);
        assertEquals(categoryModel, result);

        verify(categoryRepository).save(categoryEntity);
        verify(categoryEntityMapper).categoryModelToCategoryEntity(categoryModel);
        verify(categoryEntityMapper).categoryEntityToCategoryModel(categoryEntity);
    }

    @Test
    @DisplayName("Should return true when category exists by name")
    void shouldReturnTrueWhenCategoryExistsByName() {
        when(categoryRepository.findByName("categoryName")).thenReturn(Optional.of(categoryEntity));

        boolean result = categoryJpaAdapter.existByName("categoryName");

        assertTrue(result);

        verify(categoryRepository).findByName("categoryName");
    }

    @Test
    @DisplayName("Should return false when category does not exist by name")
    void shouldReturnFalseWhenCategoryDoesNotExistByName() {
        when(categoryRepository.findByName("nonExistentCategory")).thenReturn(Optional.empty());

        boolean result = categoryJpaAdapter.existByName("nonExistentCategory");

        assertFalse(result);

        verify(categoryRepository).findByName("nonExistentCategory");
    }

    @Test
    @DisplayName("Should return paginated categories correctly")
    void shouldReturnPaginatedCategoriesCorrectly() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        List<CategoryEntity> categoryEntitiesList = List.of(categoryEntity);
        Page<CategoryEntity> categoryEntities = new PageImpl<>(categoryEntitiesList, pageRequest, categoryEntitiesList.size());

        when(categoryRepository.findAll(pageRequest)).thenReturn(categoryEntities);

        Paginated<CategoryModel> result = categoryJpaAdapter.getCategoriesPaginated(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(categoryModel, result.getContent().get(0));
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(1, result.getTotalPages());

        verify(categoryRepository, times(1)).findAll(pageRequest);
        verify(categoryEntityMapper, times(1)).categoryEntityToCategoryModel(categoryEntity);
    }



    @Test
    @DisplayName("Should return categories by IDs correctly")
    void shouldReturnCategoriesByIdsCorrectly() {
        List<Long> ids = List.of(1L, 2L);
        List<CategoryEntity> categoryEntities = List.of(categoryEntity, new CategoryEntity());
        List<CategoryModel> categoryModels = List.of(categoryModel, new CategoryModel());

        when(categoryRepository.findAllById(ids)).thenReturn(categoryEntities);
        when(categoryEntityMapper.categoryEntityToCategoryModel(categoryEntities.get(0))).thenReturn(categoryModels.get(0));
        when(categoryEntityMapper.categoryEntityToCategoryModel(categoryEntities.get(1))).thenReturn(categoryModels.get(1));

        List<CategoryModel> result = categoryJpaAdapter.getCategoriesByIds(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(categoryModels, result);

        verify(categoryRepository, times(1)).findAllById(ids);
        verify(categoryEntityMapper, times(1)).categoryEntityToCategoryModel(categoryEntities.get(0));
        verify(categoryEntityMapper, times(1)).categoryEntityToCategoryModel(categoryEntities.get(1));
    }
}

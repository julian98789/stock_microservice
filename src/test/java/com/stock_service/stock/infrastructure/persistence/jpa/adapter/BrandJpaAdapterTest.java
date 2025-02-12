package com.stock_service.stock.infrastructure.persistence.jpa.adapter;

import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.persistence.jpa.entity.BrandEntity;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.IBrandEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.IBrandRepository;
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

class BrandJpaAdapterTest {

    @InjectMocks
    private BrandJpaAdapter brandJpaAdapter;

    @Mock
    private IBrandRepository brandRepository;

    @Mock
    private IBrandEntityMapper brandEntityMapper;

    private BrandModel brandModel;
    private BrandEntity brandEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        brandModel = new BrandModel();
        brandEntity = new BrandEntity();

        when(brandEntityMapper.brandModelToBrandEntity(brandModel)).thenReturn(brandEntity);
        when(brandEntityMapper.brandEntityToBrandModel(brandEntity)).thenReturn(brandModel);


    }

    @Test
    @DisplayName("Should save brand correctly")
    void shouldSaveBrandCorrectly() {
        when(brandRepository.save(brandEntity)).thenReturn(brandEntity);

        BrandModel result = brandJpaAdapter.saveBrand(brandModel);

        assertNotNull(result);
        assertEquals(brandModel, result);

        verify(brandRepository).save(brandEntity);
        verify(brandEntityMapper).brandModelToBrandEntity(brandModel);
        verify(brandEntityMapper).brandEntityToBrandModel(brandEntity);
    }

    @Test
    @DisplayName("Should return true when brand exists by name")
    void shouldReturnTrueWhenBrandExistsByName() {
        when(brandRepository.findByName("brandName")).thenReturn(Optional.of(brandEntity));

        boolean result = brandJpaAdapter.existsByName("brandName");

        assertTrue(result);

        verify(brandRepository).findByName("brandName");
    }

    @Test
    @DisplayName("Should return false when brand does not exist by name")
    void shouldReturnFalseWhenBrandDoesNotExistByName() {
        when(brandRepository.findByName("nonExistentBrand")).thenReturn(Optional.empty());

        boolean result = brandJpaAdapter.existsByName("nonExistentBrand");

        assertFalse(result);

        verify(brandRepository).findByName("nonExistentBrand");
    }

    @Test
    @DisplayName("Should return paginated brands correctly")
    void shouldReturnPaginatedBrandsCorrectly() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        List<BrandEntity> brandEntitiesList = List.of(brandEntity);
        Page<BrandEntity> brandEntities = new PageImpl<>(brandEntitiesList, pageRequest, brandEntitiesList.size());

        when(brandRepository.findAll(pageRequest)).thenReturn(brandEntities);

        Paginated<BrandModel> result = brandJpaAdapter.getBrandsPaginated(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(brandModel, result.getContent().get(0));
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(1, result.getTotalPages());

        verify(brandRepository, times(1)).findAll(pageRequest);
        verify(brandEntityMapper, times(1)).brandEntityToBrandModel(brandEntity);
    }

    @Test
    @DisplayName("Should return brand by ID correctly")
    void shouldReturnBrandByIdCorrectly() {
        Long id = 1L;
        when(brandRepository.findById(id)).thenReturn(Optional.of(brandEntity));

        BrandModel result = brandJpaAdapter.getBrandById(id);

        assertNotNull(result);
        assertEquals(brandModel, result);

        verify(brandRepository, times(1)).findById(id);
        verify(brandEntityMapper, times(1)).brandEntityToBrandModel(brandEntity);
    }
}
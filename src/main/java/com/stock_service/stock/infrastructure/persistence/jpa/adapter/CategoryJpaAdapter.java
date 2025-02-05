package com.stock_service.stock.infrastructure.persistence.jpa.adapter;

import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.persistence.jpa.entity.CategoryEntity;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.ICategoryEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryModelPersistencePort {


    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public CategoryModel saveCategory(CategoryModel categoryModel) {

        CategoryEntity categoryEntity = categoryEntityMapper.categoryModelToCategoryEntity(categoryModel);

        categoryEntity = categoryRepository.save(categoryEntity);

        return categoryEntityMapper.categoryEntityToCategoryModel(categoryEntity);
    }

    @Override
    public boolean existByName(String name) {

        return categoryRepository.findByName(name).isPresent();
    }

    @Override
    public Paginated<CategoryModel> getCategoriesPaginated(int page, int size, String sort, boolean ascending) {

        PageRequest pageRequest = PageRequest.of(page, size, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sort);

        Page<CategoryEntity> categoryEntities = categoryRepository.findAll(pageRequest);

        List<CategoryModel> categoryModels = categoryEntities.stream()
                .map(categoryEntityMapper::categoryEntityToCategoryModel)
                .toList();

        return new Paginated<>(
                categoryModels,
                categoryEntities.getNumber(),
                categoryEntities.getSize(),
                categoryEntities.getTotalPages()
        );
    }

    @Override
    public List<CategoryModel> getCategoriesByIds(List<Long> ids) {

        List<CategoryEntity> categoryEntities = categoryRepository.findAllById(ids);

        return categoryEntities.stream()
                .map(categoryEntityMapper::categoryEntityToCategoryModel)
                .toList();
    }


}

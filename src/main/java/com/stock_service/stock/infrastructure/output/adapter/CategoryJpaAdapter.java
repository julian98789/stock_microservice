package com.stock_service.stock.infrastructure.output.adapter;

import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.infrastructure.output.entity.CategoryEntity;
import com.stock_service.stock.infrastructure.output.mapper.ICategoryEntityMapper;
import com.stock_service.stock.infrastructure.output.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

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


}

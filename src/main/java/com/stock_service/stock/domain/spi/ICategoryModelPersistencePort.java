package com.stock_service.stock.domain.spi;

import com.stock_service.stock.domain.model.CategoryModel;

public interface ICategoryModelPersistencePort {
    boolean existByName(String name);
    CategoryModel saveCategory(CategoryModel categoryModel);

}

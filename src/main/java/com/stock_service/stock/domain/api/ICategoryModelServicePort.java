package com.stock_service.stock.domain.api;

import com.stock_service.stock.domain.model.CategoryModel;

public interface ICategoryModelServicePort {
    CategoryModel saveCategory (CategoryModel categoryModel);
    boolean existByName (String name);
}

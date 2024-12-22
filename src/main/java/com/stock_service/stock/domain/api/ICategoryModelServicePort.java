package com.stock_service.stock.domain.api;

import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.util.Paginated;

public interface ICategoryModelServicePort {
    CategoryModel saveCategory (CategoryModel categoryModel);
    Paginated<CategoryModel> getCategories(int page, int size, String sort, boolean ascending);
}

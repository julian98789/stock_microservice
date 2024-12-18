package com.stock_service.stock.domain.spi;

import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.util.Paginated;

import java.util.List;

public interface ICategoryModelPersistencePort {
    boolean existByName(String name);
    CategoryModel saveCategory(CategoryModel categoryModel);
    Paginated<CategoryModel> getCategories(int page, int size, String sort, boolean ascending); // Actualización aquí
    List<CategoryModel> getCategoriesByIds(List<Long> ids);
}

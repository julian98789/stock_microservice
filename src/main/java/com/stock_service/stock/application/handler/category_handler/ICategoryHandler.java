package com.stock_service.stock.application.handler.category_handler;

import com.stock_service.stock.application.dto.category_dto.CategoryRequest;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.domain.util.Paginated;

public interface ICategoryHandler {
    CategoryResponse saveCategory(CategoryRequest categoryRequest);
    Paginated<CategoryResponse> getCategories(int page, int size, String sort, boolean ascending);
}

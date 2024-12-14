package com.stock_service.stock.application.handler.category_handler;

import com.stock_service.stock.application.dto.category_dto.CategoryRequest;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;

public interface ICategoryHandler {
    CategoryResponse saveCategory(CategoryRequest categoryRequest);
}

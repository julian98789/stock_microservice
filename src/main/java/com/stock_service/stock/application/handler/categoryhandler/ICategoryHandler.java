package com.stock_service.stock.application.handler.categoryhandler;

import com.stock_service.stock.application.dto.categorydto.CategoryRequest;
import com.stock_service.stock.application.dto.categorydto.CategoryResponse;
import com.stock_service.stock.domain.util.Paginated;

import java.util.List;

public interface ICategoryHandler {
    CategoryResponse saveCategory(CategoryRequest categoryRequest);
    Paginated<CategoryResponse> getCategories(int page, int size, String sort, boolean ascending);
    List<String> getCategoryNamesByArticleId(Long articleId);
}

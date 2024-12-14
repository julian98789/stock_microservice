package com.stock_service.stock.application.handler.category_handler;

import com.stock_service.stock.application.dto.category_dto.CategoryRequest;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.application.mapper.category_mapper.ICategoryRequestMapper;
import com.stock_service.stock.application.mapper.category_mapper.ICategoryResponseMapper;
import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.model.CategoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryHandler implements ICategoryHandler{
    private final ICategoryModelServicePort categoryModelServicePort;
    private final ICategoryRequestMapper categoryRequestMapper;
    private final ICategoryResponseMapper categoryResponseMapper;


    @Override
    public CategoryResponse saveCategory(CategoryRequest categoryRequest) {
        CategoryModel categoryModel = categoryRequestMapper.categoryrequestToCategoryModel(categoryRequest);
        CategoryModel saveCategory = categoryModelServicePort.saveCategory(categoryModel);
        return categoryResponseMapper.categoryModelToCategoryResponse(saveCategory);
    }
}

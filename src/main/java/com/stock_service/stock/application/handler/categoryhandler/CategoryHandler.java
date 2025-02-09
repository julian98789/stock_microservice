package com.stock_service.stock.application.handler.categoryhandler;

import com.stock_service.stock.application.dto.categorydto.CategoryRequest;
import com.stock_service.stock.application.dto.categorydto.CategoryResponse;
import com.stock_service.stock.application.mapper.categorymapper.ICategoryRequestMapper;
import com.stock_service.stock.application.mapper.categorymapper.ICategoryResponseMapper;
import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.util.Paginated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryHandler implements ICategoryHandler {
    private final ICategoryModelServicePort categoryModelServicePort;
    private final ICategoryRequestMapper categoryRequestMapper;
    private final ICategoryResponseMapper categoryResponseMapper;


    @Override
    public CategoryResponse saveCategory(CategoryRequest categoryRequest) {

        CategoryModel categoryModel = categoryRequestMapper.categoryRequestToCategoryModel(categoryRequest);

        CategoryModel savedCategory = categoryModelServicePort.saveCategory(categoryModel);

        return categoryResponseMapper.categoryModelToCategoryResponse(savedCategory);
    }

    @Override
    public Paginated<CategoryResponse> getCategories(int page, int size, String sort, boolean ascending) {

        Paginated<CategoryModel> categories = categoryModelServicePort.getCategoriesPaginated(page, size, sort, ascending);

        List<CategoryResponse> categoryResponse = categories.getContent().stream()
                .map(categoryResponseMapper::categoryModelToCategoryResponse)
                .toList();

        return new Paginated<>(
                categoryResponse,
                categories.getPageNumber(),
                categories.getPageSize(),
                categories.getTotalPages()
        );
    }

    @Override
    public List<String> getCategoryNamesByArticleId(Long articleId) {
        return categoryModelServicePort.getCategoryNamesByArticleId(articleId);
    }
}

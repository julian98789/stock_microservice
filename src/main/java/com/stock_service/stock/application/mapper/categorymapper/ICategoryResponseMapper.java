package com.stock_service.stock.application.mapper.categorymapper;

import com.stock_service.stock.application.dto.categorydto.CategoryResponseForArticle;
import com.stock_service.stock.application.dto.categorydto.CategoryResponse;
import com.stock_service.stock.domain.model.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICategoryResponseMapper {

    CategoryResponse categoryModelToCategoryResponse(CategoryModel categoryModel);
    CategoryResponseForArticle categoryResponseForArticleToCategoryResponse (CategoryModel category);
}

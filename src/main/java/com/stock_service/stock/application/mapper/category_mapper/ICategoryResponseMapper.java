package com.stock_service.stock.application.mapper.category_mapper;

import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.domain.model.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICategoryResponseMapper {

    CategoryResponse categoryModelToCategoryResponse(CategoryModel categoryModel);

}

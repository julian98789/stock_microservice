package com.stock_service.stock.infrastructure.persistence.jpa.mapper;

import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.infrastructure.persistence.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface ICategoryEntityMapper {

    CategoryEntity categoryModelToCategoryEntity(CategoryModel categoryModel);
    CategoryModel categoryEntityToCategoryModel(CategoryEntity categoryEntity);
}

package com.stock_service.stock.infrastructure.output.mapper;

import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.infrastructure.output.entity.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IArticleEntityMapper {

    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "categories", source = "categories")
    ArticleEntity articleModelToArticleEntity(ArticleModel articleModel);


    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "categories", source = "categories")
    ArticleModel articleEntityToArticleModel(ArticleEntity articleEntity);
}

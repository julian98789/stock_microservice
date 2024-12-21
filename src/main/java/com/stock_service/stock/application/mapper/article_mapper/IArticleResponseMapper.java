package com.stock_service.stock.application.mapper.article_mapper;

import com.stock_service.stock.application.dto.article_dto.ArticleResponse;
import com.stock_service.stock.domain.model.ArticleModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IArticleResponseMapper {

    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "brand", source = "brand")
    ArticleResponse articleModelToArticleResponse(ArticleModel articleModel);
}

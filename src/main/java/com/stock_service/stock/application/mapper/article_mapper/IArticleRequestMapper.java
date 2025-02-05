package com.stock_service.stock.application.mapper.article_mapper;

import com.stock_service.stock.application.dto.articledto.ArticleRequest;
import com.stock_service.stock.domain.model.ArticleModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IArticleRequestMapper {

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "brand", ignore = true)
    ArticleModel articleRequestToArticleModel(ArticleRequest articleRequest);

}

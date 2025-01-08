package com.stock_service.stock.application.handler.article_handler;

import com.stock_service.stock.application.dto.article_dto.ArticleQuantityRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleResponse;
import com.stock_service.stock.domain.util.Paginated;

public interface IArticleHandler {

    ArticleResponse saveArticle(ArticleRequest articleRequest);
    Paginated<ArticleResponse> getArticles(int page, int size, String sort, boolean ascending);
    ArticleResponse getArticleById(Long id);
    ArticleResponse updateArticleQuantity(Long articleId, ArticleQuantityRequest articleQuantityRequest);
    public boolean CheckAvailabilityArticle(Long articleId, Integer requestedQuantity);

}

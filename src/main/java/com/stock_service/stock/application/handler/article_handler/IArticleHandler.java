package com.stock_service.stock.application.handler.article_handler;

import com.stock_service.stock.application.dto.article_dto.ArticleQuantityRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleResponse;
import com.stock_service.stock.domain.util.Paginated;

import java.util.List;

public interface IArticleHandler {

    ArticleResponse saveArticle(ArticleRequest articleRequest);
    Paginated<ArticleResponse> getArticlesPaginated(int page, int size, String sort, boolean ascending);

    boolean getArticleById(Long id);

    ArticleResponse updateArticleQuantity(Long articleId, ArticleQuantityRequest articleQuantityRequest);

    boolean checkAvailabilityArticle(Long articleId, Integer requestedQuantity);

    void reduceStock(Long articleId, ArticleQuantityRequest request);

    Double getArtclePriceById(Long articleId);

    Paginated<ArticleResponse> getAllArticlesPaginatedByIds(
            int page, int size, String sort, boolean ascending, String categoryName, String brandName, List<Long> articleIds);




    }

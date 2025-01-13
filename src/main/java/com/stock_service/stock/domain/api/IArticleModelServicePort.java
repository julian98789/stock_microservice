package com.stock_service.stock.domain.api;

import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.util.Paginated;

import java.util.List;


public interface IArticleModelServicePort {
    ArticleModel saveArticle(ArticleModel articleModel);

    boolean existsArticleById(Long id);

    ArticleModel updateArticleQuantity(Long id, int quantity);

    boolean isStockAvailable(Long articleId, int requestedQuantity);

    void reduceStock(Long articleId, int quantityToReduce);

    Double getArticlePriceById(Long articleId);

    ArticleModel getArticleById(Long id);

    List<ArticleModel> getAllArticlesByIds(List<Long> articleIds);


    }

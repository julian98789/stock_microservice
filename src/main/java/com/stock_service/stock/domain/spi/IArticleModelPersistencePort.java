package com.stock_service.stock.domain.spi;

import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.util.Paginated;

public interface IArticleModelPersistencePort {

    ArticleModel saveArticle(ArticleModel articleModel);
    Paginated<ArticleModel> getArticles(int page, int size, String sort, boolean ascending);
    boolean existByName(String name);
    ArticleModel getArticleById(Long id);

}

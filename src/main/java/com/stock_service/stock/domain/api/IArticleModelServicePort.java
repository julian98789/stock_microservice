package com.stock_service.stock.domain.api;

import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.util.Paginated;


public interface IArticleModelServicePort {
    ArticleModel saveArticle(ArticleModel articleModel);
    Paginated<ArticleModel> getArticles(int page, int size, String sort, boolean ascending);
}

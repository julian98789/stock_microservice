package com.stock_service.stock.domain.spi;

import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.util.Paginated;

import java.util.List;

public interface IArticleModelPersistencePort {

    ArticleModel saveArticle(ArticleModel articleModel);
    Paginated<ArticleModel> getArticlesPaginated(int page, int size, String sort, boolean ascending);
    boolean existByName(String name);
    ArticleModel getArticleById(Long id);
    void reduceArticleQuantity(Long articleId, int quantityToReduce);
    Paginated<ArticleModel> getArticlesPaginatedByFilters(
            int page, int size, String sort, boolean ascending, String categoryName, String brandName, List<Long> articleIds);
}

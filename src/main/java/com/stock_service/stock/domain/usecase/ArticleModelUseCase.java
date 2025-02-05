package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.IArticleModelServicePort;
import com.stock_service.stock.domain.exception.InsufficientStockException;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.exception.NotFoundException;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.util.Util;


import java.util.List;


public class ArticleModelUseCase implements IArticleModelServicePort {

    private final IArticleModelPersistencePort articlePersistencePort;

    public ArticleModelUseCase(IArticleModelPersistencePort articlePersistencePort) {
        this.articlePersistencePort = articlePersistencePort;
    }

    @Override
    public ArticleModel saveArticle(ArticleModel articleModel) {

        if (articlePersistencePort.existByName(articleModel.getName())) {

            throw new NameAlreadyExistsException(Util.ARTICLE_NAME_ALREADY_EXISTS);
        }
        return articlePersistencePort.saveArticle(articleModel);
    }


    @Override
    public boolean existsArticleById(Long id) {
        try {
            ArticleModel article = articlePersistencePort.getArticleById(id);

            return article != null;
        } catch (Exception e) {

            return false;
        }
    }

    @Override
    public ArticleModel updateArticleQuantity(Long id, int quantity) {

        ArticleModel article = articlePersistencePort.getArticleById(id);

        validateArticle(article);

        article.setQuantity(quantity);

        return articlePersistencePort.saveArticle(article);
    }

    @Override
    public boolean isStockAvailable(Long articleId, int requestedQuantity) {
        ArticleModel article = articlePersistencePort.getArticleById(articleId);

        validateArticle(article);

        return article.getQuantity() >= requestedQuantity;
    }

    public void reduceStock(Long articleId, int quantityToReduce) {

        ArticleModel article = articlePersistencePort.getArticleById(articleId);

        if (article.getQuantity() < quantityToReduce) {
            throw new InsufficientStockException(Util.INSUFFICIENT_STOCK);
        }

        articlePersistencePort.reduceArticleQuantity(articleId, quantityToReduce);
    }

    @Override
    public Double getArticlePriceById(Long articleId) {
        ArticleModel article = articlePersistencePort.getArticleById(articleId);

        validateArticle(article);

        return article.getPrice();
    }

    @Override
    public ArticleModel getArticleById(Long id) {
        return articlePersistencePort.getArticleById(id);
    }

    @Override
    public List<ArticleModel> getAllArticlesByIds(List<Long> articleIds) {
        return articlePersistencePort.getAllArticlesByIds(articleIds);
    }

    private void validateArticle(ArticleModel article) {
        if (article == null) {
            throw new NotFoundException(Util.ARTICLE_NOT_FOUND);
        }
    }

}

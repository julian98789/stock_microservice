package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.IArticleServicePort;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.spi.IArticlePersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.Util;


public class ArticleModelUseCase implements IArticleServicePort {

    private final IArticlePersistencePort articlePersistencePort;


    public ArticleModelUseCase(IArticlePersistencePort articlePersistencePort) {
        this.articlePersistencePort = articlePersistencePort;
    }

    @Override
    public ArticleModel saveArticle(ArticleModel articleModel) {

        if (articlePersistencePort.existByName(articleModel.getName())) {
            throw new NameAlreadyExistsException(Util.ARTICLE_NAME_ALREADY_EXISTS);

        }
        ArticleModel savedArticle = articlePersistencePort.saveArticle(articleModel);

        return savedArticle;
    }

    @Override
    public Paginated<ArticleModel> getArticles(int page, int size, String sort, boolean ascending) {

        Paginated<ArticleModel> article = articlePersistencePort.getArticles(page, size, sort, ascending);

        return article;

    }



}

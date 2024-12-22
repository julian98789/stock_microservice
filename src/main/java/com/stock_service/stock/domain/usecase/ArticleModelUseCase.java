package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.IArticleModelServicePort;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ArticleModelUseCase implements IArticleModelServicePort {

    private final IArticleModelPersistencePort articlePersistencePort;

    private static final Logger logger = LoggerFactory.getLogger(ArticleModelUseCase.class);


    public ArticleModelUseCase(IArticleModelPersistencePort articlePersistencePort) {
        this.articlePersistencePort = articlePersistencePort;
    }

    @Override
    public ArticleModel saveArticle(ArticleModel articleModel) {

        logger.info("[Dominio] Recibiendo solicitud para guardar la marcar con nombre: {}", articleModel.getName());
        if (articlePersistencePort.existByName(articleModel.getName())) {

            logger.warn("[Dominio] El nombre del articulo '{}' ya existe. Lanzando excepcion NameAlreadyExistsException", articleModel.getName());
            throw new NameAlreadyExistsException(Util.ARTICLE_NAME_ALREADY_EXISTS);

        }

        ArticleModel savedArticle = articlePersistencePort.saveArticle(articleModel);

        logger.info("[Dominio] Articulo guardada exitosamente con id: {} y nombre: {}", savedArticle.getId(), savedArticle.getName());
        return savedArticle;
    }

    @Override
    public Paginated<ArticleModel> getArticles(int page, int size, String sort, boolean ascending) {

        logger.info("[Dominio] Recibiendo solicitud para obtener articulos con los siguientes parametros: pagina = {}, tamano = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        Paginated<ArticleModel> article = articlePersistencePort.getArticles(page, size, sort, ascending);

        logger.info("[Dominio] Se obtuvieron {} artculos en la pagina {}", article.getContent().size(), page);
        return article;

    }



}

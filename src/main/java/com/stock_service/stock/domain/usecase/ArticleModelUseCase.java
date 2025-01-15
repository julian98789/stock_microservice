package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.IArticleModelServicePort;
import com.stock_service.stock.domain.exception.InsufficientStockException;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.exception.NotFoundException;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


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
    public boolean existsArticleById(Long id) {
        logger.info("[Dominio] Recibiendo solicitud para obtener artículo con ID: {}", id);
        try {
            ArticleModel article = articlePersistencePort.getArticleById(id);
            if (article == null) {
                logger.warn("[Dominio] No se encontró un artículo con ID: {}", id);
                return false;
            }
            logger.info("[Dominio] Artículo encontrado con ID: {} y nombre: {}", article.getId(), article.getName());
            return true;
        } catch (Exception e) {
            logger.error("Error al obtener el artículo con ID: {}", id, e);
            return false;
        }
    }

    @Override
    public ArticleModel updateArticleQuantity(Long id, int quantity) {
        logger.info("[Dominio] Actualizando cantidad del artículo con ID: {}", id);

        ArticleModel article = articlePersistencePort.getArticleById(id);

        validateArticle(article);

        article.setQuantity(quantity);

        ArticleModel updatedArticle = articlePersistencePort.saveArticle(article);
        logger.info("[Dominio] Artículo actualizado con nueva cantidad: {}", updatedArticle.getQuantity());
        return updatedArticle;
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

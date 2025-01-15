package com.stock_service.stock.application.handler.article_handler;


import com.stock_service.stock.application.dto.article_dto.ArticleQuantityRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleResponse;
import com.stock_service.stock.application.dto.category_dto.CategoryResponseForArticle;
import com.stock_service.stock.application.mapper.article_mapper.IArticleRequestMapper;
import com.stock_service.stock.application.mapper.article_mapper.IArticleResponseMapper;
import com.stock_service.stock.domain.api.IArticleModelServicePort;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleHandler implements IArticleHandler {

    private final IArticleModelServicePort articleModelServicePort;
    private final IArticleResponseMapper articleResponseMapper;
    private final IArticleRequestMapper articleRequestMapper;
    private final IArticleModelPersistencePort articleModelPersistencePort;
    private final IBrandModelPersistencePort brandModelPersistencePort;
    private final ICategoryModelPersistencePort categoryModelPersistencePort;

    private static final Logger logger = LoggerFactory.getLogger(ArticleHandler.class);

    @Override
    public ArticleResponse saveArticle(ArticleRequest articleRequest) {

        logger.info("[Aplicacion] Recibiendo solicitud de creacion de Articulo");
        ArticleModel articleModel = articleRequestMapper.articleRequestToArticleModel(articleRequest);

        BrandModel brandModel = brandModelPersistencePort.getBrandById(articleRequest.getBrandId());
        articleModel.setBrand(brandModel);

        List<CategoryModel> categoryModels = categoryModelPersistencePort.getCategoriesByIds(articleRequest.getCategoryIds());
        articleModel.setCategories(categoryModels);

        ArticleModel savedArticle = articleModelServicePort.saveArticle(articleModel);

        logger.info("[Aplicacion] Respuesta mapeada a articleResponse");
        return articleResponseMapper.articleModelToArticleResponse(savedArticle);
    }

    @Override
    public Paginated<ArticleResponse> getArticlesPaginated(int page, int size, String sort, boolean ascending) {

        logger.info("[Aplicacion] Recibiendo solicitud para obtener articulos desde Controller con parametros - Pagina: {}, Tamaño: {}, Orden: {}, Ascendente: {}", page, size, sort, ascending);
        Paginated<ArticleModel> paginatedArticle = articleModelPersistencePort.getArticlesPaginated(page, size, sort, ascending);

        List<ArticleResponse> articleResponses = paginatedArticle.getContent().stream()
                .map(article -> {
                    ArticleResponse articleResponse = articleResponseMapper.articleModelToArticleResponse(article);
                    List<CategoryResponseForArticle> sortedCategories = articleResponse.getCategories().stream()
                            .sorted(Comparator.comparing(CategoryResponseForArticle::getName)).toList();
                    articleResponse.setCategories(sortedCategories);
                    return articleResponse;
                }).toList();

        articleResponses = articleResponses.stream()
                .sorted(Comparator.comparing((ArticleResponse articleResponse) -> articleResponse.getCategories().get(0).getName())
                        .thenComparing(articleResponse -> articleResponse.getCategories().size()))
                .toList();

        logger.info("[Aplicacion] Se mapeo {} articulos a articleResponses", articleResponses.size());
        return new Paginated<>(
                articleResponses,
                paginatedArticle.getPageNumber(),
                paginatedArticle.getPageSize(),
                paginatedArticle.getTotalPages()
        );

    }

    @Override
    public boolean getArticleById(Long id) {
        logger.info("[Aplicación] Recibiendo solicitud para obtener artículo con ID: {}", id);
        return articleModelServicePort.existsArticleById(id);

    }

    @Override
    public ArticleResponse updateArticleQuantity(Long articleId, ArticleQuantityRequest articleQuantityRequest) {
        ArticleModel updatedArticle = articleModelServicePort.updateArticleQuantity(articleId, articleQuantityRequest.getQuantity());

        return articleResponseMapper.articleModelToArticleResponse(updatedArticle);
    }

    @Override
    public boolean checkAvailabilityArticle(Long articleId, Integer requestedQuantity) {
        return articleModelServicePort.isStockAvailable(articleId, requestedQuantity);
    }

    @Override
    public void reduceStock(Long articleId, ArticleQuantityRequest request) {
        articleModelServicePort.reduceStock(articleId, request.getQuantity());
    }

    @Override
    public Double getArtclePriceById(Long articleId) {
        return articleModelServicePort.getArticlePriceById(articleId);
    }

    @Override
    public Paginated<ArticleResponse> getAllArticlesPaginatedByIds(int page, int size, String sort, boolean ascending, String categoryName, String brandName, List<Long> articleIds) {

        Paginated<ArticleModel> paginatedArticles = articleModelPersistencePort.getArticlesPaginatedByFilters(
                page, size, sort, ascending, categoryName, brandName, articleIds);

        List<ArticleResponse> articleResponses = paginatedArticles.getContent().stream()
                .map(article -> {
                    ArticleResponse articleResponse = articleResponseMapper.articleModelToArticleResponse(article);
                    List<CategoryResponseForArticle> sortedCategories = articleResponse.getCategories().stream()
                            .sorted(Comparator.comparing(CategoryResponseForArticle::getName)).toList();
                    articleResponse.setCategories(sortedCategories);
                    return articleResponse;
                }).toList();

        logger.info("[Aplicación] Mapeo completado, total artículos procesados: {}", articleResponses.size());
        return new Paginated<>(
                articleResponses,
                paginatedArticles.getPageNumber(),
                paginatedArticles.getPageSize(),
                paginatedArticles.getTotalElements()
        );
    }

    @Override
    public List<ArticleResponse> getAllArticlesByIds(List<Long> articleIds) {
        List<ArticleModel> articles = articleModelServicePort.getAllArticlesByIds(articleIds);
        return articles.stream()
                .map(articleResponseMapper::articleModelToArticleResponse)
                .toList();
    }


}

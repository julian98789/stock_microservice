package com.stock_service.stock.application.handler.articlehandler;


import com.stock_service.stock.application.dto.articledto.ArticleQuantityRequest;
import com.stock_service.stock.application.dto.articledto.ArticleRequest;
import com.stock_service.stock.application.dto.articledto.ArticleResponse;
import com.stock_service.stock.application.dto.categorydto.CategoryResponseForArticle;
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


    @Override
    public ArticleResponse saveArticle(ArticleRequest articleRequest) {

        ArticleModel articleModel = articleRequestMapper.articleRequestToArticleModel(articleRequest);

        BrandModel brandModel = brandModelPersistencePort.getBrandById(articleRequest.getBrandId());
        articleModel.setBrand(brandModel);

        List<CategoryModel> categoryModels = categoryModelPersistencePort.getCategoriesByIds(articleRequest.getCategoryIds());
        articleModel.setCategories(categoryModels);

        ArticleModel savedArticle = articleModelServicePort.saveArticle(articleModel);

        return articleResponseMapper.articleModelToArticleResponse(savedArticle);
    }

    @Override
    public Paginated<ArticleResponse> getArticlesPaginated(int page, int size, String sort, boolean ascending) {

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

        return new Paginated<>(
                articleResponses,
                paginatedArticle.getPageNumber(),
                paginatedArticle.getPageSize(),
                paginatedArticle.getTotalPages()
        );

    }

    @Override
    public boolean getArticleById(Long id) {

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

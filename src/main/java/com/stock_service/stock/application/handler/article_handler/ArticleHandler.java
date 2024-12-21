package com.stock_service.stock.application.handler.article_handler;


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
    public Paginated<ArticleResponse> getArticles(int page, int size, String sort, boolean ascending) {
        Paginated<ArticleModel> paginatedArticle = articleModelPersistencePort.getArticles(page, size, sort, ascending);

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
}

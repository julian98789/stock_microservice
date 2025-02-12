package com.stock_service.stock.infrastructure.persistence.jpa.adapter;

import com.stock_service.stock.domain.exception.NotFoundException;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.Util;
import com.stock_service.stock.infrastructure.persistence.jpa.entity.ArticleEntity;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.IArticleEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.IArticleRepository;
import com.stock_service.stock.infrastructure.persistence.jpa.specifications.ArticleSpecifications;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@RequiredArgsConstructor
public class ArticleJpaAdapter implements IArticleModelPersistencePort {

    private final IArticleRepository articleRepository;
    private final IArticleEntityMapper articleEntityMapper;



    @Override
    public ArticleModel saveArticle(ArticleModel articleModel) {

        ArticleEntity articleEntity = articleEntityMapper.articleModelToArticleEntity(articleModel);
        ArticleEntity savedArticle = articleRepository.save(articleEntity);

        return articleEntityMapper.articleEntityToArticleModel(savedArticle);
    }

    @Override
    public Paginated<ArticleModel> getArticlesPaginated(int page, int size, String sort, boolean ascending) {

        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<ArticleEntity> articleEntities = articleRepository.findAll(pageRequest);

        List<ArticleModel> articles = articleEntities.stream()
                .map(articleEntityMapper::articleEntityToArticleModel).toList();

        return new Paginated<>(
                articles,
                articleEntities.getNumber(),
                articleEntities.getSize(),
                articleEntities.getTotalElements()
        );
    }

    @Override
    public boolean existByName(String name) {
        return articleRepository.findByName(name).isPresent();
    }

    @Override
    public ArticleModel getArticleById(Long id) {

        return articleRepository.findById(id)
                .map(articleEntityMapper::articleEntityToArticleModel)
                .orElse(null);
    }

    @Override
    public void reduceArticleQuantity(Long articleId, int quantityToReduce) {
        ArticleEntity entity = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException(Util.ARTICLE_NOT_FOUND));

        int newQuantity = entity.getQuantity() - quantityToReduce;

        entity.setQuantity(newQuantity);
        articleRepository.save(entity);
    }

    @Override
    public Paginated<ArticleModel> getArticlesPaginatedByFilters(int page, int size, String sort, boolean ascending, String categoryName, String brandName, List<Long> articleIds) {
        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));

        Specification<ArticleEntity> specification = Specification.where(ArticleSpecifications.inArticleIds(articleIds))
                .and(ArticleSpecifications.byCategoryName(categoryName))
                .and(ArticleSpecifications.byBrandName(brandName));

        Page<ArticleEntity> articleEntities = articleRepository.findAll(specification, pageRequest);

        List<ArticleModel> articles = articleEntities.stream()
                .map(articleEntityMapper::articleEntityToArticleModel).toList();

        return new Paginated<>(
                articles,
                articleEntities.getNumber(),
                articleEntities.getSize(),
                articleEntities.getTotalElements()
        );
    }

    @Override
    public List<ArticleModel> getAllArticlesByIds(List<Long> articleIds) {
        List<ArticleEntity> articleEntities = articleRepository.findAllById(articleIds);
        return articleEntityMapper.toArticleModelList(articleEntities);
    }



}

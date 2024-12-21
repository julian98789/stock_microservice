package com.stock_service.stock.infrastructure.output.adapter;

import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.output.entity.ArticleEntity;
import com.stock_service.stock.infrastructure.output.mapper.IArticleEntityMapper;
import com.stock_service.stock.infrastructure.output.repository.IArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
    public Paginated<ArticleModel> getArticles(int page, int size, String sort, boolean ascending) {
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
}

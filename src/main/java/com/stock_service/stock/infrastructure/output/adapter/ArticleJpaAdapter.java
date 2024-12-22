package com.stock_service.stock.infrastructure.output.adapter;

import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.output.entity.ArticleEntity;
import com.stock_service.stock.infrastructure.output.mapper.IArticleEntityMapper;
import com.stock_service.stock.infrastructure.output.repository.IArticleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class ArticleJpaAdapter implements IArticleModelPersistencePort {

    private final IArticleRepository articleRepository;
    private final IArticleEntityMapper articleEntityMapper;

    private static final Logger logger = LoggerFactory.getLogger(BrandJpaAdapter.class);


    @Override
    public ArticleModel saveArticle(ArticleModel articleModel) {

        logger.info("[Infraestructura] Recibiendo solicitud para guardar el articulo con nombre: {}", articleModel.getName());
        ArticleEntity articleEntity = articleEntityMapper.articleModelToArticleEntity(articleModel);
        ArticleEntity savedArticle = articleRepository.save(articleEntity);

        logger.info("[Infraestructura] Mapeo de entidad a modelo completado, articulo retornada con id: {}", savedArticle.getId());
        return articleEntityMapper.articleEntityToArticleModel(savedArticle);
    }

    @Override
    public Paginated<ArticleModel> getArticles(int page, int size, String sort, boolean ascending) {

        logger.info("[Infraestructura] Recibiendo solicitud para obtener articulos con los siguientes parametros: pagina = {}, tamano = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<ArticleEntity> articleEntities = articleRepository.findAll(pageRequest);

        List<ArticleModel> articles = articleEntities.stream()
                .map(articleEntityMapper::articleEntityToArticleModel).toList();

        logger.info("[Infraestructura] Se han mapeado {} articulos desde entidad a modelo", articles.size());
        return new Paginated<>(
                articles,
                articleEntities.getNumber(),
                articleEntities.getSize(),
                articleEntities.getTotalElements()
        );
    }

    @Override
    public boolean existByName(String name) {
        logger.info("[Infraestructura] Recibiendo solicitud para verificar existencia de articulo con nombre: {}", name);

        logger.info("[Infraestructura] No se encontro articulo con el nombre {}", name);
        return articleRepository.findByName(name).isPresent();
    }
}

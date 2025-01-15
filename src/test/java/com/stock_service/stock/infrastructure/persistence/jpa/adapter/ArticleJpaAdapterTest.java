package com.stock_service.stock.infrastructure.persistence.jpa.adapter;

import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.persistence.jpa.entity.ArticleEntity;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.IArticleEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.IArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleJpaAdapterTest {

    @InjectMocks
    private ArticleJpaAdapter articleJpaAdapter;

    @Mock
    private IArticleRepository articleRepository;

    @Mock
    private IArticleEntityMapper articleEntityMapper;

    private ArticleModel articleModel;
    private ArticleEntity articleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        BrandModel brand = new BrandModel(1L, "BrandName", "BrandDescription");
        CategoryModel category = new CategoryModel(1L, "CategoryName", "CategoryDescription");

        articleModel = new ArticleModel(1L, "ArticleName", "ArticleDescription", 10, 100.0, brand, List.of(category));
        articleEntity = new ArticleEntity();

        when(articleEntityMapper.articleModelToArticleEntity(articleModel)).thenReturn(articleEntity);
        when(articleEntityMapper.articleEntityToArticleModel(articleEntity)).thenReturn(articleModel);
    }

    @Test
    @DisplayName("Debe guardar el artículo correctamente")
    void saveArticle() {
        when(articleRepository.save(articleEntity)).thenReturn(articleEntity);

        ArticleModel result = articleJpaAdapter.saveArticle(articleModel);

        assertNotNull(result);
        assertEquals(articleModel, result);

        verify(articleRepository).save(articleEntity);
        verify(articleEntityMapper).articleModelToArticleEntity(articleModel);
        verify(articleEntityMapper).articleEntityToArticleModel(articleEntity);
    }

    @Test
    @DisplayName("Debe verificar la existencia del artículo por nombre correctamente (caso existente)")
    void existByNameCase1() {
        when(articleRepository.findByName("ArticleName")).thenReturn(Optional.of(articleEntity));

        boolean result = articleJpaAdapter.existByName("ArticleName");

        assertTrue(result);

        verify(articleRepository).findByName("ArticleName");
    }

    @Test
    @DisplayName("Debe verificar la existencia del artículo por nombre correctamente (caso no existente)")
    void existByNameCase2() {
        when(articleRepository.findByName("NonExistentArticle")).thenReturn(Optional.empty());

        boolean result = articleJpaAdapter.existByName("NonExistentArticle");

        assertFalse(result);

        verify(articleRepository).findByName("NonExistentArticle");
    }

    @Test
    @DisplayName("Debe devolver artículos paginados correctamente")
    void getArticlesPaginated() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        List<ArticleEntity> articleEntitiesList = List.of(articleEntity);
        Page<ArticleEntity> articleEntities = new PageImpl<>(articleEntitiesList, pageRequest, articleEntitiesList.size());

        when(articleRepository.findAll(pageRequest)).thenReturn(articleEntities);
        when(articleEntityMapper.articleEntityToArticleModel(articleEntity)).thenReturn(articleModel);

        Paginated<ArticleModel> result = articleJpaAdapter.getArticlesPaginated(page, size, sort, ascending);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(articleModel, result.getContent().get(0));
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(1, result.getTotalPages());

        verify(articleRepository, times(1)).findAll(pageRequest);
        verify(articleEntityMapper, times(1)).articleEntityToArticleModel(articleEntity);
    }

    @Test
    @DisplayName("Debe obtener artículo por ID correctamente")
    void getArticleById() {
        Long articleId = 1L;
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(articleEntity));

        ArticleModel result = articleJpaAdapter.getArticleById(articleId);

        assertNotNull(result);
        assertEquals(articleModel, result);

        verify(articleRepository).findById(articleId);
        verify(articleEntityMapper).articleEntityToArticleModel(articleEntity);
    }

    @Test
    @DisplayName("Debe reducir la cantidad de artículos correctamente")
    void reduceArticleQuantity() {
        Long articleId = 1L;
        int quantityToReduce = 5;
        int initialQuantity = 10;
        articleEntity.setQuantity(initialQuantity);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(articleEntity));

        articleJpaAdapter.reduceArticleQuantity(articleId, quantityToReduce);

        assertEquals(initialQuantity - quantityToReduce, articleEntity.getQuantity());
        verify(articleRepository).save(articleEntity);
    }

    @Test
    @DisplayName("Debe devolver todos los artículos por IDs correctamente")
    void getAllArticlesByIds() {
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleEntity> articleEntities = List.of(articleEntity, articleEntity);

        when(articleRepository.findAllById(articleIds)).thenReturn(articleEntities);
        when(articleEntityMapper.toArticleModelList(articleEntities)).thenReturn(List.of(articleModel, articleModel));

        List<ArticleModel> result = articleJpaAdapter.getAllArticlesByIds(articleIds);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(articleModel, result.get(0));
        assertEquals(articleModel, result.get(1));

        verify(articleRepository).findAllById(articleIds);
        verify(articleEntityMapper).toArticleModelList(articleEntities);
    }



}
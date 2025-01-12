package com.stock_service.stock.infrastructure.configuration;

import com.stock_service.stock.domain.api.IArticleModelServicePort;
import com.stock_service.stock.domain.api.IBrandModelServicePort;
import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.spi.IArticleModelPersistencePort;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.usecase.ArticleModelUseCase;
import com.stock_service.stock.domain.usecase.BrandModelUseCase;
import com.stock_service.stock.domain.usecase.CategoryModelUseCase;
import com.stock_service.stock.infrastructure.persistence.jpa.adapter.ArticleJpaAdapter;
import com.stock_service.stock.infrastructure.persistence.jpa.adapter.BrandJpaAdapter;
import com.stock_service.stock.infrastructure.persistence.jpa.adapter.CategoryJpaAdapter;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.IArticleEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.IBrandEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.ICategoryEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.IArticleRepository;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.IBrandRepository;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;
    private final IBrandRepository brandRepository;
    private final IBrandEntityMapper brandEntityMapper;
    private final IArticleRepository articleRepository;
    private final IArticleEntityMapper articleEntityMapper;

    @Bean
    public ICategoryModelPersistencePort categoryModelPersistencePort(){
        return new CategoryJpaAdapter(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public IArticleModelPersistencePort articleModelPersistencePort() {
        return new ArticleJpaAdapter(articleRepository, articleEntityMapper);
    }

    @Bean
    public IArticleModelServicePort iArticleServicePort(IArticleModelPersistencePort articleModelPersistencePort) {
        return new ArticleModelUseCase(articleModelPersistencePort);
    }

    @Bean
    public ICategoryModelServicePort categoryModelServicePort() {
        // Aquí inyectamos correctamente IArticleModelServicePort
        return new CategoryModelUseCase(categoryModelPersistencePort(), iArticleServicePort(articleModelPersistencePort()));
    }

    @Bean
    public IBrandModelPersistencePort brandModelPersistencePort(){
        return new BrandJpaAdapter(brandRepository, brandEntityMapper);
    }

    @Bean
    public IBrandModelServicePort brandModelServicePort(){
        return new BrandModelUseCase(brandModelPersistencePort());
    }
}
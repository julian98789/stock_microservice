package com.stock_service.stock.infrastructure.configuration;

import com.stock_service.stock.domain.api.IBrandModelServicePort;
import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.usecase.BrandModelUseCase;
import com.stock_service.stock.domain.usecase.CategoryModelUseCase;
import com.stock_service.stock.infrastructure.output.adapter.BrandJpaAdapter;
import com.stock_service.stock.infrastructure.output.adapter.CategoryJpaAdapter;
import com.stock_service.stock.infrastructure.output.mapper.IBrandEntityMapper;
import com.stock_service.stock.infrastructure.output.mapper.ICategoryEntityMapper;
import com.stock_service.stock.infrastructure.output.repository.IBrandRepository;
import com.stock_service.stock.infrastructure.output.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfigurationCategory {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;
    private final IBrandRepository brandRepository;
    private final IBrandEntityMapper brandEntityMapper;

    @Bean
    public ICategoryModelPersistencePort categoryModelPersistencePort(){
        return new CategoryJpaAdapter(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public ICategoryModelServicePort categoryModelServicePort(){
        return new CategoryModelUseCase(categoryModelPersistencePort());
    }

    @Bean
    public IBrandModelPersistencePort brandModelPersistencePort(){
        return new BrandJpaAdapter(brandRepository, brandEntityMapper);
    }

    @Bean
    IBrandModelServicePort brandModelServicePort(){
        return new BrandModelUseCase(brandModelPersistencePort());
    }

}

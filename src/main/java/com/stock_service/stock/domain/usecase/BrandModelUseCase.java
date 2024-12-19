package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.IBrandModelServicePort;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.UtilMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrandModelUseCase implements IBrandModelServicePort{

    private static final Logger logger = LoggerFactory.getLogger(CategoryModelUseCase.class);

    private final IBrandModelPersistencePort brandModelPersistencePort;

    public BrandModelUseCase(IBrandModelPersistencePort brandModelPersistencePort) {
        this.brandModelPersistencePort = brandModelPersistencePort;
    }

    @Override
    public BrandModel saveBrand(BrandModel brandModel) {
        if(brandModelPersistencePort.existsByName(brandModel.getName())){
            throw new NameAlreadyExistsException(UtilMessage.BRAND_NAME_ALREADY_EXISTS);
        }
        BrandModel savedBrand = brandModelPersistencePort.saveBrand(brandModel);

        return savedBrand;
    }

    @Override
    public boolean existByName(String name) {
        boolean exists = brandModelPersistencePort.existsByName(name);
        return exists;

    }

    @Override
    public Paginated<BrandModel> getBrands(int page, int size, String sort, boolean ascending) {
        Paginated<BrandModel> brands = brandModelPersistencePort.getBrands(page, size, sort, ascending);
        return brands;
    }
}

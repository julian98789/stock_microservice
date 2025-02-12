package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.IBrandModelServicePort;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.Util;


public class BrandModelUseCase implements IBrandModelServicePort{


    private final IBrandModelPersistencePort brandModelPersistencePort;

    public BrandModelUseCase(IBrandModelPersistencePort brandModelPersistencePort) {
        this.brandModelPersistencePort = brandModelPersistencePort;
    }

    @Override
    public BrandModel saveBrand(BrandModel brandModel) {

        if(brandModelPersistencePort.existsByName(brandModel.getName())){

            throw new NameAlreadyExistsException(Util.BRAND_NAME_ALREADY_EXISTS);
        }

        return brandModelPersistencePort.saveBrand(brandModel);
    }


    @Override
    public Paginated<BrandModel> getBrandsPaginated(int page, int size, String sort, boolean ascending) {

        return brandModelPersistencePort.getBrandsPaginated(page, size, sort, ascending);
    }
}

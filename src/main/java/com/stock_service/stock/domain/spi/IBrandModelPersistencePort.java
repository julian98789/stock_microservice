package com.stock_service.stock.domain.spi;

import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.util.Paginated;

public interface IBrandModelPersistencePort {
    BrandModel saveBrand(BrandModel brandModel);

    boolean existsByName(String name);

    Paginated<BrandModel> getBrands(int page, int size, String sort, boolean ascending);
    BrandModel getBrandById(Long id);
}

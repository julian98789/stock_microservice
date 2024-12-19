package com.stock_service.stock.domain.api;

import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.util.Paginated;

public interface IBrandModelServicePort {
    BrandModel saveBrand(BrandModel brandModel);

    boolean existByName(String name);

    Paginated<BrandModel> getBrands(int page, int size, String sort, boolean ascending);
}

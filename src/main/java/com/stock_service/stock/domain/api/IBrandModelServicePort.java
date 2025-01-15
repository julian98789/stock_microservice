package com.stock_service.stock.domain.api;

import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.util.Paginated;

public interface IBrandModelServicePort {
    BrandModel saveBrand(BrandModel brandModel);

    Paginated<BrandModel> getBrandsPaginated(int page, int size, String sort, boolean ascending);
}

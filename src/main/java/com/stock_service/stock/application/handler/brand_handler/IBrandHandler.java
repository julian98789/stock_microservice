package com.stock_service.stock.application.handler.brand_handler;

import com.stock_service.stock.application.dto.brand_dto.BrandRequest;
import com.stock_service.stock.application.dto.brand_dto.BrandResponse;
import com.stock_service.stock.domain.util.Paginated;

public interface IBrandHandler {
    BrandResponse saveBrand(BrandRequest brandRequest);
    Paginated<BrandResponse> getBrandsPaginated(int page, int size, String sort, boolean ascending);
}

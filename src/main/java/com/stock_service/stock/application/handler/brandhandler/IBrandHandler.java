package com.stock_service.stock.application.handler.brandhandler;

import com.stock_service.stock.application.dto.branddto.BrandRequest;
import com.stock_service.stock.application.dto.branddto.BrandResponse;
import com.stock_service.stock.domain.util.Paginated;

public interface IBrandHandler {
    BrandResponse saveBrand(BrandRequest brandRequest);
    Paginated<BrandResponse> getBrandsPaginated(int page, int size, String sort, boolean ascending);
}

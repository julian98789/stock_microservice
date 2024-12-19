package com.stock_service.stock.application.handler.brand_handler;

import com.stock_service.stock.application.dto.brand_dto.BrandRequest;
import com.stock_service.stock.application.dto.brand_dto.BrandResponse;
import com.stock_service.stock.application.mapper.brand_mapper.IBrandRequestMapper;
import com.stock_service.stock.application.mapper.brand_mapper.IBrandResponseMapper;
import com.stock_service.stock.application.mapper.category_mapper.ICategoryRequestMapper;
import com.stock_service.stock.domain.api.IBrandModelServicePort;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.util.Paginated;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandHandler implements IBrandHandler{

    private final IBrandModelServicePort brandModelServicePort;
    private final IBrandRequestMapper brandRequestMapper;
    private final IBrandResponseMapper brandResponseMapper;


    @Override
    public BrandResponse saveBrand(BrandRequest brandRequest) {
        BrandModel brandModel = brandRequestMapper.brandRequestToBrandModel(brandRequest);
        BrandModel savedBrand = brandModelServicePort.saveBrand(brandModel);
        BrandResponse brandResponse = brandResponseMapper.brandModelToBrandResponse(savedBrand);
        return brandResponse;
    }

    @Override
    public Paginated<BrandResponse> getBrands(int page, int size, String sort, boolean ascending) {

        Paginated<BrandModel> brands = brandModelServicePort.getBrands(page, size, sort, ascending);

        List<BrandResponse> brandResponse = brands.getContent().stream()
                .map(brandResponseMapper::brandModelToBrandResponse)
                .toList();

        return new Paginated<>(
                brandResponse,
                brands.getPageNumber(),
                brands.getPageSize(),
                brands.getTotalPages()
        );
    }
}

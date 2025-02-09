package com.stock_service.stock.application.handler.brandhandler;

import com.stock_service.stock.application.dto.branddto.BrandRequest;
import com.stock_service.stock.application.dto.branddto.BrandResponse;
import com.stock_service.stock.application.mapper.brandmapper.IBrandRequestMapper;
import com.stock_service.stock.application.mapper.brandmapper.IBrandResponseMapper;
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

        return brandResponseMapper.brandModelToBrandResponse(savedBrand);
    }

    @Override
    public Paginated<BrandResponse> getBrandsPaginated(int page, int size, String sort, boolean ascending) {

        Paginated<BrandModel> brands = brandModelServicePort.getBrandsPaginated(page, size, sort, ascending);

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

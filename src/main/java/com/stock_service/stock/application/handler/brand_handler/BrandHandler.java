package com.stock_service.stock.application.handler.brand_handler;

import com.stock_service.stock.application.dto.brand_dto.BrandRequest;
import com.stock_service.stock.application.dto.brand_dto.BrandResponse;
import com.stock_service.stock.application.mapper.brand_mapper.IBrandRequestMapper;
import com.stock_service.stock.application.mapper.brand_mapper.IBrandResponseMapper;
import com.stock_service.stock.domain.api.IBrandModelServicePort;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.util.Paginated;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandHandler implements IBrandHandler{

    private final IBrandModelServicePort brandModelServicePort;
    private final IBrandRequestMapper brandRequestMapper;
    private final IBrandResponseMapper brandResponseMapper;

    private static final Logger logger = LoggerFactory.getLogger(BrandHandler.class);

    @Override
    public BrandResponse saveBrand(BrandRequest brandRequest) {

        logger.info("[Aplicacion] Recibiendo solicitud de creacion de marca");
        BrandModel brandModel = brandRequestMapper.brandRequestToBrandModel(brandRequest);
        BrandModel savedBrand = brandModelServicePort.saveBrand(brandModel);
        BrandResponse brandResponse = brandResponseMapper.brandModelToBrandResponse(savedBrand);

        logger.info("[Aplicacion] Respuesta mapeada a CategoryResponse");
        return brandResponse;
    }

    @Override
    public Paginated<BrandResponse> getBrandsPaginated(int page, int size, String sort, boolean ascending) {

        logger.info("[Aplicacion] Recibiendo solicitud para obtener marcas desde Controller con parametros - Pagina: {}, Tamaño: {}, Orden: {}, Ascendente: {}", page, size, sort, ascending);
        Paginated<BrandModel> brands = brandModelServicePort.getBrandsPaginated(page, size, sort, ascending);

        List<BrandResponse> brandResponse = brands.getContent().stream()
                .map(brandResponseMapper::brandModelToBrandResponse)
                .toList();

        logger.info("[Aplicacion] Se mapeo {} marcas a brandResponse", brandResponse.size());
        return new Paginated<>(
                brandResponse,
                brands.getPageNumber(),
                brands.getPageSize(),
                brands.getTotalPages()
        );
    }
}

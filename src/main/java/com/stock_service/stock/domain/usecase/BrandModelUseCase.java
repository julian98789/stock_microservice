package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.IBrandModelServicePort;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrandModelUseCase implements IBrandModelServicePort{

    private static final Logger logger = LoggerFactory.getLogger(BrandModelUseCase.class);

    private final IBrandModelPersistencePort brandModelPersistencePort;

    public BrandModelUseCase(IBrandModelPersistencePort brandModelPersistencePort) {
        this.brandModelPersistencePort = brandModelPersistencePort;
    }

    @Override
    public BrandModel saveBrand(BrandModel brandModel) {

        logger.info("[Dominio] Recibiendo solicitud para guardar la marcar con nombre: {}", brandModel.getName());
        if(brandModelPersistencePort.existsByName(brandModel.getName())){

            logger.warn("[Dominio] El nombre de la marca '{}' ya existe. Lanzando excepcion NameAlreadyExistsException", brandModel.getName());
            throw new NameAlreadyExistsException(Util.BRAND_NAME_ALREADY_EXISTS);
        }
        BrandModel savedBrand = brandModelPersistencePort.saveBrand(brandModel);

        logger.info("[Dominio] Marca guardada exitosamente con id: {} y nombre: {}", savedBrand.getId(), savedBrand.getName());
        return savedBrand;
    }


    @Override
    public Paginated<BrandModel> getBrandsPaginated(int page, int size, String sort, boolean ascending) {

        logger.info("[Dominio] Recibiendo solicitud para obtener marca con los siguientes parametros: pagina = {}, tamano = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        Paginated<BrandModel> brands = brandModelPersistencePort.getBrandsPaginated(page, size, sort, ascending);

        logger.info("[Dominio] Se obtuvieron {} marcas en la pagina {}", brands.getContent().size(), page);
        return brands;
    }
}

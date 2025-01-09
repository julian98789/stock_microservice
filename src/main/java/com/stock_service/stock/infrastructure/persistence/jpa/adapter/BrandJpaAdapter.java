package com.stock_service.stock.infrastructure.persistence.jpa.adapter;

import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.persistence.jpa.entity.BrandEntity;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.IBrandEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.IBrandRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;


@RequiredArgsConstructor
public class BrandJpaAdapter implements IBrandModelPersistencePort {

     private final IBrandRepository brandRepository;
     private final IBrandEntityMapper brandEntityMapper;

    private static final Logger logger = LoggerFactory.getLogger(BrandJpaAdapter.class);

    @Override
    public BrandModel saveBrand(BrandModel brandModel) {

        logger.info("[Infraestructura] Recibiendo solicitud para guardar la marca con nombre: {}", brandModel.getName());
        BrandEntity brandEntity = brandEntityMapper.brandModelToBrandEntity(brandModel);
        brandEntity = brandRepository.save(brandEntity);
        BrandModel savedBrand = brandEntityMapper.brandEntityToBrandModel(brandEntity);

        logger.info("[Infraestructura] Mapeo de entidad a modelo completado, marca retornada con id: {}", savedBrand.getId());
        return savedBrand;
    }

    @Override
    public boolean existsByName(String name) {

        logger.info("[Infraestructura] Recibiendo solicitud para verificar existencia de marca con nombre: {}", name);
        boolean exists = brandRepository.findByName(name).isPresent();

        logger.info("[Infraestructura] No se encontro marca con el nombre {}", name);
        return exists;

    }

    @Override
    public Paginated<BrandModel> getBrands(int page, int size, String sort, boolean ascending) {

        logger.info("[Infraestructura] Recibiendo solicitud para obtener marcas con los siguientes parametros: pagina = {}, tamano = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        PageRequest pageRequest = PageRequest.of(page, size, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sort);

        Page<BrandEntity> brandEntities = brandRepository.findAll(pageRequest);


        List<BrandModel> brandModels = brandEntities.stream()
                .map(brandEntityMapper::brandEntityToBrandModel)
                .toList();

        logger.info("[Infraestructura] Se han mapeado {} marcas desde entidad a modelo", brandModels.size());
        return new Paginated<>(
                brandModels,
                brandEntities.getNumber(),
                brandEntities.getSize(),
                brandEntities.getTotalElements()
        );
    }

    @Override
    public BrandModel getBrandById(Long id) {

        logger.info("[Infraestructura] Recibiendo solicitud para recuperar la marca con el id: {}", id);
        BrandEntity brandEntity = brandRepository.findById(id).orElse(null);

        BrandModel brandModel = brandEntityMapper.brandEntityToBrandModel(brandEntity);

        logger.info("[Infraestructura] Marca recuperada con id: {}", brandModel.getId());
        return brandModel;
    }
}

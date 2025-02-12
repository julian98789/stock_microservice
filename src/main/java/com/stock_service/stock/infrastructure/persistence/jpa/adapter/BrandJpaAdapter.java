package com.stock_service.stock.infrastructure.persistence.jpa.adapter;

import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.domain.spi.IBrandModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.persistence.jpa.entity.BrandEntity;
import com.stock_service.stock.infrastructure.persistence.jpa.mapper.IBrandEntityMapper;
import com.stock_service.stock.infrastructure.persistence.jpa.repository.IBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;


@RequiredArgsConstructor
public class BrandJpaAdapter implements IBrandModelPersistencePort {

     private final IBrandRepository brandRepository;
     private final IBrandEntityMapper brandEntityMapper;


    @Override
    public BrandModel saveBrand(BrandModel brandModel) {

        BrandEntity brandEntity = brandEntityMapper.brandModelToBrandEntity(brandModel);
        brandEntity = brandRepository.save(brandEntity);

        return brandEntityMapper.brandEntityToBrandModel(brandEntity);
    }

    @Override
    public boolean existsByName(String name) {

        return brandRepository.findByName(name).isPresent();

    }

    @Override
    public Paginated<BrandModel> getBrandsPaginated(int page, int size, String sort, boolean ascending) {

        PageRequest pageRequest = PageRequest.of(page, size, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sort);

        Page<BrandEntity> brandEntities = brandRepository.findAll(pageRequest);


        List<BrandModel> brandModels = brandEntities.stream()
                .map(brandEntityMapper::brandEntityToBrandModel)
                .toList();

        return new Paginated<>(
                brandModels,
                brandEntities.getNumber(),
                brandEntities.getSize(),
                brandEntities.getTotalElements()
        );
    }

    @Override
    public BrandModel getBrandById(Long id) {

        BrandEntity brandEntity = brandRepository.findById(id).orElse(null);

        return brandEntityMapper.brandEntityToBrandModel(brandEntity);
    }
}

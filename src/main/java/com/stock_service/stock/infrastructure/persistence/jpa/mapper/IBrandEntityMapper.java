package com.stock_service.stock.infrastructure.persistence.jpa.mapper;

import com.stock_service.stock.domain.model.BrandModel;
import com.stock_service.stock.infrastructure.persistence.jpa.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBrandEntityMapper {

    BrandEntity brandModelToBrandEntity(BrandModel brandModel);
    BrandModel brandEntityToBrandModel(BrandEntity brandEntity);
}

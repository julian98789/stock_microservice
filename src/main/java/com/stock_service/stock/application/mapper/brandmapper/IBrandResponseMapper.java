package com.stock_service.stock.application.mapper.brandmapper;

import com.stock_service.stock.application.dto.branddto.BrandResponse;
import com.stock_service.stock.domain.model.BrandModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface IBrandResponseMapper {
    BrandResponse brandModelToBrandResponse(BrandModel brandModel);
}

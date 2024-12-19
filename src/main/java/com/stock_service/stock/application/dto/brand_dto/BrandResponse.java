package com.stock_service.stock.application.dto.brand_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandResponse {
    Long id;
    private String name;
    private String description;
}

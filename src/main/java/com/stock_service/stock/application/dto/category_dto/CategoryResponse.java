package com.stock_service.stock.application.dto.category_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    Long id;
    private String name;
    private String description;
}

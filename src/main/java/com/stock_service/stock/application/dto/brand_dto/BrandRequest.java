package com.stock_service.stock.application.dto.brand_dto;

import com.stock_service.stock.domain.util.Util;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BrandRequest {
    @NotBlank(message = Util.NAME_REQUIRED)
    @Size(min = Util.NAME_MIN_VALUE, max = Util.NAME_MAX_VALUE, message = Util.NAME_SIZE)
    private String name;

    @NotBlank(message = Util.DESCRIPTION_REQUIRED)
    @Size(min =  Util.DESCRIPTION_MIN_VALUE, max = Util.DESCRIPTION_CATEGORY_MAX_VALUE, message = Util.DESCRIPTION_CATEGORY_SIZE)
    private String description;
}

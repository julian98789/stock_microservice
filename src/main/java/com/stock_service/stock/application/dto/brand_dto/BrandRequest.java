package com.stock_service.stock.application.dto.brand_dto;

import com.stock_service.stock.domain.util.UtilMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BrandRequest {
    @NotBlank(message = UtilMessage.NAME_REQUIRED)
    @Size(min = UtilMessage.NAME_MIN_VALUE, max = UtilMessage.NAME_MAX_VALUE, message = UtilMessage.NAME_SIZE)
    private String name;

    @NotBlank(message = UtilMessage.DESCRIPTION_REQUIRED)
    @Size(min =  UtilMessage.DESCRIPTION_MIN_VALUE, max = UtilMessage.DESCRIPTION_CATEGORY_MAX_VALUE, message = UtilMessage.DESCRIPTION_CATEGORY_SIZE)
    private String description;
}

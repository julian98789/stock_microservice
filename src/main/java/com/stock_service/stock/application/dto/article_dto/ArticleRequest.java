package com.stock_service.stock.application.dto.article_dto;

import com.stock_service.stock.domain.util.Util;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;


@Getter
@Setter
public class ArticleRequest {

    @NotBlank(message = Util.NAME_REQUIRED)
    @Size(min = Util.NAME_MIN_VALUE, max = Util.NAME_MAX_VALUE, message = Util.NAME_SIZE)
    private String name;

    @NotBlank(message = Util.DESCRIPTION_REQUIRED)
    @Size(min = Util.DESCRIPTION_MIN_VALUE, max = Util.DESCRIPTION_ARTICLE_MAX_VALUE, message = Util.DESCRIPTION_ARTICLE_SIZE)
    private String description;

    @NotNull(message = Util.ARTICLE_QUANTITY_REQUIRED)
    @Min(value = Util.ARTICLE_QUANTITY_MIN_VALUE, message = Util.ARTICLE_QUANTITY_MIN)
    private int quantity;

    @NotNull(message = Util.ARTICLE_PRICE_REQUIRED)
    @DecimalMin(value = Util.ARTICLE_PRICE_MIN_VALUE, inclusive = false, message = Util.ARTICLE_PRICE_MIN)
    private double price;

    @NotNull(message = Util.ARTICLE_BRAND_ID_REQUIRED)
    private Long brandId;

    @UniqueElements( message = Util.ARTICLE_CATEGORIES_UNIQUE)
    @NotEmpty(message = Util.ARTICLE_CATEGORIES_REQUIRED)
    @Size(min = Util.ARTICLE_CATEGORIES_MIN_VALUE,max = Util.ARTICLE_CATEGORIES_MAX_VALUE, message = Util.ARTICLE_CATEGORIES_SIZE)
    private List<Long> categoryIds;
}

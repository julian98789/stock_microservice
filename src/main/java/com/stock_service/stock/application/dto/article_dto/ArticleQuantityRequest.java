package com.stock_service.stock.application.dto.article_dto;

import com.stock_service.stock.domain.util.Util;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleQuantityRequest {

    @NotNull(message = Util.ARTICLE_QUANTITY_REQUIRED)
    @Min(value = Util.ARTICLE_QUANTITY_MIN_VALUE, message = Util.ARTICLE_QUANTITY_MIN)
    private Integer quantity;
}

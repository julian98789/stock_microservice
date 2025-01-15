package com.stock_service.stock.application.dto.article_dto;

import com.stock_service.stock.application.dto.brand_dto.BrandResponse;
import com.stock_service.stock.application.dto.category_dto.CategoryResponseForArticle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleResponse {

    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private double price;
    private BrandResponse brand;
    private List<CategoryResponseForArticle> categories;
}

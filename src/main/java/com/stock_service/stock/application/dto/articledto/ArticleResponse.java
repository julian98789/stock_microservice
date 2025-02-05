package com.stock_service.stock.application.dto.articledto;

import com.stock_service.stock.application.dto.branddto.BrandResponse;
import com.stock_service.stock.application.dto.categorydto.CategoryResponseForArticle;
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

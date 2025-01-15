package com.stock_service.stock.infrastructure.persistence.jpa.specifications;

import com.stock_service.stock.infrastructure.persistence.jpa.entity.ArticleEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ArticleSpecifications {
    private ArticleSpecifications() {}

    public static Specification<ArticleEntity> inArticleIds(List<Long> ids) {
        return (root, query, criteriaBuilder) -> ids == null || ids.isEmpty()
                ? criteriaBuilder.conjunction()
                : root.get("id").in(ids);
    }

    public static Specification<ArticleEntity> byCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) -> categoryName == null || categoryName.isEmpty()
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.join("categories").get("name"), categoryName);
    }

    public static Specification<ArticleEntity> byBrandName(String brandName) {
        return (root, query, criteriaBuilder) -> brandName == null || brandName.isEmpty()
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.join("brand").get("name"), brandName);
    }

}
package com.stock_service.stock.infrastructure.output.repository;

import com.stock_service.stock.infrastructure.output.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface IArticleRepository extends JpaRepository<ArticleEntity, Long> {
    Optional<ArticleEntity> findByName(String aString);
    Page<ArticleEntity> findAll(Pageable pageable);

}

package com.stock_service.stock.infrastructure.persistence.jpa.repository;

import com.stock_service.stock.infrastructure.persistence.jpa.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IArticleRepository extends JpaRepository<ArticleEntity, Long>, JpaSpecificationExecutor<ArticleEntity> {
    Optional<ArticleEntity> findByName(String aString);

    Page<ArticleEntity> findAll(Pageable pageable);
}
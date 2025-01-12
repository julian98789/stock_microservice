package com.stock_service.stock.infrastructure.persistence.jpa.repository;

import com.stock_service.stock.infrastructure.persistence.jpa.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String aString);
    Page<CategoryEntity> findAll(Pageable pageable);


}

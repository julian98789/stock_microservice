package com.stock_service.stock.infrastructure.output.repository;

import com.stock_service.stock.infrastructure.output.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String aString);
}

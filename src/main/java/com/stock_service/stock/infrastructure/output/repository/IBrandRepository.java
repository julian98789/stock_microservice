package com.stock_service.stock.infrastructure.output.repository;

import com.stock_service.stock.infrastructure.output.entity.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface IBrandRepository extends JpaRepository<BrandEntity,Long> {

    Optional<BrandEntity> findByName(String aString);
    Page<BrandEntity> findAll(Pageable pageable);
}

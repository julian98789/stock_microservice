package com.stock_service.stock.infrastructure.input.controller;

import com.stock_service.stock.application.dto.brand_dto.BrandRequest;
import com.stock_service.stock.application.dto.brand_dto.BrandResponse;

import com.stock_service.stock.application.handler.brand_handler.BrandHandler;
import com.stock_service.stock.domain.util.Paginated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandRestController {

    private final BrandHandler brandHandler;

    private static final Logger logger = LoggerFactory.getLogger(BrandRestController.class);
    @PostMapping("/crear")
    public ResponseEntity<BrandResponse> saveBrand(@Valid @RequestBody BrandRequest brandRequest) {

        logger.info("[Infraestructura] Recibiendo solicitud para crear marca ");
        BrandResponse savedBrand = brandHandler.saveBrand(brandRequest);

        logger.info("[Infraestructura] Marca creada exitosamente con nombre: {}", savedBrand.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
    }

    @GetMapping("/listar")
    public ResponseEntity<Paginated<BrandResponse>> getBrand(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "name") @NotBlank @Size(min = 1) String sort,
            @RequestParam(defaultValue = "true") boolean ascending) {

        logger.info("[Infraestructura] Recibiendo solicitud para obtener marcas con los siguientes parametros: pagina = {}, tamano = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        Paginated<BrandResponse> paginatedResult = brandHandler.getBrands(page, size, sort, ascending);

        logger.info("[Infraestructura] Se obtuvieron {} marcas en la pagina {}", paginatedResult.getContent().size(), page);
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }
}

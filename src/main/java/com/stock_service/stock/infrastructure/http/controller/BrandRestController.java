package com.stock_service.stock.infrastructure.http.controller;

import com.stock_service.stock.application.dto.brand_dto.BrandRequest;
import com.stock_service.stock.application.dto.brand_dto.BrandResponse;
import com.stock_service.stock.application.handler.brand_handler.BrandHandler;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandRestController {

    private final BrandHandler brandHandler;
    private static final Logger logger = LoggerFactory.getLogger(BrandRestController.class);

    @Operation(
            summary = "Crear una nueva marca",
            description = "Este endpoint permite crear una nueva marca en el sistema enviando los datos necesarios en el cuerpo de la solicitud.",
            tags = {"Brand"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Marca creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BrandResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Error en la validación de datos.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_ADMIN )
    @PostMapping("/crear")
    public ResponseEntity<BrandResponse> saveBrand(@Valid @RequestBody BrandRequest brandRequest) {

        logger.info("[Infraestructura] Recibiendo solicitud para crear marca ");
        BrandResponse savedBrand = brandHandler.saveBrand(brandRequest);

        logger.info("[Infraestructura] Marca creada exitosamente con nombre: {}", savedBrand.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
    }

    @Operation(
            summary = "Obtener marcas paginadas",
            description = "Este endpoint permite obtener una lista paginada de marcas, con opciones de ordenación y paginación.",
            tags = {"Brand"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marcas obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paginated.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Error en la validación de datos.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
                    content = @Content(mediaType = "application/json"))
    })

    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENTE + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/listar")
    public ResponseEntity<Paginated<BrandResponse>> getBrand(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "name") @NotBlank @Size(min = 1) String sort,
            @RequestParam(defaultValue = "true") boolean ascending) {

        logger.info("[Infraestructura] Recibiendo solicitud para obtener marcas con los siguientes parametros: pagina = {}, tamano = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        Paginated<BrandResponse> paginatedResult = brandHandler.getBrandsPaginated(page, size, sort, ascending);

        logger.info("[Infraestructura] Se obtuvieron {} marcas en la pagina {}", paginatedResult.getContent().size(), page);
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }
}
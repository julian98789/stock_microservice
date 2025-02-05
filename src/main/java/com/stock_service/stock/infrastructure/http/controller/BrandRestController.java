package com.stock_service.stock.infrastructure.http.controller;

import com.stock_service.stock.application.dto.branddto.BrandRequest;
import com.stock_service.stock.application.dto.branddto.BrandResponse;
import com.stock_service.stock.application.handler.brandhandler.BrandHandler;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandRestController {

    private final BrandHandler brandHandler;

    @Operation(
            summary = "Create a new brand",
            description = "This endpoint allows creating a new brand in the system by sending the necessary data in the request body.",
            tags = {"Brand"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Brand created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BrandResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request. Data validation error.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_ADMIN)
    @PostMapping("/crear")
    public ResponseEntity<BrandResponse> saveBrand(@Valid @RequestBody BrandRequest brandRequest) {
        BrandResponse savedBrand = brandHandler.saveBrand(brandRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
    }

    @Operation(
            summary = "Get paginated brands",
            description = "This endpoint allows obtaining a paginated list of brands, with sorting and pagination options.",
            tags = {"Brand"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brands retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paginated.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request. Data validation error.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENT + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/listar")
    public ResponseEntity<Paginated<BrandResponse>> getBrand(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "name") @NotBlank @Size(min = 1) String sort,
            @RequestParam(defaultValue = "true") boolean ascending) {

        Paginated<BrandResponse> paginatedResult = brandHandler.getBrandsPaginated(page, size, sort, ascending);
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }
}
package com.stock_service.stock.infrastructure.input.controller;

import com.stock_service.stock.application.dto.category_dto.CategoryRequest;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.application.handler.category_handler.CategoryHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryHandler categoryHandler;


    @Operation(
            summary = "Crear una nueva categoría",
            description = "Este endpoint permite crear una nueva categoría en el sistema enviando los datos necesarios en el cuerpo de la solicitud.",
            tags = {"Category"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Error en la validación de datos.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse savedCategory = categoryHandler.saveCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }
}

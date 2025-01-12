package com.stock_service.stock.infrastructure.http.controller;

import com.stock_service.stock.application.dto.category_dto.CategoryRequest;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.application.handler.category_handler.CategoryHandler;
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

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);

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

    @PreAuthorize(Util.ROLE_ADMIN)
    @PostMapping("/crear")
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest) {

        logger.info("[Infraestructura] Recibiendo solicitud para crear categoria ");
        CategoryResponse savedCategory = categoryHandler.saveCategory(categoryRequest);

        logger.info("[Infraestructura] Categoria creada exitosamente con nombre: {}", savedCategory.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @Operation(
            summary = "Obtener categorías paginadas",
            description = "Este endpoint permite obtener una lista paginada de categorías, con opciones de ordenación y paginación.",
            tags = {"Category"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paginated.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Error en la validación de datos.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENTE + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/listar")
    public ResponseEntity<Paginated<CategoryResponse>> getCategorie(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "name") @NotBlank @Size(min = 1) String sort,
            @RequestParam(defaultValue = "true") boolean ascending) {

        logger.info("[Infraestructura] Recibiendo solicitud para obtener categorias con los siguientes parametros: pagina = {}, tamano = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        Paginated<CategoryResponse> paginatedResult = categoryHandler.getCategories(page, size, sort, ascending);

        logger.info("[Infraestructura] Se obtuvieron {} categorias en la pagina {}", paginatedResult.getContent().size(), page);
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }

    @GetMapping("/names-by-article/{articleId}")
    public ResponseEntity<List<String>> getCategoryNamesByArticleId(@PathVariable Long articleId) {
        List<String> categoryNames = categoryHandler.getCategoryNamesByArticleId(articleId);
        return new ResponseEntity<>(categoryNames, HttpStatus.OK);
    }
}

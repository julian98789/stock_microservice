package com.stock_service.stock.infrastructure.input.controller;

import com.stock_service.stock.application.dto.article_dto.ArticleRequest;
import com.stock_service.stock.application.dto.article_dto.ArticleResponse;
import com.stock_service.stock.application.handler.article_handler.ArticleHandler;
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
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleHandler articleHandler;
    private static final Logger logger = LoggerFactory.getLogger(ArticleRestController.class);

    @Operation(
            summary = "Crear un nuevo artículo",
            description = "Este endpoint permite crear un nuevo artículo en el sistema enviando los datos necesarios en el cuerpo de la solicitud.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artículo creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Error en la validación de datos.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
                    content = @Content(mediaType = "application/json"))
    })

    @PreAuthorize(Util.ROLE_ADMIN )
    @PostMapping("/crear")
    public ResponseEntity<ArticleResponse> saveArticle(@Valid @RequestBody ArticleRequest articleRequest) {

        logger.info("[Infraestructura] Recibiendo solicitud para crear artículo ");
        ArticleResponse savedArticle = articleHandler.saveArticle(articleRequest);

        logger.info("[Infraestructura] Artículo creado exitosamente con nombre: {}", savedArticle.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @Operation(
            summary = "Obtener artículos paginados",
            description = "Este endpoint permite obtener una lista paginada de artículos, con opciones de ordenación y paginación.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículos obtenidos exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paginated.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Error en la validación de datos.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
                    content = @Content(mediaType = "application/json"))
    })

    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENTE + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/listar")
    public ResponseEntity<Paginated<ArticleResponse>> getArticles(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "name") @NotBlank @Size(min = 1) String sort,
            @RequestParam(defaultValue = "true") boolean ascending) {

        logger.info("[Infraestructura] Recibiendo solicitud para obtener artículos con los siguientes parámetros: página = {}, tamaño = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        Paginated<ArticleResponse> paginatedResult = articleHandler.getArticles(page, size, sort, ascending);

        logger.info("[Infraestructura] Se obtuvieron {} artículos en la página {}", paginatedResult.getContent().size(), page);
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener un artículo por ID",
            description = "Este endpoint permite obtener un artículo específico por su ID.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENTE + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id) {
        logger.info("[Infraestructura] Recibiendo solicitud para obtener artículo con ID: {}", id);
        ArticleResponse articleResponse = articleHandler.getArticleById(id);
        logger.info("[Infraestructura] Artículo encontrado con ID: {}", id);
        return new ResponseEntity<>(articleResponse, HttpStatus.OK);
    }

}
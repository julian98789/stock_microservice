package com.stock_service.stock.infrastructure.http.controller;

import com.stock_service.stock.application.dto.categorydto.CategoryRequest;
import com.stock_service.stock.application.dto.categorydto.CategoryResponse;
import com.stock_service.stock.application.handler.categoryhandler.CategoryHandler;
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

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryHandler categoryHandler;

    @Operation(
            summary = "Create a new category",
            description = "This endpoint allows creating a new category in the system by sending the necessary data in the request body.",
            tags = {"Category"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request. Data validation error.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json"))
    })

    @PreAuthorize(Util.ROLE_ADMIN)
    @PostMapping("/crear")
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse savedCategory = categoryHandler.saveCategory(categoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @Operation(
            summary = "Get paginated categories",
            description = "This endpoint allows obtaining a paginated list of categories, " +
                    "with sorting and pagination options.",
            tags = {"Category"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paginated.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request. Data validation error.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json"))
    })

    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENT + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/listar")
    public ResponseEntity<Paginated<CategoryResponse>> getCategories(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "name") @NotBlank @Size(min = 1) String sort,
            @RequestParam(defaultValue = "true") boolean ascending) {

        Paginated<CategoryResponse> paginatedResult = categoryHandler.getCategories(page, size, sort, ascending);

        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }

    @Operation(
            summary = "Get category names by article ID",
            description = "This endpoint allows obtaining a list of category names " +
                    "associated with a specific article by its ID.",
            tags = {"Category"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category names retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request. Data validation error.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json"))
    })

    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENT + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/names-by-article/{articleId}")
    public ResponseEntity<List<String>> getCategoryNamesByArticleId(@PathVariable Long articleId) {
        List<String> categoryNames = categoryHandler.getCategoryNamesByArticleId(articleId);
        return new ResponseEntity<>(categoryNames, HttpStatus.OK);
    }
}
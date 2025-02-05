package com.stock_service.stock.infrastructure.http.controller;

import com.stock_service.stock.application.dto.articledto.ArticleQuantityRequest;
import com.stock_service.stock.application.dto.articledto.ArticleRequest;
import com.stock_service.stock.application.dto.articledto.ArticleResponse;
import com.stock_service.stock.application.dto.articledto.ArticleCartRequest;
import com.stock_service.stock.application.handler.articlehandler.IArticleHandler;
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
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleRestController {

    private final IArticleHandler articleHandler;

    @Operation(
            summary = "Create a new article",
            description = "This endpoint allows creating a new article in the system by sending " +
                    "the necessary data in the request body.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request. Data validation error.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_ADMIN)
    @PostMapping("/crear")
    public ResponseEntity<ArticleResponse> saveArticle(@Valid @RequestBody ArticleRequest articleRequest) {
        ArticleResponse savedArticle = articleHandler.saveArticle(articleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @Operation(
            summary = "Get paginated articles",
            description = "This endpoint allows obtaining a paginated list of articles," +
                    " with sorting and pagination options.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paginated.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request. Data validation error.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENT + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/listar")
    public ResponseEntity<Paginated<ArticleResponse>> getArticles(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "name") @NotBlank @Size(min = 1) String sort,
            @RequestParam(defaultValue = "true") boolean ascending) {
        Paginated<ArticleResponse> paginatedResult = articleHandler.getArticlesPaginated(page, size, sort, ascending);
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }

    @Operation(
            summary = "Get article by ID",
            description = "This endpoint allows obtaining an article by its ID.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENT + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/{articleId}")
    public ResponseEntity<Boolean> getArticleById(@PathVariable Long articleId) {
        boolean article = articleHandler.getArticleById(articleId);
        return ResponseEntity.ok(article);
    }

    @Operation(
            summary = "Update article quantity",
            description = "This endpoint allows updating the quantity of an article.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article quantity updated successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_AUX_BODEGA)
    @PatchMapping("/quantity/{articleId}")
    public void updateArticleQuantity(
            @PathVariable Long articleId,
            @RequestBody ArticleQuantityRequest articleQuantityRequest) {
        articleHandler.updateArticleQuantity(articleId, articleQuantityRequest);
    }

    @Operation(
            summary = "Check stock availability",
            description = "This endpoint allows checking if there is sufficient stock of an article.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock checked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_CLIENT)
    @GetMapping("/{articleId}/check-quantity/{quantity}")
    public ResponseEntity<Boolean> isStockSufficient(
            @PathVariable Long articleId,
            @PathVariable Integer quantity) {
        boolean isAvailable = articleHandler.checkAvailabilityArticle(articleId, quantity);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(
            summary = "Reduce article quantity",
            description = "This endpoint allows reducing the quantity of an article in stock.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article quantity reduced successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_CLIENT)
    @PatchMapping("/{articleId}/subtract-stock")
    public void reduceArticleQuantity(@PathVariable Long articleId,
                                      @RequestBody @Valid ArticleQuantityRequest request) {
        articleHandler.reduceStock(articleId, request);
    }

    @Operation(
            summary = "Get article price by ID",
            description = "This endpoint allows obtaining the price of an article by its ID.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article price retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_CLIENT)
    @GetMapping("/{articleId}/price")
    public ResponseEntity<Double> getArticlePriceById(@PathVariable Long articleId) {
        Double price = articleHandler.getArtclePriceById(articleId);
        return ResponseEntity.ok(price);
    }

    @Operation(
            summary = "Get all paginated articles by IDs",
            description = "This endpoint allows obtaining a paginated list of articles by their IDs, " +
                    "with sorting and pagination options.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paginated.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request. Data validation error.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_ADMIN + " or " + Util.ROLE_CLIENT + " or " + Util.ROLE_AUX_BODEGA)
    @GetMapping("/article-cart")
    public ResponseEntity<Paginated<ArticleResponse>> getAllArticlesPaginatedByIds(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "name") @NotBlank @Size(min = 1) String sort,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String brandName,
            @RequestBody @Valid ArticleCartRequest articleCartRequest) {
        Paginated<ArticleResponse> paginatedResult = articleHandler.getAllArticlesPaginatedByIds(
                page, size, sort, ascending, categoryName, brandName, articleCartRequest.getArticleIds());
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all articles by IDs",
            description = "This endpoint allows obtaining a list of all articles by their IDs.",
            tags = {"Article"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request. Data validation error.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(Util.ROLE_CLIENT)
    @GetMapping("/get-all-articles")
    public ResponseEntity<List<ArticleResponse>> getAllArticles(@RequestBody ArticleCartRequest articleCartRequest) {
        List<ArticleResponse> articleResponses = articleHandler.getAllArticlesByIds(articleCartRequest.getArticleIds());
        return ResponseEntity.ok(articleResponses);
    }
}
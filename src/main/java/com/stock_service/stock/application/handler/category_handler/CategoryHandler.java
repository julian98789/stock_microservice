package com.stock_service.stock.application.handler.category_handler;

import com.stock_service.stock.application.dto.category_dto.CategoryRequest;
import com.stock_service.stock.application.dto.category_dto.CategoryResponse;
import com.stock_service.stock.application.mapper.category_mapper.ICategoryRequestMapper;
import com.stock_service.stock.application.mapper.category_mapper.ICategoryResponseMapper;
import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.util.Paginated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryHandler implements ICategoryHandler {
    private final ICategoryModelServicePort categoryModelServicePort;
    private final ICategoryRequestMapper categoryRequestMapper;
    private final ICategoryResponseMapper categoryResponseMapper;

    private static final Logger logger = LoggerFactory.getLogger(CategoryHandler.class);

    @Override
    public CategoryResponse saveCategory(CategoryRequest categoryRequest) {
        logger.info("[Aplicacion] Recibiendo solicitud de creación de categoria desde Controller con nombre: {}", categoryRequest.getName());
        CategoryModel categoryModel = categoryRequestMapper.categoryrequestToCategoryModel(categoryRequest);

        logger.info("[Aplicacion] Mapeo de CategoryRequest a CategoryModel con nombre: {}", categoryModel.getName());
        CategoryModel savedCategory = categoryModelServicePort.saveCategory(categoryModel);

        logger.info("[Aplicacion] Categoria guardada exitosamente con ID: {} y nombre: {}", savedCategory.getId(), savedCategory.getName());
        CategoryResponse categoryResponse = categoryResponseMapper.categoryModelToCategoryResponse(savedCategory);

        logger.info("[Aplicacion] Respuesta mapeada a CategoryResponse con ID: {} y nombre: {}", categoryResponse.getId(), categoryResponse.getName());
        return categoryResponse;
    }

    @Override
    public Paginated<CategoryResponse> getCategories(int page, int size, String sort, boolean ascending) {
        logger.info("[Aplicacion] Recibiendo solicitud para obtener categorias desde Controller con parámetros - Página: {}, Tamaño: {}, Orden: {}, Ascendente: {}", page, size, sort, ascending);
        Paginated<CategoryModel> categories = categoryModelServicePort.getCategories(page, size, sort, ascending);

        logger.info("[Aplicacion] Se obtuvieron {} categorias en la página: {}", categories.getContent().size(), page);
        List<CategoryResponse> categoryResponse = categories.getContent().stream()
                .map(categoryResponseMapper::categoryModelToCategoryResponse)
                .toList();

        logger.info("[Aplicacion] Se mapeó {} categorias a CategoryResponse", categoryResponse.size());
        return new Paginated<>(
                categoryResponse,
                categories.getPageNumber(),
                categories.getPageSize(),
                categories.getTotalPages()
        );
    }
}

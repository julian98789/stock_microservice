package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryModelUseCase implements ICategoryModelServicePort {

    private static final Logger logger = LoggerFactory.getLogger(CategoryModelUseCase.class);

    private final ICategoryModelPersistencePort categoryModelPersistencePort;

    public CategoryModelUseCase(ICategoryModelPersistencePort categoryModelPersistencePort) {
        this.categoryModelPersistencePort = categoryModelPersistencePort;
    }

    @Override
    public CategoryModel saveCategory(CategoryModel categoryModel) {
        logger.info("[Dominio] Recibiendo solicitud para guardar la categoria con nombre: {}", categoryModel.getName());

        if (categoryModelPersistencePort.existByName(categoryModel.getName())) {
            logger.warn("[Dominio] El nombre de la categoria '{}' ya existe. Lanzando excepcion NameAlreadyExistsException", categoryModel.getName());
            throw new NameAlreadyExistsException(Util.CATEGORY_NAME_ALREADY_EXISTS);
        }

        CategoryModel savedCategory = categoryModelPersistencePort.saveCategory(categoryModel);

        logger.info("[Dominio] Categoria guardada exitosamente con id: {} y nombre: {}", savedCategory.getId(), savedCategory.getName());
        return savedCategory;
    }


    @Override
    public Paginated<CategoryModel> getCategories(int page, int size, String sort, boolean ascending) {

        logger.info("[Dominio] Recibiendo solicitud para obtener categorias con los siguientes parametros: pagina = {}, tamano = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        Paginated<CategoryModel> categories = categoryModelPersistencePort.getCategories(page, size, sort, ascending);

        logger.info("[Dominio] Se obtuvieron {} categorias en la pagina {}", categories.getContent().size(), page);
        return categories;
    }
}

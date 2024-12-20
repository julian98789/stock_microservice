package com.stock_service.stock.infrastructure.output.adapter;

import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.infrastructure.output.entity.CategoryEntity;
import com.stock_service.stock.infrastructure.output.mapper.ICategoryEntityMapper;
import com.stock_service.stock.infrastructure.output.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryModelPersistencePort {

    private static final Logger logger = LoggerFactory.getLogger(CategoryJpaAdapter.class);

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public CategoryModel saveCategory(CategoryModel categoryModel) {

        logger.info("[Infraestructura] Recibiendo solicitud para guardar la categoria con nombre: {}", categoryModel.getName());

        CategoryEntity categoryEntity = categoryEntityMapper.categoryModelToCategoryEntity(categoryModel);

        categoryEntity = categoryRepository.save(categoryEntity);

        CategoryModel savedCategoryModel = categoryEntityMapper.categoryEntityToCategoryModel(categoryEntity);

        logger.info("[Infraestructura] Mapeo de entidad a modelo completado, categoria retornada con id: {}", savedCategoryModel.getId());
        return savedCategoryModel;
    }

    @Override
    public boolean existByName(String name) {
        logger.info("[Infraestructura] Recibiendo solicitud para verificar existencia de categoria con nombre: {}", name);

        boolean exists = categoryRepository.findByName(name).isPresent();

        logger.info("[Infraestructura] No se encontro categoria con el nombre {}", name);
        return exists;
    }

    @Override
    public Paginated<CategoryModel> getCategories(int page, int size, String sort, boolean ascending) {

        logger.info("[Infraestructura] Recibiendo solicitud para obtener categorias con los siguientes parametros: pagina = {}, tamano = {}, orden = {}, ascendente = {}", page, size, sort, ascending);
        PageRequest pageRequest = PageRequest.of(page, size, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sort);

        Page<CategoryEntity> categoryEntities = categoryRepository.findAll(pageRequest);

        List<CategoryModel> categoryModels = categoryEntities.stream()
                .map(categoryEntityMapper::categoryEntityToCategoryModel)
                .toList();

        logger.info("[Infraestructura] Se han mapeado {} categorias desde entidad a modelo", categoryModels.size());
        return new Paginated<>(
                categoryModels,
                categoryEntities.getNumber(),
                categoryEntities.getSize(),
                categoryEntities.getTotalPages()
        );
    }

    @Override
    public List<CategoryModel> getCategoriesByIds(List<Long> ids) {

        logger.info("[Infraestructura] Recibiendo solicitud para recuperar categorias con los siguientes ids: {}", ids);
        List<CategoryEntity> categoryEntities = categoryRepository.findAllById(ids);

        List<CategoryModel> categoryModels = categoryEntities.stream()
                .map(categoryEntityMapper::categoryEntityToCategoryModel)
                .toList();

        logger.info("[Infraestructura] Se han mapeado {} entidades a modelos de categoria", categoryModels.size());
        return categoryModels;
    }
}

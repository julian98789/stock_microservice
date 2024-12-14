package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.util.Message;


public class CategoryModelUseCase implements ICategoryModelServicePort {

    private final ICategoryModelPersistencePort categoryModelPersistencePort;

    public CategoryModelUseCase(ICategoryModelPersistencePort categoryModelPersistencePort) {
        this.categoryModelPersistencePort = categoryModelPersistencePort;
    }

    @Override
    public CategoryModel saveCategory(CategoryModel categoryModel) {
       if (categoryModelPersistencePort.existByName(categoryModel.getName())) {
           throw new NameAlreadyExistsException(Message.CATEGORY_NAME_ALREADY_EXISTS);
       }
         return categoryModelPersistencePort.saveCategory(categoryModel);
    }

    @Override
    public boolean existByName(String name) {
        return categoryModelPersistencePort.existByName(name);
    }
}

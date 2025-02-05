package com.stock_service.stock.domain.usecase;

import com.stock_service.stock.domain.api.IArticleModelServicePort;
import com.stock_service.stock.domain.api.ICategoryModelServicePort;
import com.stock_service.stock.domain.exception.NameAlreadyExistsException;
import com.stock_service.stock.domain.exception.NotFoundException;
import com.stock_service.stock.domain.model.ArticleModel;
import com.stock_service.stock.domain.model.CategoryModel;
import com.stock_service.stock.domain.spi.ICategoryModelPersistencePort;
import com.stock_service.stock.domain.util.Paginated;
import com.stock_service.stock.domain.util.Util;

import java.util.List;

public class CategoryModelUseCase implements ICategoryModelServicePort {

    private final ICategoryModelPersistencePort categoryModelPersistencePort;
    private final IArticleModelServicePort articleModelServicePort;

    public CategoryModelUseCase(ICategoryModelPersistencePort categoryModelPersistencePort, IArticleModelServicePort articleModelServicePort) {
        this.categoryModelPersistencePort = categoryModelPersistencePort;
        this.articleModelServicePort = articleModelServicePort;
    }


    @Override
    public CategoryModel saveCategory(CategoryModel categoryModel) {

        if (categoryModelPersistencePort.existByName(categoryModel.getName())) {
            throw new NameAlreadyExistsException(Util.CATEGORY_NAME_ALREADY_EXISTS);
        }

        return categoryModelPersistencePort.saveCategory(categoryModel);
    }


    @Override
    public Paginated<CategoryModel> getCategoriesPaginated(int page, int size, String sort, boolean ascending) {


        return categoryModelPersistencePort.getCategoriesPaginated(page, size, sort, ascending);
    }

    @Override
    public List<String> getCategoryNamesByArticleId(Long articleId) {

        ArticleModel articleModel = articleModelServicePort.getArticleById(articleId);

        if (articleModel == null ) {
            throw new NotFoundException(Util.ARTICLE_NOT_FOUND);
        }

        return articleModel.getCategories().stream()
                .map(CategoryModel::getName)
                .toList();
    }

}

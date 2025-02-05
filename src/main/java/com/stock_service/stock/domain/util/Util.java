package com.stock_service.stock.domain.util;

public class Util {

    public static final String CATEGORY_NAME_ALREADY_EXISTS = "The category name already exists.";
    public static final String NAME_REQUIRED = "The name is required.";
    public static final String NAME_SIZE = "The size must be between 1 and 50 characters.";
    public static final String DESCRIPTION_REQUIRED = "The description is required.";
    public static final String DESCRIPTION_CATEGORY_SIZE = "The size must be between 1 and 90 characters.";
    public static final String BRAND_NAME_ALREADY_EXISTS = "The brand already exists.";
    public static final String ARTICLE_NAME_ALREADY_EXISTS = "The article already exists.";
    public static final String DESCRIPTION_ARTICLE_SIZE = "The size must be between 1 and 120 characters.";
    public static final String ARTICLE_QUANTITY_MIN = "The minimum quantity is 0.";
    public static final String ARTICLE_PRICE_REQUIRED = "The price is required.";
    public static final String ARTICLE_QUANTITY_REQUIRED = "The article quantity is required.";
    public static final String ARTICLE_PRICE_MIN_VALUE = "0.0";
    public static final String ARTICLE_PRICE_MIN = "The price must be greater than 0.";
    public static final String ARTICLE_BRAND_ID_REQUIRED = "The brand ID is required.";
    public static final String ARTICLE_CATEGORIES_UNIQUE = "Duplicate categories cannot be added.";
    public static final String ARTICLE_CATEGORIES_REQUIRED = "At least one category must be provided.";
    public static final String ARTICLE_CATEGORIES_SIZE = "The article must have between 1 and 3 associated categories.";
    public static final String ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String ROLE_AUX_BODEGA = "hasRole('AUX_BODEGA')";
    public static final String ROLE_CLIENT = "hasRole('CLIENT')";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ARTICLE_NOT_FOUND = "The article ID was not found.";
    public static final String INSUFFICIENT_STOCK = "Insufficient stock.";

    public static final int ARTICLE_CATEGORIES_MAX_VALUE = 3;
    public static final int ARTICLE_CATEGORIES_MIN_VALUE = 1;
    public static final int ARTICLE_QUANTITY_MIN_VALUE = 0;
    public static final int DESCRIPTION_ARTICLE_MAX_VALUE = 120;
    public static final int DESCRIPTION_MIN_VALUE = 1;
    public static final int DESCRIPTION_CATEGORY_MAX_VALUE = 90;
    public static final int NAME_MIN_VALUE = 1;
    public static final int NAME_MAX_VALUE = 50;
    public static final int TOKEN_PREFIX_LENGTH = 7;


    // Private constructor to prevent instantiation
    private Util() {}
}

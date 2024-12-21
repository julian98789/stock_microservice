package com.stock_service.stock.domain.util;

public class Util {
    public static final String CATEGORY_NAME_ALREADY_EXISTS = "El nombre de la categoria ya existe";
    public static final String NAME_REQUIRED = "El nombre es obligatorio.";
    public static final int NAME_MIN_VALUE = 1;
    public static final int NAME_MAX_VALUE = 50;
    public static final String NAME_SIZE = "El tamaño debe estar entre 1 y 50 caracteres.";
    public static final String DESCRIPTION_REQUIRED = "La descripción es obligatoria.";
    public static final int DESCRIPTION_MIN_VALUE = 1;
    public static final int DESCRIPTION_CATEGORY_MAX_VALUE = 90;
    public static final String DESCRIPTION_CATEGORY_SIZE = "El tamaño debe estar entre 1 y 90 caracteres.";
    public static final String BRAND_NAME_ALREADY_EXISTS = "La marca ya existe";
    public static final String ARTICLE_NAME_ALREADY_EXISTS = "El articulo ya existe";

    // Private constructor to prevent instantiation
    private Util() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

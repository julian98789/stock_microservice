package com.stock_service.stock.domain.util;

public class Util {

    //Mensajes
    public static final String CATEGORY_NAME_ALREADY_EXISTS = "El nombre de la categoria ya existe";
    public static final String NAME_REQUIRED = "El nombre es obligatorio.";
    public static final String NAME_SIZE = "El tamaño debe estar entre 1 y 50 caracteres.";
    public static final String DESCRIPTION_REQUIRED = "La descripción es obligatoria.";
    public static final String DESCRIPTION_CATEGORY_SIZE = "El tamaño debe estar entre 1 y 90 caracteres.";
    public static final String BRAND_NAME_ALREADY_EXISTS = "La marca ya existe";
    public static final String ARTICLE_NAME_ALREADY_EXISTS = "El articulo ya existe";
    public static final String DESCRIPTION_ARTICLE_SIZE = "El tamaño debe estar entre 1 y 120 caracteres.";
    public static final String ARTICLE_QUANTITY_MIN = "La cantidad mínima es 0.";
    public static final String ARTICLE_PRICE_REQUIRED = "El precio es obligatorio.";
    public static final String ARTICLE_QUANTITY_REQUIRED = "La catidad del articulo es obligatorio.";
    public static final String ARTICLE_PRICE_MIN_VALUE = "0.0";
    public static final String ARTICLE_PRICE_MIN = "El precio debe ser mayor que 0.";
    public static final String ARTICLE_BRAND_ID_REQUIRED = "El id de la marca es obligatorio.";
    public static final String ARTICLE_CATEGORIES_UNIQUE = "No se pueden agregar categorías repetidas.";
    public static final String ARTICLE_CATEGORIES_REQUIRED = "Debe proporcionar al menos una categoría.";
    public static final String ARTICLE_CATEGORIES_SIZE = "El artículo debe tener entre 1 y 3 categorías asociadas.";
    public static final String ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String ROLE_AUX_BODEGA = "hasRole('AUX_BODEGA')";
    public static final String ROLE_CLIENTE = "hasRole('CLIENTE')";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final int TOKEN_PREFIX_LENGTH = 7;
    public static final String ARTICLE_NOT_FOUND = "El articulo no se encontro";

    //Valores
    public static final int ARTICLE_CATEGORIES_MAX_VALUE = 3;
    public static final int ARTICLE_CATEGORIES_MIN_VALUE = 1;
    public static final int ARTICLE_QUANTITY_MIN_VALUE = 0;
    public static final int DESCRIPTION_ARTICLE_MAX_VALUE = 120;
    public static final int DESCRIPTION_MIN_VALUE = 1;
    public static final int DESCRIPTION_CATEGORY_MAX_VALUE = 90;
    public static final int NAME_MIN_VALUE = 1;
    public static final int NAME_MAX_VALUE = 50;


    // Private constructor to prevent instantiation
    private Util() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

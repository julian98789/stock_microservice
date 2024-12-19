package com.stock_service.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class StockApplication {
	private static final Logger logger = LoggerFactory.getLogger(StockApplication.class);
	public static void main(String[] args) {

		SpringApplication.run(StockApplication.class, args);
		logger.info("🚀 La aplicacion Stock Service ha iniciado correctamente y esta lista para manejar solicitudes.");
	}

}

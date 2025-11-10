package br.com.service;

/**
 * Centraliza as configurações de conexão com a API do back-end.
 */
public final class ApiConfig {
    // Previne instanciação
    private ApiConfig() {}
    public static final String BASE_URL = "http://localhost:8080/api/v1";
}
package br.com.service;

import br.com.model.Bico;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BicoApiService implements IBicoService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    // Ajuste o endpoint se necessário
    private final String apiBaseUrl = "http://localhost:8080/api/v1/bicos";

    public BicoApiService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Bico> listarBicos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao buscar bicos: " + response.statusCode());
        }

        return objectMapper.readValue(response.body(), new TypeReference<List<Bico>>() {});
    }
}
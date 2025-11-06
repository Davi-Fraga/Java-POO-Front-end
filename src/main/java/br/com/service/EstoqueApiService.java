package br.com.service;

import br.com.model.Estoque;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class EstoqueApiService implements IEstoqueService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final String apiBaseUrl = "http://localhost:8080/api/v1/estoques";

    public EstoqueApiService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Estoque> listarEstoques() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao buscar estoques: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), new TypeReference<List<Estoque>>() {});
    }

    @Override
    public Estoque criarEstoque(Estoque estoque) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(estoque);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) { // 201 Created
            throw new IOException("Falha ao criar estoque: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), Estoque.class);
    }

    @Override
    public Estoque atualizarEstoque(Long id, Estoque estoque) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(estoque);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao atualizar estoque: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), Estoque.class);
    }

    @Override
    public void deletarEstoque(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .DELETE()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != 204) { // 204 No Content
            throw new IOException("Falha ao deletar estoque: " + response.statusCode());
        }
    }
}
package br.com.service;

import br.com.model.Custo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CustoApiService implements ICustoService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final String apiBaseUrl = "http://localhost:8080/api/v1/custos";

    public CustoApiService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Custo> listarCustos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao buscar custos: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), new TypeReference<List<Custo>>() {});
    }

    @Override
    public Custo criarCusto(Custo custo) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(custo);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) { // 201 Created
            throw new IOException("Falha ao criar custo: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), Custo.class);
    }

    @Override
    public Custo atualizarCusto(Long id, Custo custo) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(custo);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao atualizar custo: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), Custo.class);
    }

    @Override
    public void deletarCusto(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .DELETE()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != 204) { // 204 No Content
            throw new IOException("Falha ao deletar custo: " + response.statusCode());
        }
    }
}
package br.com.service;

import br.com.model.Pessoa;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class PessoaApiService implements IPessoaService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final String apiBaseUrl = "http://localhost:8080/api/v1/pessoas";

    public PessoaApiService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Pessoa> listarPessoas() throws IOException, InterruptedException {
        // A API é paginada. Para simplificar a UI, buscamos os primeiros 100 registros.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "?size=100"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao buscar pessoas: " + response.statusCode() + " " + response.body());
        }

        // O JSON de resposta da paginação tem o conteúdo dentro de um campo "content"
        JsonNode root = objectMapper.readTree(response.body());
        JsonNode content = root.path("content");

        if (content.isMissingNode() || !content.isArray()) {
            return Collections.emptyList();
        }

        return objectMapper.convertValue(content, new TypeReference<List<Pessoa>>() {});
    }

    @Override
    public Pessoa criarPessoa(Pessoa pessoa) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(pessoa);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) { // 201 Created
            throw new IOException("Falha ao criar pessoa: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), Pessoa.class);
    }

    @Override
    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(pessoa);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao atualizar pessoa: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), Pessoa.class);
    }

    @Override
    public void deletarPessoa(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .DELETE()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != 204) { // 204 No Content
            throw new IOException("Falha ao deletar pessoa: " + response.statusCode());
        }
    }
}
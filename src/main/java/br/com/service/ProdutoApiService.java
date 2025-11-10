package br.com.service;

import br.com.model.Produto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProdutoApiService implements IProdutoService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final String apiBaseUrl = ApiConfig.BASE_URL + "/produtos";

    public ProdutoApiService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Produto> listarProdutos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao buscar produtos: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), new TypeReference<List<Produto>>() {});
    }

    @Override
    public Produto criarProduto(Produto produto) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(produto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) { // 201 Created
            throw new IOException("Falha ao criar produto: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), Produto.class);
    }

    @Override
    public Produto atualizarProduto(Long id, Produto produto) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(produto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao atualizar produto: " + response.statusCode() + " " + response.body());
        }

        return objectMapper.readValue(response.body(), Produto.class);
    }

    @Override
    public void deletarProduto(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .DELETE()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != 204) { // 204 No Content
            throw new IOException("Falha ao deletar produto: " + response.statusCode());
        }
    }

    @Override
    public Produto buscarProdutoPorId(Long id) throws IOException, InterruptedException {
        // Este método não é usado pela ProdutoFrame, mas é uma boa prática tê-lo.
        // Fica como exercício para implementação, se necessário no futuro.
        throw new UnsupportedOperationException("Método 'buscarProdutoPorId' não implementado.");
    }
}
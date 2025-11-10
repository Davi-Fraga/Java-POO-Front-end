package br.com.service;

import br.com.model.Produto;
import br.com.util.HttpConnectionUtil;

import java.util.List;
import java.util.Map;

public class ProdutoService {

    private static final String ENDPOINT = "/produtos";

    /**
     * Lista todos os objetos Produto da API.
     *
     * @return Uma lista de objetos Produto.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public List<Produto> listarTodos() {
        try {
            return HttpConnectionUtil.sendGetListRequest(ENDPOINT, Produto.class);
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os produtos: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os produtos.", e);
        }
    }

    /**
     * Busca um objeto Produto por ID na API.
     *
     * @param id O ID do produto a ser buscado.
     * @return O objeto Produto correspondente ao ID.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public Produto buscarPorId(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendGetRequest(specificEndpoint, Produto.class);
        } catch (Exception e) {
            System.err.println("Erro ao buscar produto por ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar o produto com ID: " + id, e);
        }
    }

    /**
     * Cria um novo objeto Produto na API.
     *
     * @param produto O objeto Produto a ser criado.
     * @return A resposta da API como String (pode ser o objeto criado com ID, etc.).
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String criar(Produto produto) {
        try {
            return HttpConnectionUtil.sendPostRequest(ENDPOINT, produto);
        } catch (Exception e) {
            System.err.println("Erro ao criar produto: " + e.getMessage());
            throw new RuntimeException("Não foi possível criar o produto.", e);
        }
    }

    /**
     * Atualiza um objeto Produto existente na API.
     *
     * @param id O ID do produto a ser atualizado.
     * @param produto O objeto Produto a ser atualizado.
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizar(Long id, Produto produto) {
        if (id == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo para atualização.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendPutRequest(specificEndpoint, produto);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar produto com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar o produto com ID: " + id, e);
        }
    }

    /**
     * Atualiza parcialmente um objeto Produto na API.
     *
     * @param id O ID do produto a ser atualizado.
     * @param campos Um mapa contendo os campos e seus novos valores para atualização.
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizarParcial(Long id, Map<String, Object> campos) {
        if (id == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo para atualização parcial.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendPatchRequest(specificEndpoint, campos);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar parcialmente o produto com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar parcialmente o produto com ID: " + id, e);
        }
    }

    /**
     * Deleta um objeto Produto da API pelo ID.
     *
     * @param id O ID do produto a ser deletado.
     * @return true se a operação foi bem-sucedida, false caso contrário.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API.
     */
    public boolean deletar(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            HttpConnectionUtil.sendDeleteRequest(specificEndpoint);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao deletar produto com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível deletar o produto com ID: " + id, e);
        }
    }
}

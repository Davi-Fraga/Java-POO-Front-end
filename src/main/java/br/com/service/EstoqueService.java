package br.com.service;

import br.com.model.Estoque;
import br.com.util.HttpConnectionUtil;

import java.util.List;
import java.util.Map; // Importar Map

public class EstoqueService {

    private static final String ENDPOINT = "/estoques";

    /**
     * Lista todos os objetos Estoque da API.
     *
     * @return Uma lista de objetos Estoque.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public List<Estoque> listarTodos() {
        try {
            return HttpConnectionUtil.sendGetListRequest(ENDPOINT, Estoque.class);
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os estoques: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os estoques.", e);
        }
    }

    /**
     * Busca um objeto Estoque por ID na API.
     *
     * @param id O ID do estoque a ser buscado.
     * @return O objeto Estoque correspondente ao ID.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public Estoque buscarPorId(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendGetRequest(specificEndpoint, Estoque.class);
        } catch (Exception e) {
            System.err.println("Erro ao buscar estoque por ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar o estoque com ID: " + id, e);
        }
    }

    /**
     * Cria um novo objeto Estoque na API.
     *
     * @param estoque O objeto Estoque a ser criado.
     * @return A resposta da API como String (pode ser o objeto criado com ID, etc.).
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String criar(Estoque estoque) {
        try {
            return HttpConnectionUtil.sendPostRequest(ENDPOINT, estoque);
        } catch (Exception e) {
            System.err.println("Erro ao criar estoque: " + e.getMessage());
            throw new RuntimeException("Não foi possível criar o estoque.", e);
        }
    }

    /**
     * Atualiza um objeto Estoque existente na API.
     *
     * @param estoque O objeto Estoque a ser atualizado (deve conter o ID).
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizar(Estoque estoque) {
        if (estoque.getId() == null) {
            throw new IllegalArgumentException("ID do estoque não pode ser nulo para atualização.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + estoque.getId();
            return HttpConnectionUtil.sendPutRequest(specificEndpoint, estoque);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar estoque com ID " + estoque.getId() + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar o estoque com ID: " + estoque.getId(), e);
        }
    }

    /**
     * Deleta um objeto Estoque da API pelo ID.
     *
     * @param id O ID do estoque a ser deletado.
     * @return true se a operação foi bem-sucedida, false caso contrário.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API.
     */
    public boolean deletar(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            HttpConnectionUtil.sendDeleteRequest(specificEndpoint);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao deletar estoque com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível deletar o estoque com ID: " + id, e);
        }
    }

    /**
     * Atualiza parcialmente um objeto Estoque na API.
     *
     * @param id O ID do estoque a ser atualizado.
     * @param campos Um mapa contendo os campos e seus novos valores para atualização.
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizarParcial(Long id, Map<String, Object> campos) {
        if (id == null) {
            throw new IllegalArgumentException("ID do estoque não pode ser nulo para atualização parcial.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendPatchRequest(specificEndpoint, campos);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar parcialmente o estoque com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar parcialmente o estoque com ID: " + id, e);
        }
    }
}

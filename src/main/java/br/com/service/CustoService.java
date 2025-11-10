package br.com.service;

import br.com.model.Custo;
import br.com.util.HttpConnectionUtil;

import java.util.List;
import java.util.Map; // Importar Map

public class CustoService {

    private static final String ENDPOINT = "/custos";

    /**
     * Lista todos os objetos Custo da API.
     *
     * @return Uma lista de objetos Custo.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public List<Custo> listarTodos() {
        try {
            return HttpConnectionUtil.sendGetListRequest(ENDPOINT, Custo.class);
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os custos: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os custos.", e);
        }
    }

    /**
     * Busca um objeto Custo por ID na API.
     *
     * @param id O ID do custo a ser buscado.
     * @return O objeto Custo correspondente ao ID.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public Custo buscarPorId(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendGetRequest(specificEndpoint, Custo.class);
        } catch (Exception e) {
            System.err.println("Erro ao buscar custo por ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar o custo com ID: " + id, e);
        }
    }

    /**
     * Cria um novo objeto Custo na API.
     *
     * @param custo O objeto Custo a ser criado.
     * @return A resposta da API como String (pode ser o objeto criado com ID, etc.).
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String criar(Custo custo) {
        try {
            return HttpConnectionUtil.sendPostRequest(ENDPOINT, custo);
        } catch (Exception e) {
            System.err.println("Erro ao criar custo: " + e.getMessage());
            throw new RuntimeException("Não foi possível criar o custo.", e);
        }
    }

    /**
     * Atualiza um objeto Custo existente na API.
     *
     * @param custo O objeto Custo a ser atualizado (deve conter o ID).
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizar(Custo custo) {
        if (custo.id() == null) { // Usando id() para records
            throw new IllegalArgumentException("ID do custo não pode ser nulo para atualização.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + custo.id(); // Usando id() para records
            return HttpConnectionUtil.sendPutRequest(specificEndpoint, custo);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar custo com ID " + custo.id() + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar o custo com ID: " + custo.id(), e);
        }
    }

    /**
     * Deleta um objeto Custo da API pelo ID.
     *
     * @param id O ID do custo a ser deletado.
     * @return true se a operação foi bem-sucedida, false caso contrário.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API.
     */
    public boolean deletar(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            HttpConnectionUtil.sendDeleteRequest(specificEndpoint);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao deletar custo com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível deletar o custo com ID: " + id, e);
        }
    }

    /**
     * Atualiza parcialmente um objeto Custo na API.
     *
     * @param id O ID do custo a ser atualizado.
     * @param campos Um mapa contendo os campos e seus novos valores para atualização.
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizarParcial(Long id, Map<String, Object> campos) {
        if (id == null) {
            throw new IllegalArgumentException("ID do custo não pode ser nulo para atualização parcial.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendPatchRequest(specificEndpoint, campos);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar parcialmente o custo com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar parcialmente o custo com ID: " + id, e);
        }
    }
}

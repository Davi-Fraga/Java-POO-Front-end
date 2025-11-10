package br.com.service;

import br.com.model.Preco;
import br.com.util.HttpConnectionUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map; // Importar Map

public class PrecoService {

    private static final String ENDPOINT = "/precos";

    /**
     * Lista todos os objetos Preco da API.
     *
     * @return Uma lista de objetos Preco.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public List<Preco> listarTodos() {
        try {
            return HttpConnectionUtil.sendGetListRequest(ENDPOINT, Preco.class);
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os preços: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os preços.", e);
        }
    }

    /**
     * Busca um objeto Preco por ID na API.
     *
     * @param id O ID do preço a ser buscado.
     * @return O objeto Preco correspondente ao ID, ou null se não encontrado.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public Preco buscarPorId(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendGetRequest(specificEndpoint, Preco.class);
        } catch (Exception e) {
            System.err.println("Erro ao buscar preço por ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar o preço com ID: " + id, e);
        }
    }

    /**
     * Cria um novo objeto Preco na API.
     *
     * @param preco O objeto Preco a ser criado.
     * @return A resposta da API como String (pode ser o objeto criado com ID, etc.).
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String criar(Preco preco) {
        try {
            return HttpConnectionUtil.sendPostRequest(ENDPOINT, preco);
        } catch (Exception e) {
            System.err.println("Erro ao criar preço: " + e.getMessage());
            throw new RuntimeException("Não foi possível criar o preço.", e);
        }
    }

    /**
     * Atualiza um objeto Preco existente na API.
     *
     * @param preco O objeto Preco a ser atualizado (deve conter o ID).
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizar(Preco preco) {
        if (preco.getId() == null) {
            throw new IllegalArgumentException("ID do preço não pode ser nulo para atualização.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + preco.getId();
            return HttpConnectionUtil.sendPutRequest(specificEndpoint, preco);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar preço com ID " + preco.getId() + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar o preço com ID: " + preco.getId(), e);
        }
    }

    /**
     * Deleta um objeto Preco da API pelo ID.
     *
     * @param id O ID do preço a ser deletado.
     * @return true se a operação foi bem-sucedida, false caso contrário.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API.
     */
    public boolean deletar(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            String response = HttpConnectionUtil.sendDeleteRequest(specificEndpoint);
            // Dependendo da sua API, você pode verificar o conteúdo da resposta para confirmar o sucesso.
            // Por exemplo, se a API retornar "OK" ou um JSON de sucesso.
            // Por enquanto, assumimos sucesso se não houver exceção.
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao deletar preço com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível deletar o preço com ID: " + id, e);
        }
    }

    /**
     * Atualiza parcialmente um objeto Preco na API.
     *
     * @param id O ID do preço a ser atualizado.
     * @param campos Um mapa contendo os campos e seus novos valores para atualização.
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizarParcial(Long id, Map<String, Object> campos) {
        if (id == null) {
            throw new IllegalArgumentException("ID do preço não pode ser nulo para atualização parcial.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendPatchRequest(specificEndpoint, campos);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar parcialmente o preço com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar parcialmente o preço com ID: " + id, e);
        }
    }
}
package br.com.service;

import br.com.model.Acesso;
import br.com.util.HttpConnectionUtil;

import java.util.List;
import java.util.Map; // Importar Map

public class AcessoService {

    private static final String ENDPOINT = "/acessos";

    /**
     * Lista todos os objetos Acesso da API.
     *
     * @return Uma lista de objetos Acesso.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public List<Acesso> listarTodos() {
        try {
            return HttpConnectionUtil.sendGetListRequest(ENDPOINT, Acesso.class);
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os acessos: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os acessos.", e);
        }
    }

    /**
     * Busca um objeto Acesso por ID na API.
     *
     * @param id O ID do acesso a ser buscado.
     * @return O objeto Acesso correspondente ao ID.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public Acesso buscarPorId(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendGetRequest(specificEndpoint, Acesso.class);
        } catch (Exception e) {
            System.err.println("Erro ao buscar acesso por ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar o acesso com ID: " + id, e);
        }
    }

    /**
     * Cria um novo objeto Acesso na API.
     *
     * @param acesso O objeto Acesso a ser criado.
     * @return A resposta da API como String (pode ser o objeto criado com ID, etc.).
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String criar(Acesso acesso) {
        try {
            return HttpConnectionUtil.sendPostRequest(ENDPOINT, acesso);
        } catch (Exception e) {
            System.err.println("Erro ao criar acesso: " + e.getMessage());
            throw new RuntimeException("Não foi possível criar o acesso.", e);
        }
    }

    /**
     * Atualiza um objeto Acesso existente na API.
     *
     * @param acesso O objeto Acesso a ser atualizado (deve conter o ID).
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizar(Acesso acesso) {
        // Usar getIdAcesso() conforme a correção anterior
        if (acesso.getIdAcesso() == null) {
            throw new IllegalArgumentException("ID do acesso não pode ser nulo para atualização.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + acesso.getIdAcesso();
            return HttpConnectionUtil.sendPutRequest(specificEndpoint, acesso);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar acesso com ID " + acesso.getIdAcesso() + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar o acesso com ID: " + acesso.getIdAcesso(), e);
        }
    }

    /**
     * Deleta um objeto Acesso da API pelo ID.
     *
     * @param id O ID do acesso a ser deletado.
     * @return true se a operação foi bem-sucedida, false caso contrário.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API.
     */
    public boolean deletar(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            HttpConnectionUtil.sendDeleteRequest(specificEndpoint);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao deletar acesso com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível deletar o acesso com ID: " + id, e);
        }
    }

    /**
     * Atualiza parcialmente um objeto Acesso na API.
     *
     * @param id O ID do acesso a ser atualizado.
     * @param campos Um mapa contendo os campos e seus novos valores para atualização.
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizarParcial(Long id, Map<String, Object> campos) {
        if (id == null) {
            throw new IllegalArgumentException("ID do acesso não pode ser nulo para atualização parcial.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendPatchRequest(specificEndpoint, campos);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar parcialmente o acesso com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar parcialmente o acesso com ID: " + id, e);
        }
    }
}

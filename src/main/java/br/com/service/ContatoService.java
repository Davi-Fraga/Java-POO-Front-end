package br.com.service;

import br.com.model.Contato;
import br.com.util.HttpConnectionUtil;

import java.util.List;
import java.util.Map;

public class ContatoService {

    private static final String ENDPOINT = "/contatos";

    /**
     * Lista todos os objetos Contato da API.
     */
    public List<Contato> listarTodos() {
        try {
            return HttpConnectionUtil.sendGetListRequest(ENDPOINT, Contato.class);
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os contatos: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os contatos.", e);
        }
    }

    /**
     * Busca um objeto Contato por ID na API.
     */
    public Contato buscarPorId(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendGetRequest(specificEndpoint, Contato.class);
        } catch (Exception e) {
            System.err.println("Erro ao buscar contato por ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar o contato com ID: " + id, e);
        }
    }

    /**
     * Cria um novo objeto Contato na API.
     */
    public String criar(Contato contato) {
        try {
            // DEBUGS IMPORTANTES: garantem que o objeto chegou aqui com pessoaId preenchido
            System.out.println("DEBUG ContatoService Contato recebido = " + contato);
            System.out.println("DEBUG ContatoService getPessoaId() = " + contato.getPessoaId());

            return HttpConnectionUtil.sendPostRequest(ENDPOINT, contato);
        } catch (Exception e) {
            System.err.println("Erro ao criar contato: " + e.getMessage());
            throw new RuntimeException("Não foi possível criar o contato.", e);
        }
    }

    /**
     * Atualiza um objeto Contato existente na API.
     */
    public String atualizar(Contato contato) {
        if (contato.getId() == null) {
            throw new IllegalArgumentException("ID do contato não pode ser nulo para atualização.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + contato.getId();
            return HttpConnectionUtil.sendPutRequest(specificEndpoint, contato);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar contato com ID " + contato.getId() + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar o contato com ID: " + contato.getId(), e);
        }
    }

    /**
     * Deleta um objeto Contato da API pelo ID.
     */
    public boolean deletar(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            HttpConnectionUtil.sendDeleteRequest(specificEndpoint);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao deletar contato com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível deletar o contato com ID: " + id, e);
        }
    }

    /**
     * Atualiza parcialmente um objeto Contato na API.
     */
    public String atualizarParcial(Long id, Map<String, Object> campos) {
        if (id == null) {
            throw new IllegalArgumentException("ID do contato não pode ser nulo para atualização parcial.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendPatchRequest(specificEndpoint, campos);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar parcialmente o contato com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar parcialmente o contato com ID: " + id, e);
        }
    }
}

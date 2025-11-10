package br.com.service;

import br.com.model.Pessoa;
import br.com.util.HttpConnectionUtil;
import br.com.util.PaginatedResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Collections;
import java.util.List;
import java.util.Map; // Importar Map

public class PessoaService {

    private static final String ENDPOINT = "/pessoas";
    // ObjectMapper local para desserialização de tipos genéricos complexos
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Configura o ObjectMapper para lidar com tipos de data e hora do Java 8
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Lista todas as Pessoas da API.
     *
     * @return Uma lista de objetos Pessoa.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public List<Pessoa> listarTodos() {
        try {
            return HttpConnectionUtil.sendGetListRequest(ENDPOINT, Pessoa.class);
        } catch (Exception e) {
            System.err.println("Erro ao listar todas as pessoas: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar todas as pessoas.", e);
        }
    }

    /**
     * Lista Pessoas com paginação.
     *
     * @param page O número da página (base 0).
     * @param size O tamanho da página.
     * @return Um objeto PaginatedResponse contendo a lista de Pessoas e metadados de paginação.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public PaginatedResponse<Pessoa> listarComPaginacao(int page, int size) {
        try {
            String paginatedEndpoint = ENDPOINT + "?page=" + page + "&size=" + size;
            String jsonResponse = HttpConnectionUtil.sendGetRawResponse(paginatedEndpoint);

            // Desserializa a resposta JSON para PaginatedResponse<Pessoa> usando TypeReference
            return objectMapper.readValue(jsonResponse, new TypeReference<PaginatedResponse<Pessoa>>() {});
        } catch (Exception e) {
            System.err.println("Erro ao listar pessoas com paginação (page=" + page + ", size=" + size + "): " + e.getMessage());
            throw new RuntimeException("Não foi possível listar pessoas com paginação.", e);
        }
    }

    /**
     * Busca Pessoas por CPF/CNPJ.
     *
     * @param documento O CPF ou CNPJ a ser buscado.
     * @return Uma lista de objetos Pessoa que correspondem ao documento.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou desserialização.
     */
    public List<Pessoa> buscarPorCpfCnpj(String documento) {
        try {
            String searchEndpoint = ENDPOINT + "?cpfCnpj=" + documento;
            // Assumimos que a API pode retornar uma lista, mesmo que seja de um único item
            return HttpConnectionUtil.sendGetListRequest(searchEndpoint, Pessoa.class);
        } catch (Exception e) {
            System.err.println("Erro ao buscar pessoa por CPF/CNPJ " + documento + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar pessoa por CPF/CNPJ.", e);
        }
    }

    /**
     * Cria uma nova Pessoa na API.
     *
     * @param pessoa O objeto Pessoa a ser criado.
     * @return A resposta da API como String (pode ser o objeto criado com ID, etc.).
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String criar(Pessoa pessoa) {
        try {
            return HttpConnectionUtil.sendPostRequest(ENDPOINT, pessoa);
        } catch (Exception e) {
            System.err.println("Erro ao criar pessoa: " + e.getMessage());
            throw new RuntimeException("Não foi possível criar a pessoa.", e);
        }
    }

    /**
     * Atualiza uma Pessoa existente na API.
     *
     * @param pessoa O objeto Pessoa a ser atualizado (deve conter o ID).
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizar(Pessoa pessoa) {
        if (pessoa.getId() == null) {
            throw new IllegalArgumentException("ID da pessoa não pode ser nulo para atualização.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + pessoa.getId();
            return HttpConnectionUtil.sendPutRequest(specificEndpoint, pessoa);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar pessoa com ID " + pessoa.getId() + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar a pessoa com ID: " + pessoa.getId(), e);
        }
    }

    /**
     * Deleta uma Pessoa da API pelo ID.
     *
     * @param id O ID da pessoa a ser deletada.
     * @return true se a operação foi bem-sucedida, false caso contrário.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API.
     */
    public boolean deletar(Long id) {
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            HttpConnectionUtil.sendDeleteRequest(specificEndpoint);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao deletar pessoa com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível deletar a pessoa com ID: " + id, e);
        }
    }

    /**
     * Atualiza parcialmente um objeto Pessoa na API.
     *
     * @param id O ID da pessoa a ser atualizada.
     * @param campos Um mapa contendo os campos e seus novos valores para atualização.
     * @return A resposta da API como String.
     * @throws RuntimeException Se ocorrer um erro na comunicação com a API ou serialização.
     */
    public String atualizarParcial(Long id, Map<String, Object> campos) {
        if (id == null) {
            throw new IllegalArgumentException("ID da pessoa não pode ser nulo para atualização parcial.");
        }
        try {
            String specificEndpoint = ENDPOINT + "/" + id;
            return HttpConnectionUtil.sendPatchRequest(specificEndpoint, campos);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar parcialmente a pessoa com ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Não foi possível atualizar parcialmente a pessoa com ID: " + id, e);
        }
    }
}
package br.com.service;

import br.com.model.Pessoa;
import br.com.util.HttpConnectionUtil;
import br.com.util.PaginatedResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PessoaService {

    // Ajuste se o endpoint da sua API for diferente
    private static final String ENDPOINT = "/pessoas";

    /**
     * Lista todas as pessoas (sem paginação).
     * Usado, por exemplo, pelo ContatoFrame para preencher o combo de Pessoas.
     */
    public List<Pessoa> listarTodos() {
        try {
            return HttpConnectionUtil.sendGetListRequest(ENDPOINT, Pessoa.class);
        } catch (Exception e) {
            System.err.println("Erro ao listar pessoas: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar as pessoas.", e);
        }
    }

    /**
     * Lista pessoas com "paginação" para a tela PessoaFrame.
     *
     * O backend está retornando um ARRAY simples de Pessoa (ex: [ { ... }, { ... } ]),
     * e não um objeto paginado. Então:
     *  - buscamos a lista simples;
     *  - montamos um PaginatedResponse<Pessoa> manualmente.
     */
    public PaginatedResponse<Pessoa> listarComPaginacao(int pagina, int tamanhoPagina) {
        try {
            // Busca a mesma lista simples que listarTodos()
            List<Pessoa> pessoas = HttpConnectionUtil.sendGetListRequest(ENDPOINT, Pessoa.class);

            PaginatedResponse<Pessoa> resposta = new PaginatedResponse<>();
            resposta.setContent(pessoas);

            // Como o backend não manda info de página, usamos uma única página
            resposta.setPage(0);
            resposta.setSize(pessoas.size());
            resposta.setTotalElements(pessoas.size());
            resposta.setTotalPages(1);

            return resposta;
        } catch (Exception e) {
            System.err.println("Erro ao listar pessoas com paginação: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar pessoas com paginação.", e);
        }
    }

    /**
     * Busca pessoas filtrando por CPF/CNPJ.
     * A tela PessoaFrame chama este método ao clicar em "Buscar CPF/CNPJ".
     *
     * Aqui assumimos que o backend aceita o filtro por query param:
     *   GET /pessoas?cpfCnpj=...
     * Se o seu endpoint for diferente, basta ajustar a linha do endpoint.
     */
    public List<Pessoa> buscarPorCpfCnpj(String cpfCnpj) {
        try {
            String param = URLEncoder.encode(cpfCnpj, StandardCharsets.UTF_8);
            String endpointFiltrado = ENDPOINT + "?cpfCnpj=" + param;
            return HttpConnectionUtil.sendGetListRequest(endpointFiltrado, Pessoa.class);
        } catch (Exception e) {
            System.err.println("Erro ao buscar pessoas por CPF/CNPJ: " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar pessoas pelo CPF/CNPJ informado.", e);
        }
    }

    /**
     * Cria uma nova pessoa na API.
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
     * Atualiza uma pessoa existente na API.
     * A pessoa DEVE ter um ID preenchido.
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
     * Deleta uma pessoa pelo ID na API.
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

    // Se existirem outros métodos antigos (buscarPorId, atualizarParcial, etc.),
    // você pode colocá-los aqui embaixo, mantendo a mesma assinatura original.
}

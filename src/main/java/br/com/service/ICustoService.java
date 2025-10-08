package br.com.service;

import br.com.model.Custo;

import java.util.List;

public interface ICustoService {
    List<Custo> listarCustos() throws Exception;

    Custo criarCusto(Custo custo) throws Exception;

    Custo atualizarCusto(Long id, Custo custo) throws Exception;

    void deletarCusto(Long id) throws Exception;
}
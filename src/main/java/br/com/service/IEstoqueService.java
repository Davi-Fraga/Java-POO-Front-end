package br.com.service;

import br.com.model.Estoque;

import java.util.List;

public interface IEstoqueService {
    List<Estoque> listarEstoques() throws Exception;

    Estoque criarEstoque(Estoque estoque) throws Exception;

    Estoque atualizarEstoque(Long id, Estoque estoque) throws Exception;

    void deletarEstoque(Long id) throws Exception;
}
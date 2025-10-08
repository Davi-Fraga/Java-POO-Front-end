package br.com.service;

import br.com.model.Produto;

import java.util.List;

public interface IProdutoService {
    List<Produto> listarProdutos() throws Exception;

    Produto criarProduto(Produto produto) throws Exception;

    Produto atualizarProduto(Long id, Produto produto) throws Exception;

    void deletarProduto(Long id) throws Exception;
}
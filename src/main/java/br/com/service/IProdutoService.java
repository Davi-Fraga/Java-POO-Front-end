package br.com.service;

import br.com.model.Produto;

import java.io.IOException;
import java.util.List;

public interface IProdutoService {
    List<Produto> listarProdutos() throws IOException, InterruptedException;

    Produto criarProduto(Produto produto) throws IOException, InterruptedException;

    Produto atualizarProduto(Long id, Produto produto) throws IOException, InterruptedException;

    void deletarProduto(Long id) throws IOException, InterruptedException;

    Produto buscarProdutoPorId(Long id) throws IOException, InterruptedException;
}

package br.com.service;

import br.com.model.Preco;

import java.util.List;

public interface IPrecoService {
    List<Preco> listarPrecos() throws Exception;

    Preco criarPreco(Preco preco) throws Exception;

    Preco atualizarPreco(Long id, Preco preco) throws Exception;

    void deletarPreco(Long id) throws Exception;
}
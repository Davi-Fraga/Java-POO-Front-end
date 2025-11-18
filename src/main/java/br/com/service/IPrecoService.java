package br.com.service;

import br.com.model.Preco;

import java.util.List;

public interface IPrecoService {
    List<Preco> listarTodos() throws Exception; // Alterado de listarPrecos()

    Preco criar(Preco preco) throws Exception; // Alterado de criarPreco()

    Preco atualizar(Preco preco) throws Exception; // Alterado de atualizarPreco(Long id, Preco preco)

    void deletar(Long id) throws Exception; // Alterado de deletarPreco()
}

package br.com.service;

import br.com.model.Pessoa;

import java.util.List;

public interface IPessoaService {
    List<Pessoa> listarPessoas() throws Exception;

    Pessoa criarPessoa(Pessoa pessoa) throws Exception;

    Pessoa atualizarPessoa(Long id, Pessoa pessoa) throws Exception;

    void deletarPessoa(Long id) throws Exception;
}
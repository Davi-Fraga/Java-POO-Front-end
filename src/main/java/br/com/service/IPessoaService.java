package br.com.service;

import br.com.model.Pessoa;

import java.io.IOException;
import java.util.List;

public interface IPessoaService {
    List<Pessoa> listarPessoas() throws IOException, InterruptedException;
    Pessoa criarPessoa(Pessoa pessoa) throws IOException, InterruptedException;
    Pessoa atualizarPessoa(Long id, Pessoa pessoa) throws IOException, InterruptedException;
    void deletarPessoa(Long id) throws IOException, InterruptedException;
}

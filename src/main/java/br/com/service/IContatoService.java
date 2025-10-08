package br.com.service;

import br.com.model.Contato;

import java.util.List;

public interface IContatoService {
    List<Contato> listarContatos() throws Exception;

    Contato criarContato(Contato contato) throws Exception;

    Contato atualizarContato(Long id, Contato contato) throws Exception;

    void deletarContato(Long id) throws Exception;
}
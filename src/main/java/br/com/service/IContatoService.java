package br.com.service;

import br.com.model.Contato;

import java.io.IOException;
import java.util.List;

public interface IContatoService {
    List<Contato> listarContatos() throws IOException, InterruptedException;
    Contato criarContato(Contato contato) throws IOException, InterruptedException;
    Contato atualizarContato(Long id, Contato contato) throws IOException, InterruptedException;
    void deletarContato(Long id) throws IOException, InterruptedException;
}

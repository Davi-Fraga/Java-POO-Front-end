package br.com.service;

import br.com.model.Estoque;

import java.io.IOException;
import java.util.List;

public interface IEstoqueService {
    List<Estoque> listarEstoques() throws IOException, InterruptedException;
    Estoque criarEstoque(Estoque estoque) throws IOException, InterruptedException;
    Estoque atualizarEstoque(Long id, Estoque estoque) throws IOException, InterruptedException;
    void deletarEstoque(Long id) throws IOException, InterruptedException;
}

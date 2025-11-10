package br.com.service;

import br.com.model.Abastecimento;

import java.util.List;

public interface AbastecimentoService {
    void salvar(Abastecimento abastecimento);
    List<Abastecimento> buscarTodos();
}

package br.com.service;

import br.com.model.Abastecimento;
import br.com.model.Produto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AbastecimentoApiServiceMock implements AbastecimentoService {
    private static final List<Abastecimento> abastecimentos = new ArrayList<>();

    @Override
    public void salvar(Abastecimento abastecimento) {
        abastecimento.setId(abastecimentos.size() + 1);
        abastecimentos.add(abastecimento);
    }

    @Override
    public List<Abastecimento> buscarTodos() {
        return abastecimentos;
    }
}

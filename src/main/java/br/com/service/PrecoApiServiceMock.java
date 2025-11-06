package br.com.service;

import br.com.model.Preco;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrecoApiServiceMock implements IPrecoService {

    private final List<Preco> precos = new ArrayList<>();
    private long nextId = 1;

    public PrecoApiServiceMock() {
        // Dados iniciais para teste
        precos.add(new Preco(nextId++, new BigDecimal("5.89"), LocalDateTime.now().minusDays(2)));
        precos.add(new Preco(nextId++, new BigDecimal("5.95"), LocalDateTime.now().minusDays(1)));
        precos.add(new Preco(nextId++, new BigDecimal("5.99"), LocalDateTime.now()));
    }

    @Override
    public List<Preco> listarPrecos() {
        System.out.println("MOCK: Listando preços...");
        return new ArrayList<>(precos);
    }

    @Override
    public Preco criarPreco(Preco preco) throws IOException {
        System.out.println("MOCK: Criando preço: " + preco.getValor());
        preco.setId(nextId++);
        preco.setDataHoraAlteracao(LocalDateTime.now()); // Simula a data/hora do servidor
        precos.add(preco);
        return preco;
    }

    @Override
    public Preco atualizarPreco(Long id, Preco preco) throws IOException {
        System.out.println("MOCK: Atualizando preço ID: " + id);
        for (int i = 0; i < precos.size(); i++) {
            if (precos.get(i).getId().equals(id)) {
                preco.setId(id);
                preco.setDataHoraAlteracao(LocalDateTime.now()); // Simula a data/hora do servidor
                precos.set(i, preco);
                return preco;
            }
        }
        throw new IOException("Falha ao atualizar preço: ID não encontrado no mock.");
    }

    @Override
    public void deletarPreco(Long id) throws IOException {
        System.out.println("MOCK: Deletando preço ID: " + id);
        boolean removed = precos.removeIf(p -> Objects.equals(p.getId(), id));
        if (!removed) {
            throw new IOException("Falha ao deletar preço: ID não encontrado no mock.");
        }
    }
}
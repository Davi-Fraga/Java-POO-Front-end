package br.com.service;

import br.com.model.Preco;
import br.com.model.enums.TipoPreco; // Importar o enum TipoPreco

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate; // Importar LocalDate
import java.time.LocalTime; // Importar LocalTime
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrecoApiServiceMock implements IPrecoService {

    private final List<Preco> precos = new ArrayList<>();
    private long nextId = 1;

    public PrecoApiServiceMock() {
        // Dados iniciais para teste, usando o novo construtor de Preco
        LocalDate now = LocalDate.now();
        LocalTime timeNow = LocalTime.now();
        precos.add(new Preco(nextId++, new BigDecimal("5.89"), now.minusDays(2), timeNow, TipoPreco.VAREJO));
        precos.add(new Preco(nextId++, new BigDecimal("5.95"), now.minusDays(1), timeNow, TipoPreco.ATACADO));
        precos.add(new Preco(nextId++, new BigDecimal("5.99"), now, timeNow, TipoPreco.PROMOCIONAL));
    }

    @Override
    public List<Preco> listarTodos() { // Alterado de listarPrecos() para listarTodos()
        System.out.println("MOCK: Listando preços...");
        return new ArrayList<>(precos);
    }

    @Override
    public Preco criar(Preco preco) throws IOException { // Alterado de criarPreco() para criar()
        System.out.println("MOCK: Criando preço: " + preco.getValor());
        preco.setId(nextId++);
        preco.setDataAlteracao(LocalDate.now()); // Simula a data do servidor
        preco.setHoraAlteracao(LocalTime.now()); // Simula a hora do servidor
        // O TipoPreco já deve vir preenchido do frontend
        precos.add(preco);
        return preco;
    }

    @Override
    public Preco atualizar(Preco preco) throws IOException { // Alterado de atualizarPreco() para atualizar()
        System.out.println("MOCK: Atualizando preço ID: " + preco.getId());
        for (int i = 0; i < precos.size(); i++) {
            if (precos.get(i).getId().equals(preco.getId())) {
                preco.setDataAlteracao(LocalDate.now()); // Simula a data do servidor
                preco.setHoraAlteracao(LocalTime.now()); // Simula a hora do servidor
                // O TipoPreco já deve vir preenchido do frontend
                precos.set(i, preco);
                return preco;
            }
        }
        throw new IOException("Falha ao atualizar preço: ID não encontrado no mock.");
    }

    @Override
    public void deletar(Long id) throws IOException { // Alterado de deletarPreco() para deletar()
        System.out.println("MOCK: Deletando preço ID: " + id);
        boolean removed = precos.removeIf(p -> Objects.equals(p.getId(), id));
        if (!removed) {
            throw new IOException("Falha ao deletar preço: ID não encontrado no mock.");
        }
    }
}

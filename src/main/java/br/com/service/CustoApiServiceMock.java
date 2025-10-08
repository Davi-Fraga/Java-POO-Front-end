package br.com.service;

import br.com.model.Custo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustoApiServiceMock implements ICustoService {

    private final List<Custo> custos = new ArrayList<>();
    private long nextId = 1;

    public CustoApiServiceMock() {
        // Dados iniciais para teste
        custos.add(new Custo(nextId++, 17.5, 2.50, 0.80, 25.0, LocalDate.now().minusDays(10)));
        custos.add(new Custo(nextId++, 18.0, 2.55, 0.80, 26.0, LocalDate.now().minusDays(5)));
        custos.add(new Custo(nextId++, 18.0, 2.60, 0.85, 26.0, LocalDate.now()));
    }

    @Override
    public List<Custo> listarCustos() {
        System.out.println("MOCK: Listando custos...");
        return new ArrayList<>(custos);
    }

    @Override
    public Custo criarCusto(Custo custo) throws IOException {
        System.out.println("MOCK: Criando custo para a data: " + custo.getDataProcessamento());
        if (custos.stream().anyMatch(c -> c.getDataProcessamento().isEqual(custo.getDataProcessamento()))) {
            throw new IOException("Falha ao criar custo: Já existe um custo para esta data no mock.");
        }
        custo.setId(nextId++);
        custos.add(custo);
        return custo;
    }

    @Override
    public Custo atualizarCusto(Long id, Custo custo) throws IOException {
        System.out.println("MOCK: Atualizando custo ID: " + id);
        for (int i = 0; i < custos.size(); i++) {
            if (custos.get(i).getId().equals(id)) {
                custo.setId(id);
                custos.set(i, custo);
                return custo;
            }
        }
        throw new IOException("Falha ao atualizar custo: ID não encontrado no mock.");
    }

    @Override
    public void deletarCusto(Long id) throws IOException {
        System.out.println("MOCK: Deletando custo ID: " + id);
        boolean removed = custos.removeIf(p -> Objects.equals(p.getId(), id));
        if (!removed) {
            throw new IOException("Falha ao deletar custo: ID não encontrado no mock.");
        }
    }
}
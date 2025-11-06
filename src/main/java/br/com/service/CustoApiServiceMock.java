package br.com.service;

import br.com.model.Custo;
import br.com.model.enums.TipoCusto;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CustoApiServiceMock implements ICustoService {

    private final List<Custo> custos = new ArrayList<>();
    private long nextId = 1;

    public CustoApiServiceMock() {
        // Dados iniciais para teste
        custos.add(new Custo(nextId++, new BigDecimal("17.5"), new BigDecimal("2.50"), new BigDecimal("0.80"), new BigDecimal("25.0"), LocalDate.now().minusDays(10), TipoCusto.IMPOSTO));
        custos.add(new Custo(nextId++, new BigDecimal("18.0"), new BigDecimal("2.55"), new BigDecimal("0.80"), new BigDecimal("26.0"), LocalDate.now().minusDays(5), TipoCusto.VARIAVEL));
        custos.add(new Custo(nextId++, new BigDecimal("18.0"), new BigDecimal("2.60"), new BigDecimal("0.85"), new BigDecimal("26.0"), LocalDate.now(), TipoCusto.FIXO));
    }

    @Override
    public List<Custo> listarCustos() {
        System.out.println("MOCK: Listando custos...");
        return new ArrayList<>(custos);
    }

    @Override
    public Custo criarCusto(Custo custo) throws IOException {
        System.out.println("MOCK: Criando custo para a data: " + custo.dataProcessamento());
        if (custos.stream().anyMatch(c -> c.dataProcessamento().isEqual(custo.dataProcessamento()))) {
            throw new IOException("Falha ao criar custo: Já existe um custo para esta data no mock.");
        }
        Custo novoCusto = new Custo(nextId++, custo.imposto(), custo.custoVariavel(), custo.custoFixo(), custo.margemLucro(), custo.dataProcessamento(), custo.tipoCusto());
        custos.add(novoCusto);
        return novoCusto;
    }

    @Override
    public Custo atualizarCusto(Long id, Custo custo) throws IOException {
        System.out.println("MOCK: Atualizando custo ID: " + id);
        for (int i = 0; i < custos.size(); i++) {
            if (custos.get(i).id().equals(id)) {
                // Cria uma nova instância com o ID existente e os novos dados
                Custo custoAtualizado = new Custo(
                        id,
                        custo.imposto(),
                        custo.custoVariavel(),
                        custo.custoFixo(),
                        custo.margemLucro(),
                        custo.dataProcessamento(),
                        custo.tipoCusto()
                );
                custos.set(i, custoAtualizado);
                return custoAtualizado;
            }
        }
        throw new IOException("Falha ao atualizar custo: ID não encontrado no mock.");
    }

    @Override
    public void deletarCusto(Long id) throws IOException {
        System.out.println("MOCK: Deletando custo ID: " + id);
        boolean removed = custos.removeIf(p -> Objects.equals(p.id(), id));
        if (!removed) {
            throw new IOException("Falha ao deletar custo: ID não encontrado no mock.");
        }
    }
}

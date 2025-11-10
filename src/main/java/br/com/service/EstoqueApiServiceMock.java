package br.com.service;

import br.com.model.Estoque;
import br.com.model.enums.TipoEstoque;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EstoqueApiServiceMock implements IEstoqueService {

    private final List<Estoque> estoques = new ArrayList<>();
    private long nextId = 1;

    public EstoqueApiServiceMock() {
        // Dados iniciais para teste
        estoques.add(new Estoque(nextId++, new BigDecimal("10000.50"), "Tanque 01", "Pátio Principal", "LOTE-GC-2024", LocalDate.of(2025, 12, 31), TipoEstoque.GASOLINA_COMUM));
        estoques.add(new Estoque(nextId++, new BigDecimal("500.00"), "Prateleira A-5", "Depósito 2", "LOTE-DS10-2023", LocalDate.of(2026, 6, 30), TipoEstoque.DIESEL_S10));
        estoques.add(new Estoque(nextId++, new BigDecimal("8000.00"), "Tanque 02", "Pátio Principal", "LOTE-ET-2024", LocalDate.of(2025, 12, 31), TipoEstoque.ETANOL));
    }

    @Override
    public List<Estoque> listarEstoques() {
        System.out.println("MOCK: Listando estoques...");
        return new ArrayList<>(estoques);
    }

    @Override
    public Estoque criarEstoque(Estoque estoque) throws IOException {
        // Corrigido: Usar getLocalFabricacao() em vez de getLoteFabricacao()
        System.out.println("MOCK: Criando estoque para o lote: " + estoque.getLocalFabricacao());
        if (estoques.stream().anyMatch(e -> e.getLocalFabricacao().equalsIgnoreCase(estoque.getLocalFabricacao()))) {
            throw new IOException("Falha ao criar estoque: Lote de Fabricação já existe no mock.");
        }
        estoque.setId(nextId++);
        estoques.add(estoque);
        return estoque;
    }

    @Override
    public Estoque atualizarEstoque(Long id, Estoque estoque) throws IOException {
        System.out.println("MOCK: Atualizando estoque ID: " + id);
        for (int i = 0; i < estoques.size(); i++) {
            if (estoques.get(i).getId().equals(id)) {
                estoque.setId(id);
                estoques.set(i, estoque);
                return estoque;
            }
        }
        throw new IOException("Falha ao atualizar estoque: ID não encontrado no mock.");
    }

    @Override
    public void deletarEstoque(Long id) throws IOException {
        System.out.println("MOCK: Deletando estoque ID: " + id);
        boolean removed = estoques.removeIf(p -> Objects.equals(p.getId(), id));
        if (!removed) {
            throw new IOException("Falha ao deletar estoque: ID não encontrado no mock.");
        }
    }
}
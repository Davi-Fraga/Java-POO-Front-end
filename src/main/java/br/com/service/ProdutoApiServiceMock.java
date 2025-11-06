package br.com.service;

import br.com.model.Produto;
import br.com.model.enums.TipoProduto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProdutoApiServiceMock implements IProdutoService {

    private final List<Produto> produtos = new ArrayList<>();
    private long nextId = 1;

    public ProdutoApiServiceMock() {
        // Dados iniciais para teste
        produtos.add(new Produto(nextId++, "Gasolina Comum", "GC001", "Petrobras", "Petrobras", "Combustível", TipoProduto.COMBUSTIVEL, new BigDecimal("5.89")));
        produtos.add(new Produto(nextId++, "Óleo Motor 5W30", "OM5W30", "Ipiranga", "Ipiranga", "Lubrificante", TipoProduto.LUBRIFICANTE, new BigDecimal("45.50")));
        produtos.add(new Produto(nextId++, "Pneu Aro 15", "PN185-60R15", "Goodyear", "Goodyear", "Pneus", TipoProduto.CONVENIENCIA, new BigDecimal("350.00")));
    }

    @Override
    public List<Produto> listarProdutos() throws IOException, InterruptedException {
        System.out.println("MOCK: Listando produtos...");
        return new ArrayList<>(produtos);
    }

    @Override
    public Produto criarProduto(Produto produto) throws IOException, InterruptedException {
        System.out.println("MOCK: Criando produto: " + produto.getNome());
        if (produtos.stream().anyMatch(p -> p.getNome().equalsIgnoreCase(produto.getNome()))) {
            throw new IOException("Falha ao criar produto: Nome já existe no mock.");
        }
        produto.setId(nextId++);
        produtos.add(produto);
        return produto;
    }

    @Override
    public Produto atualizarProduto(Long id, Produto produto) throws IOException, InterruptedException {
        System.out.println("MOCK: Atualizando produto ID: " + id);
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId().equals(id)) {
                produto.setId(id);
                produtos.set(i, produto);
                return produto;
            }
        }
        throw new IOException("Falha ao atualizar produto: ID não encontrado no mock.");
    }

    @Override
    public void deletarProduto(Long id) throws IOException, InterruptedException {
        System.out.println("MOCK: Deletando produto ID: " + id);
        boolean removed = produtos.removeIf(p -> Objects.equals(p.getId(), id));
        if (!removed) {
            throw new IOException("Falha ao deletar produto: ID não encontrado no mock.");
        }
    }

    @Override
    public Produto buscarProdutoPorId(Long id) throws IOException, InterruptedException {
        return produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IOException("Produto não encontrado no mock."));
    }
}

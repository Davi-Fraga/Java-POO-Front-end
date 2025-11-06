package br.com.service;

import br.com.model.Pessoa;
import br.com.model.enums.TipoPessoa;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementação "Mock" (simulada) do serviço de Pessoa.
 * Usa uma lista em memória para testar a UI sem precisar do backend.
 */
public class PessoaApiServiceMock implements IPessoaService {

    private final List<Pessoa> pessoas = new ArrayList<>();
    private long nextId = 1;

    public PessoaApiServiceMock() {
        // Dados iniciais para teste
        pessoas.add(new Pessoa(nextId++, "Maria da Silva (Mock)", "12345678901", 12345L, LocalDate.of(1990, 5, 10), TipoPessoa.FISICA));
        pessoas.add(new Pessoa(nextId++, "João Pereira (Mock)", "98765432100", 54321L, LocalDate.of(1985, 11, 20), TipoPessoa.FISICA));
        pessoas.add(new Pessoa(nextId++, "Empresa XYZ (Mock)", "11222333000144", null, LocalDate.of(2005, 1, 15), TipoPessoa.JURIDICA));
    }

    @Override
    public List<Pessoa> listarPessoas() {
        System.out.println("MOCK: Listando pessoas...");
        return new ArrayList<>(pessoas); // Retorna uma cópia para segurança
    }

    @Override
    public Pessoa criarPessoa(Pessoa pessoa) throws IOException {
        System.out.println("MOCK: Criando pessoa: " + pessoa.getNomeCompleto());
        // Simula a validação de CPF/CNPJ único
        if (pessoas.stream().anyMatch(p -> p.getCpfCnpj().equals(pessoa.getCpfCnpj()))) {
            throw new IOException("Falha ao criar pessoa: CPF/CNPJ já existe no mock.");
        }
        pessoa.setId(nextId++);
        pessoas.add(pessoa);
        return pessoa;
    }

    @Override
    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) throws IOException {
        System.out.println("MOCK: Atualizando pessoa ID: " + id);
        for (int i = 0; i < pessoas.size(); i++) {
            if (pessoas.get(i).getId().equals(id)) {
                pessoa.setId(id);
                pessoas.set(i, pessoa);
                return pessoa;
            }
        }
        throw new IOException("Falha ao atualizar pessoa: ID não encontrado no mock.");
    }

    @Override
    public void deletarPessoa(Long id) throws IOException {
        System.out.println("MOCK: Deletando pessoa ID: " + id);
        boolean removed = pessoas.removeIf(p -> Objects.equals(p.getId(), id));
        if (!removed) {
            throw new IOException("Falha ao deletar pessoa: ID não encontrado no mock.");
        }
    }
}

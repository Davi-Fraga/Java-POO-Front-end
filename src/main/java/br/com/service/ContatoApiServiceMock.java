package br.com.service;

import br.com.model.Contato;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContatoApiServiceMock implements IContatoService {

    private final List<Contato> contatos = new ArrayList<>();
    private long nextId = 1;

    public ContatoApiServiceMock() {
        // Dados iniciais para teste
        contatos.add(new Contato(nextId++, "(45) 99999-8888", "contato@postoexemplo.com", "Av. Brasil, 123, Centro"));
        contatos.add(new Contato(nextId++, "(11) 5555-4444", "comercial@fornecedor.com", "Rua das Indústrias, 456, SP"));
    }

    @Override
    public List<Contato> listarContatos() {
        System.out.println("MOCK: Listando contatos...");
        return new ArrayList<>(contatos);
    }

    @Override
    public Contato criarContato(Contato contato) throws IOException {
        System.out.println("MOCK: Criando contato: " + contato.getEmail());
        if (contatos.stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(contato.getEmail()))) {
            throw new IOException("Falha ao criar contato: E-mail já existe no mock.");
        }
        contato.setId(nextId++);
        contatos.add(contato);
        return contato;
    }

    @Override
    public Contato atualizarContato(Long id, Contato contato) throws IOException {
        System.out.println("MOCK: Atualizando contato ID: " + id);
        for (int i = 0; i < contatos.size(); i++) {
            if (contatos.get(i).getId().equals(id)) {
                contato.setId(id);
                contatos.set(i, contato);
                return contato;
            }
        }
        throw new IOException("Falha ao atualizar contato: ID não encontrado no mock.");
    }

    @Override
    public void deletarContato(Long id) throws IOException {
        System.out.println("MOCK: Deletando contato ID: " + id);
        boolean removed = contatos.removeIf(p -> Objects.equals(p.getId(), id));
        if (!removed) {
            throw new IOException("Falha ao deletar contato: ID não encontrado no mock.");
        }
    }
}
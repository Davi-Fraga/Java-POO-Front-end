package br.com.dao;

import br.com.model.PerfilAcesso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO para gerenciar perfis de acesso, suas permissões e status.
 * Utiliza uma lista estática para simular um banco de dados (Mock).
 */
public class PerfilAcessoDAO {

    private static final List<PerfilAcesso> perfis = new ArrayList<>();
    private static int proximoId = 1;

    static {
        // Mock de perfis iniciais
        PerfilAcesso perfil1 = new PerfilAcesso(proximoId++, "teste1", "Ativo");
        perfil1.setPermissao("gerencia", true);
        perfil1.setPermissao("estoque", true);
        perfil1.setPermissao("vendas", false);
        perfis.add(perfil1);

        PerfilAcesso perfil2 = new PerfilAcesso(proximoId++, "teste2", "Ativo");
        perfil2.setPermissao("vendas", true);
        perfil2.setPermissao("gerencia", false);
        perfis.add(perfil2);

        PerfilAcesso perfil3 = new PerfilAcesso(proximoId++, "desconhecido", "Bloqueado");
        perfis.add(perfil3);
    }

    public List<PerfilAcesso> consultarPerfis() {
        return new ArrayList<>(perfis);
    }

    public void adicionarPerfil(String nome, Map<String, Boolean> permissoesIniciais) {
        PerfilAcesso novoPerfil = new PerfilAcesso(proximoId++, nome, "Ativo");
        novoPerfil.setPermissoes(permissoesIniciais);
        perfis.add(novoPerfil);
    }

    public void removerPerfil(int idPerfil) {
        perfis.removeIf(p -> p.getIdPerfil() == idPerfil);
    }

    public PerfilAcesso consultarPerfilPorNome(String nome) {
        return perfis.stream()
            .filter(p -> p.getNomePerfil().equals(nome))
            .findFirst()
            .orElse(null);
    }

    public void alternarStatus(int idPerfil) {
        for (PerfilAcesso perfil : perfis) {
            if (perfil.getIdPerfil() == idPerfil) {
                String novoStatus = "Ativo".equals(perfil.getStatus()) ? "Bloqueado" : "Ativo";
                perfil.setStatus(novoStatus);
                break;
            }
        }
    }

    public void modificarPermissao(int idPerfil, String area, boolean podeAcessar) {
        for (PerfilAcesso perfil : perfis) {
            if (perfil.getIdPerfil() == idPerfil) {
                perfil.setPermissao(area, podeAcessar);
                break;
            }
        }
    }
}

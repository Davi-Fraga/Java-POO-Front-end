package br.com.dao;

import br.com.model.Acesso;
import br.com.model.PerfilAcesso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gerenciar os logs de acesso.
 */
public class AcessoDAO {

    private static final List<Acesso> logs = new ArrayList<>();
    private static int proximoId = 1;
    private final PerfilAcessoDAO perfilDAO = new PerfilAcessoDAO(); // Instância para interagir

    static {
        // Logs de acesso iniciais
        logs.add(new Acesso(proximoId++, "teste1", "192.168.1.10", LocalDateTime.now().minusHours(1), "Sucesso"));
        logs.add(new Acesso(proximoId++, "teste2", "Módulo_B", LocalDateTime.now().minusMinutes(30), "Sucesso"));
        logs.add(new Acesso(proximoId++, "teste1", "192.168.1.10", LocalDateTime.now().minusMinutes(15), "Falha"));
        logs.add(new Acesso(proximoId++, "desconhecido", "203.0.113.25", LocalDateTime.now().minusMinutes(5), "Bloqueado"));
    }

    /**
     * Registra uma nova tentativa de acesso, verificando o status do perfil.
     */
    public void registrarTentativaAcesso(String nomePerfil, String localLogin, boolean sucesso) {
        PerfilAcesso perfil = perfilDAO.consultarPerfilPorNome(nomePerfil);
        String statusFinal;

        if (perfil != null && "Bloqueado".equals(perfil.getStatus())) {
            statusFinal = "Bloqueado";
        } else {
            statusFinal = sucesso ? "Sucesso" : "Falha";
        }

        logs.add(new Acesso(proximoId++, nomePerfil, localLogin, LocalDateTime.now(), statusFinal));
    }

    /**
     * Retorna todos os logs de acesso.
     */
    public List<Acesso> consultarLogs() {
        return new ArrayList<>(logs);
    }
}

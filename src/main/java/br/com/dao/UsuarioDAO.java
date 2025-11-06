package br.com.dao;

import br.com.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela lógica de acesso e persistência de dados.
 */
public class UsuarioDAO {

    private static final List<Usuario> usuarios = new ArrayList<>();

    static {
        // Pré-populando a lista de usuários com os dados de teste especificados
        usuarios.add(new Usuario(1, "teste1", "123", "GERENCIA"));
        usuarios.add(new Usuario(2, "teste2", "123", "ABASTECIMENTO"));
    }

    /**
     * Autentica um usuário com base no login e senha.
     *
     * @param login O login do usuário.
     * @param senha A senha do usuário.
     * @return O objeto Usuario se a autenticação for bem-sucedida, ou null caso contrário.
     */
    public Usuario autenticar(String login, String senha) {
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login) && usuario.getSenha().equals(senha)) {
                return usuario;
            }
        }
        return null;
    }
}

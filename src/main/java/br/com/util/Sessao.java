package br.com.util;

import br.com.model.Usuario;

/**
 * Classe estática para armazenar o usuário logado e seu perfil.
 */
public class Sessao {

    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado) {
        Sessao.usuarioLogado = usuarioLogado;
    }
}

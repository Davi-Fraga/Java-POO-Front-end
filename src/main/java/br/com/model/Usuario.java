package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Classe POJO para representar um usuário.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    private int id;
    private String login;
    private String senha;
    private String perfilAcesso;

    public Usuario(int id, String login, String senha, String perfilAcesso) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.perfilAcesso = perfilAcesso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfilAcesso() {
        return perfilAcesso;
    }

    public void setPerfilAcesso(String perfilAcesso) {
        this.perfilAcesso = perfilAcesso;
    }
}

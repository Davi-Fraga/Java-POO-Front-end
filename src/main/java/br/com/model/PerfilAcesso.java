package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe POJO para representar um perfil de acesso com suas permissões e status.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerfilAcesso {

    private int idPerfil;
    private String nomePerfil;
    private String status; // "Ativo", "Bloqueado"
    private Map<String, Boolean> permissoes;

    // Construtor padrão para desserialização do Jackson
    public PerfilAcesso() {
        this.permissoes = new HashMap<>();
    }

    public PerfilAcesso(int idPerfil, String nomePerfil, String status) {
        this.idPerfil = idPerfil;
        this.nomePerfil = nomePerfil;
        this.status = status;
        this.permissoes = new HashMap<>();
    }

    // Getters e Setters

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNomePerfil() {
        return nomePerfil;
    }

    public void setNomePerfil(String nomePerfil) {
        this.nomePerfil = nomePerfil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Boolean> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Map<String, Boolean> permissoes) {
        this.permissoes = permissoes;
    }

    public void setPermissao(String area, boolean podeAcessar) {
        this.permissoes.put(area, podeAcessar);
    }
}

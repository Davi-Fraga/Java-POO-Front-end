package br.com.model;

import java.time.LocalDateTime;

/**
 * Classe POJO para representar um registro de acesso.
 */
public class Acesso {

    private int idAcesso;
    private String usuarioOuPerfil;
    private String localLogin;
    private LocalDateTime dataHora;
    private String status;

    public Acesso(int idAcesso, String usuarioOuPerfil, String localLogin, LocalDateTime dataHora, String status) {
        this.idAcesso = idAcesso;
        this.usuarioOuPerfil = usuarioOuPerfil;
        this.localLogin = localLogin;
        this.dataHora = dataHora;
        this.status = status;
    }

    // Getters e Setters

    public int getIdAcesso() {
        return idAcesso;
    }

    public void setIdAcesso(int idAcesso) {
        this.idAcesso = idAcesso;
    }

    public String getUsuarioOuPerfil() {
        return usuarioOuPerfil;
    }

    public void setUsuarioOuPerfil(String usuarioOuPerfil) {
        this.usuarioOuPerfil = usuarioOuPerfil;
    }

    public String getLocalLogin() {
        return localLogin;
    }

    public void setLocalLogin(String localLogin) {
        this.localLogin = localLogin;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

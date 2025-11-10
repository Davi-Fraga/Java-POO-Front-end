package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Acesso {

    private Long id;
    private String nomeAcesso; // Atributo para o nome do acesso
    private String usuarioPerfil; // Atributo para o perfil do usuário
    private String localLogin; // Atributo para o local de login
    private String status;
    private LocalDateTime dataHora;

    // Construtor padrão (necessário para desserialização do Jackson)
    public Acesso() {
    }

    // Construtor com todos os campos
    public Acesso(Long id, String nomeAcesso, String usuarioPerfil, String localLogin, String status, LocalDateTime dataHora) {
        this.id = id;
        this.nomeAcesso = nomeAcesso;
        this.usuarioPerfil = usuarioPerfil;
        this.localLogin = localLogin;
        this.status = status;
        this.dataHora = dataHora;
    }

    // NOVO CONSTRUTOR SOBRECARREGADO PARA ACOMPANHAR O ACESSO DAO
    // Mapeia os 5 argumentos que AcessoDAO está passando para os 6 atributos da classe Acesso.
    // O 'usuarioPerfil' será definido como "N/A" por padrão neste construtor.
    public Acesso(Long id, String nomeAcesso, String localLogin, LocalDateTime dataHora, String status) {
        this(id, nomeAcesso, "N/A", localLogin, status, dataHora); // Chama o construtor completo
    }

    // Getters corrigidos para corresponder aos chamados em AcessoFrame.java
    public Long getIdAcesso() { // Renomeado de getId() para getIdAcesso()
        return id;
    }

    public String getAcesso() { // Getter para o nome do acesso (mantido)
        return nomeAcesso;
    }

    public String getUsuarioOuPerfil() { // Renomeado de getUsuarioPerfil() para getUsuarioOuPerfil()
        return usuarioPerfil;
    }

    public String getLocalLogin() { // Renomeado de getLocalLoguin() para getLocalLogin()
        return localLogin;
    }

    public String getStatus() { // Mantido
        return status;
    }

    public LocalDateTime getDataHora() { // Mantido
        return dataHora;
    }

    // Setters corrigidos para manter a consistência
    public void setIdAcesso(Long id) { // Renomeado de setId() para setIdAcesso()
        this.id = id;
    }

    public void setAcesso(String nomeAcesso) { // Setter para o nome do acesso (mantido)
        this.nomeAcesso = nomeAcesso;
    }

    public void setUsuarioOuPerfil(String usuarioPerfil) { // Renomeado de setUsuarioPerfil() para setUsuarioOuPerfil()
        this.usuarioPerfil = usuarioPerfil;
    }

    public void setLocalLogin(String localLogin) { // Renomeado de setLocalLoguin() para setLocalLogin()
        this.localLogin = localLogin;
    }

    public void setStatus(String status) { // Mantido
        this.status = status;
    }

    public void setDataHora(LocalDateTime dataHora) { // Mantido
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "Acesso{" +
                "id=" + id +
                ", nomeAcesso='" + nomeAcesso + '\'' +
                ", usuarioPerfil='" + usuarioPerfil + '\'' +
                ", localLogin='" + localLogin + '\'' +
                ", status='" + status + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}

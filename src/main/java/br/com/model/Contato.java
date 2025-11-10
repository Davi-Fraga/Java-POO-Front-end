package br.com.model;

import br.com.model.enums.TipoContato; // Importar o enum TipoContato
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contato {

    private Long id;
    private TipoContato tipoContato; // Alterado para o tipo enum TipoContato
    private String telefone;
    private String email;
    private String endereco;

    // Construtor padrão (necessário para desserialização do Jackson)
    public Contato() {
    }

    // Construtor com todos os campos
    public Contato(Long id, TipoContato tipoContato, String telefone, String email, String endereco) {
        this.id = id;
        this.tipoContato = tipoContato;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public TipoContato getTipoContato() { // Getter retorna TipoContato enum
        return tipoContato;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTipoContato(TipoContato tipoContato) { // Setter aceita TipoContato enum
        this.tipoContato = tipoContato;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Contato{" +
               "id=" + id +
               ", tipoContato=" + tipoContato + // toString do enum será usado
               ", telefone='" + telefone + '\'' +
               ", email='" + email + '\'' +
               ", endereco='" + endereco + '\'' +
               '}';
    }
}

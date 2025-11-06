package br.com.model;

import br.com.model.enums.TipoContato;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contato {

    private Long id;
    private String telefone;
    private String email;
    private String endereco;
    private TipoContato tipoContato;

    public Contato() {
    }

    public Contato(Long id, String telefone, String email, String endereco, TipoContato tipoContato) {
        this.id = id;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.tipoContato = tipoContato;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public TipoContato getTipoContato() {
        return tipoContato;
    }

    public void setTipoContato(TipoContato tipoContato) {
        this.tipoContato = tipoContato;
    }

    @Override
    public String toString() {
        return email;
    }
}

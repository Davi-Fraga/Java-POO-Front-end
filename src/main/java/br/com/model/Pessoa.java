package br.com.model;

import br.com.model.enums.TipoPessoa;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pessoa {

    // Aceita vários nomes possíveis de ID vindos do backend:
    //  "id", "pessoaId", "idPessoa", "codigo", "codigoPessoa", "id_pessoa", "pessoa_id"
    @JsonProperty("id")
    @JsonAlias({
            "pessoaId",
            "idPessoa",
            "codigo",
            "codigoPessoa",
            "id_pessoa",
            "pessoa_id"
    })
    private Long id;

    // Backend envia "nomeCompleto", mapeamos para "nome"
    @JsonProperty("nomeCompleto")
    private String nome;

    private String cpfCnpj;
    private LocalDate dataNascimento;
    private TipoPessoa tipoPessoa;

    // Construtor padrão (necessário para desserialização do Jackson)
    public Pessoa() {
    }

    // Construtor com todos os campos
    public Pessoa(Long id, String nome, String cpfCnpj, LocalDate dataNascimento, TipoPessoa tipoPessoa) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.dataNascimento = dataNascimento;
        this.tipoPessoa = tipoPessoa;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpfCnpj='" + cpfCnpj + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", tipoPessoa=" + tipoPessoa +
                '}';
    }
}

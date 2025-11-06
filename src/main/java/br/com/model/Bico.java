package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Modelo para representar um Bico de Combustível.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bico {

    private Long id;
    private String nome;
    private String status;
    private Produto produto; // Assumindo que a API retorna o objeto Produto aninhado

    public Bico() {
    }

    public Bico(Long id, String nome, String status, Produto produto) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.produto = produto;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return nome;
    }
}
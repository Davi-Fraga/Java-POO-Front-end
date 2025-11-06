package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import br.com.model.enums.TipoProduto;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Produto {

    private Long id;
    private String nome;
    private String referencia;
    private String fornecedor;
    private String marca;
    private String categoria;
    private TipoProduto tipoProduto;
    private BigDecimal precoVenda;

    public Produto() {
    }

    // Construtor atualizado para incluir o novo campo
    public Produto(Long id, String nome, String referencia, String fornecedor, String marca, String categoria, TipoProduto tipoProduto, BigDecimal precoVenda) {
        this.id = id;
        this.nome = nome;
        this.referencia = referencia;
        this.fornecedor = fornecedor;
        this.marca = marca;
        this.categoria = categoria;
        this.tipoProduto = tipoProduto;
        this.precoVenda = precoVenda;
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

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    @Override
    public String toString() {
        return nome;
    }
}

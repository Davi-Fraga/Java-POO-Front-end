package br.com.model;

import br.com.model.enums.TipoProduto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Produto {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TipoProduto tipoProduto;
    private String marca;
    private String fornecedor;
    private String referencia;
    private String categoria;

    // Construtor padrão (necessário para desserialização do Jackson)
    public Produto() {
    }

    // Construtor com todos os campos
    public Produto(Long id, String nome, String descricao, BigDecimal preco, LocalDateTime createdAt, LocalDateTime updatedAt, TipoProduto tipoProduto, String marca, String fornecedor, String referencia, String categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tipoProduto = tipoProduto;
        this.marca = marca;
        this.fornecedor = fornecedor;
        this.referencia = referencia;
        this.categoria = categoria;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public String getMarca() {
        return marca;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getCategoria() {
        return categoria;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", tipoProduto=" + tipoProduto +
                ", marca='" + marca + '\'' +
                ", fornecedor='" + fornecedor + '\'' +
                ", referencia='" + referencia + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}

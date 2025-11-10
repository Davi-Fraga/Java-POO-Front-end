package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Abastecimento {
    private int id;
    private Produto produto;
    private double quantidade;
    private double precoLitro;
    private double total;
    private Date data;

    public Abastecimento(Produto produto, double quantidade, double precoLitro, double total, Date data) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoLitro = precoLitro;
        this.total = total;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoLitro() {
        return precoLitro;
    }

    public void setPrecoLitro(double precoLitro) {
        this.precoLitro = precoLitro;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}

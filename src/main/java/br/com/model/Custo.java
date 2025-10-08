package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Custo {

    private Long id;
    private Double imposto;
    private Double custoVariavel;
    private Double custoFixo;
    private Double margemLucro;
    private LocalDate dataProcessamento;

    public Custo() {
    }

    public Custo(Long id, Double imposto, Double custoVariavel, Double custoFixo, Double margemLucro, LocalDate dataProcessamento) {
        this.id = id;
        this.imposto = imposto;
        this.custoVariavel = custoVariavel;
        this.custoFixo = custoFixo;
        this.margemLucro = margemLucro;
        this.dataProcessamento = dataProcessamento;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getImposto() {
        return imposto;
    }

    public void setImposto(Double imposto) {
        this.imposto = imposto;
    }

    public Double getCustoVariavel() {
        return custoVariavel;
    }

    public void setCustoVariavel(Double custoVariavel) {
        this.custoVariavel = custoVariavel;
    }

    public Double getCustoFixo() {
        return custoFixo;
    }

    public void setCustoFixo(Double custoFixo) {
        this.custoFixo = custoFixo;
    }

    public Double getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(Double margemLucro) {
        this.margemLucro = margemLucro;
    }

    public LocalDate getDataProcessamento() {
        return dataProcessamento;
    }

    public void setDataProcessamento(LocalDate dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    @Override
    public String toString() {
        return "Custo ID: " + id + " (Data: " + dataProcessamento + ")";
    }
}
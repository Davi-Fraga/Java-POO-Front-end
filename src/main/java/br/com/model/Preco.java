package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Preco {

    private Long id;
    private BigDecimal valor;
    private LocalDateTime dataHoraAlteracao;

    public Preco() {
    }

    public Preco(Long id, BigDecimal valor, LocalDateTime dataHoraAlteracao) {
        this.id = id;
        this.valor = valor;
        this.dataHoraAlteracao = dataHoraAlteracao;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHoraAlteracao() {
        return dataHoraAlteracao;
    }

    public void setDataHoraAlteracao(LocalDateTime dataHoraAlteracao) {
        this.dataHoraAlteracao = dataHoraAlteracao;
    }
}
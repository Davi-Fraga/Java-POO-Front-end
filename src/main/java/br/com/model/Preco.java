package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Preco {

    private Long id;
    private BigDecimal valor;
    private LocalDateTime dataHoraAlteracao; // Nome do atributo corrigido

    // Construtor padrão (necessário para desserialização do Jackson)
    public Preco() {
    }

    // Construtor com todos os campos
    public Preco(Long id, BigDecimal valor, LocalDateTime dataHoraAlteracao) {
        this.id = id;
        this.valor = valor;
        this.dataHoraAlteracao = dataHoraAlteracao;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    // Getter corrigido
    public LocalDateTime getDataHoraAlteracao() {
        return dataHoraAlteracao;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    // Setter corrigido
    public void setDataHoraAlteracao(LocalDateTime dataHoraAlteracao) {
        this.dataHoraAlteracao = dataHoraAlteracao;
    }

    @Override
    public String toString() {
        return "Preco{" +
               "id=" + id +
               ", valor=" + valor +
               ", dataHoraAlteracao=" + dataHoraAlteracao +
               '}';
    }
}

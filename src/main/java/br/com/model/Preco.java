package br.com.model;

import br.com.model.enums.TipoPreco; // Assumindo que TipoPreco é um enum neste pacote
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Preco {

    private Long id;
    private BigDecimal valor;
    @JsonProperty("dataAlteracao")
    private LocalDate dataAlteracao;
    @JsonProperty("horaAlteracao")
    private LocalTime horaAlteracao;
    @JsonProperty("tipoPreco")
    private TipoPreco tipoPreco; // Novo campo para o tipo de preço

    // Construtor padrão (necessário para desserialização do Jackson)
    public Preco() {
    }

    // Construtor com todos os campos (ajustado)
    public Preco(Long id, BigDecimal valor, LocalDate dataAlteracao, LocalTime horaAlteracao, TipoPreco tipoPreco) {
        this.id = id;
        this.valor = valor;
        this.dataAlteracao = dataAlteracao;
        this.horaAlteracao = horaAlteracao;
        this.tipoPreco = tipoPreco;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataAlteracao() {
        return dataAlteracao;
    }

    public LocalTime getHoraAlteracao() {
        return horaAlteracao;
    }

    public TipoPreco getTipoPreco() {
        return tipoPreco;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setDataAlteracao(LocalDate dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public void setHoraAlteracao(LocalTime horaAlteracao) {
        this.horaAlteracao = horaAlteracao;
    }

    public void setTipoPreco(TipoPreco tipoPreco) {
        this.tipoPreco = tipoPreco;
    }

    @Override
    public String toString() {
        return "Preco{" +
               "id=" + id +
               ", valor=" + valor +
               ", dataAlteracao=" + dataAlteracao +
               ", horaAlteracao=" + horaAlteracao +
               ", tipoPreco=" + tipoPreco +
               '}';
    }
}

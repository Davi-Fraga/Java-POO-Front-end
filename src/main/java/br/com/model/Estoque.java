package br.com.model;

import br.com.model.enums.TipoEstoque; // Importar o enum TipoEstoque
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty; // Importar esta anotação

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Estoque {

    private Long id;
    private String produto; // Assumindo que 'produto' é uma String, ou pode ser um objeto Produto
    private BigDecimal quantidade;
    private String localTanque;
    private String localCarregamento;
    private String localEndereco;
    @JsonProperty("loteFabricacao") // Adicionar esta anotação
    private String localFabricacao; // Corrigido para 'localFabricacao'
    private LocalDate dataValidade;
    private TipoEstoque tipoEstoque; // Alterado para o tipo enum TipoEstoque

    // Construtor padrão (necessário para desserialização do Jackson)
    public Estoque() {
    }

    // Construtor com todos os campos (original)
    public Estoque(Long id, String produto, BigDecimal quantidade, String localTanque, String localCarregamento,
                   String localEndereco, String localFabricacao, LocalDate dataValidade, TipoEstoque tipoEstoque) { // tipoEstoque alterado
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.localTanque = localTanque;
        this.localCarregamento = localCarregamento;
        this.localEndereco = localEndereco;
        this.localFabricacao = localFabricacao;
        this.dataValidade = dataValidade;
        this.tipoEstoque = tipoEstoque;
    }

    // NOVO CONSTRUTOR para corresponder à chamada em EstoqueFrame.java
    // Estoque(null, quantidade, localTanque, localEndereco, lote, dataValidade, tipoEstoque)
    public Estoque(Long id, BigDecimal quantidade, String localTanque, String localEndereco,
                   String loteFabricacao, LocalDate dataValidade, TipoEstoque tipoEstoque) {
        this.id = id;
        this.produto = ""; // 'produto' não é passado pelo frame, inicializa como vazio
        this.quantidade = quantidade;
        this.localTanque = localTanque;
        this.localCarregamento = null; // 'localCarregamento' não é passado pelo frame
        this.localEndereco = localEndereco;
        this.localFabricacao = loteFabricacao; // 'lote' do frame mapeia para 'localFabricacao'
        this.dataValidade = dataValidade;
        this.tipoEstoque = tipoEstoque;
    }


    // Getters
    public Long getId() {
        return id;
    }

    public String getProduto() {
        return produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public String getLocalTanque() {
        return localTanque;
    }

    public String getLocalCarregamento() {
        return localCarregamento;
    }

    public String getLocalEndereco() {
        return localEndereco;
    }

    @JsonProperty("loteFabricacao") // Adicionar esta anotação ao getter
    public String getLocalFabricacao() { // Getter corrigido
        return localFabricacao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public TipoEstoque getTipoEstoque() { // Getter retorna TipoEstoque enum
        return tipoEstoque;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public void setLocalTanque(String localTanque) {
        this.localTanque = localTanque;
    }

    public void setLocalCarregamento(String localCarregamento) {
        this.localCarregamento = localCarregamento;
    }

    public void setLocalEndereco(String localEndereco) {
        this.localEndereco = localEndereco;
    }

    @JsonProperty("loteFabricacao") // Adicionar esta anotação ao setter
    public void setLocalFabricacao(String localFabricacao) { // Setter corrigido
        this.localFabricacao = localFabricacao;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public void setTipoEstoque(TipoEstoque tipoEstoque) { // Setter aceita TipoEstoque enum
        this.tipoEstoque = tipoEstoque;
    }

    @Override
    public String toString() {
        return "Estoque{" +
                "id=" + id +
                ", produto='" + produto + '\'' +
                ", quantidade=" + quantidade +
                ", localTanque='" + localTanque + '\'' +
                ", localCarregamento='" + localCarregamento + '\'' +
                ", localEndereco='" + localEndereco + '\'' +
                ", localFabricacao='" + localFabricacao + '\'' +
                ", dataValidade=" + dataValidade +
                ", tipoEstoque=" + tipoEstoque +
                '}';
    }
}

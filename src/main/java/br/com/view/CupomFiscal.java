package br.com.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CupomFiscal {

    private final String nomeBico;
    private final String tipoCombustivel;
    private final double precoLitro;
    private final double volume;
    private final double total;
    private final String formaPagamento;

    public CupomFiscal(String nomeBico, String tipoCombustivel, double precoLitro, double volume, double total, String formaPagamento) {
        this.nomeBico = nomeBico;
        this.tipoCombustivel = tipoCombustivel;
        this.precoLitro = precoLitro;
        this.volume = volume;
        this.total = total;
        this.formaPagamento = formaPagamento;
    }

    public String gerarTextoCupom() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Cabeçalho
        sb.append("POSTO DE COMBUSTIVEL XPTO LTDA\n");
        sb.append("CNPJ: 12.345.678/0001-99\n");
        sb.append("IE: 123.456.789.111\n");
        sb.append("Av. Principal, 123 - Centro, Cidade - UF\n");
        sb.append("------------------------------------------------\n");
        sb.append("          CUPOM FISCAL ELETRÔNICO         \n");
        sb.append("------------------------------------------------\n");

        // Detalhes da Transação
        sb.append("COO: ").append(String.format("%06d", (int) (Math.random() * 100000))).append("\n");
        sb.append("Data/Hora: ").append(dtf.format(LocalDateTime.now())).append("\n");
        sb.append("Bico: ").append(nomeBico).append("\n");
        sb.append("------------------------------------------------\n");

        // Detalhes do Item
        sb.append("DESCRIÇÃO      QTD      UN      VL UNIT R$      VL TOTAL R$\n");
        sb.append(String.format("%-15s %-7.2f %-7s %-15.2f %-15.2f\n",
                tipoCombustivel, volume, "LT", precoLitro, total));
        sb.append("------------------------------------------------\n");

        // Rodapé
        sb.append(String.format("Total Bruto: R$ %.2f\n", total));
        sb.append(String.format("Valor Total da Nota: R$ %.2f\n", total));
        sb.append("Forma de Pagamento: ").append(formaPagamento).append("\n");
        sb.append("\n");
        sb.append("Consulte o QR Code da NFC-e\n");
        sb.append("         [ESPAÇO PARA QR CODE]         \n");
        sb.append("\n");
        sb.append("Tributos incidentes (Lei Federal 12.741/2012)\n");

        return sb.toString();
    }
}

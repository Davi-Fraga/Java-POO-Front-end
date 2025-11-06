package br.com.view;

import br.com.model.Bico;
import br.com.service.BicoApiService;
import br.com.service.IBicoService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class AbastecimentoFrame extends JFrame {

    private final IBicoService bicoService;

    public AbastecimentoFrame(String username) {
        setTitle("Central de Abastecimento");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.bicoService = new BicoApiService();

        // --- Cabeçalho ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Painel de Controle de Abastecimento", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIStyle.AZUL_TITULO);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton sairButton = new JButton("Sair");
        sairButton.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });
        headerPanel.add(sairButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Painel dos Bicos ---
        JPanel bicosPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // GridLayout dinâmico
        bicosPanel.setOpaque(false);
        bicosPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        carregarBicos(bicosPanel);

        add(bicosPanel, BorderLayout.CENTER);
    }

    private void carregarBicos(JPanel container) {
        try {
            List<Bico> bicos = bicoService.listarBicos();
            for (Bico bico : bicos) {
                String nomeProduto = "N/A";
                double preco = 0.0;
                if (bico.getProduto() != null) {
                    nomeProduto = bico.getProduto().getNome();
                    // Assumindo que o modelo Produto terá um campo de preço, por exemplo, getPrecoVenda()
                    if (bico.getProduto().getPrecoVenda() != null) {
                        preco = bico.getProduto().getPrecoVenda().doubleValue();
                    }
                }
                container.add(new BicoPanel(bico.getNome(), nomeProduto, preco, bico.getStatus()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar os bicos: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AbastecimentoFrame("Operador_01").setVisible(true);
        });
    }
}

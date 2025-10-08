package br.com.view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela principal que serve como menu para acessar os outros cadastros.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("PDV - Sistema de Gerenciamento de Posto");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fechar esta janela encerra a aplicação
        setLocationRelativeTo(null);

        // Painel principal com um layout de grade para organizar os botões
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Adiciona uma margem

        // --- Criação dos Botões ---
        JButton pessoasButton = createMenuButton("Gerenciar Pessoas");
        JButton produtosButton = createMenuButton("Gerenciar Produtos");
        JButton estoqueButton = createMenuButton("Gerenciar Estoque");
        JButton custosButton = createMenuButton("Gerenciar Custos");
        JButton precosButton = createMenuButton("Gerenciar Preços");
        JButton contatosButton = createMenuButton("Gerenciar Contatos");

        // --- Adição dos Botões ao Painel ---
        mainPanel.add(pessoasButton);
        mainPanel.add(produtosButton);
        mainPanel.add(estoqueButton);
        mainPanel.add(custosButton);
        mainPanel.add(precosButton);
        mainPanel.add(contatosButton);

        // --- Ações dos Botões ---
        pessoasButton.addActionListener(e -> {
            PessoaFrame pessoaFrame = new PessoaFrame();
            pessoaFrame.setVisible(true);
        });

        produtosButton.addActionListener(e -> {
            ProdutoFrame produtoFrame = new ProdutoFrame();
            produtoFrame.setVisible(true);
        });

        estoqueButton.addActionListener(e -> {
            EstoqueFrame estoqueFrame = new EstoqueFrame();
            estoqueFrame.setVisible(true);
        });

        custosButton.addActionListener(e -> {
            CustoFrame custoFrame = new CustoFrame();
            custoFrame.setVisible(true);
        });

        precosButton.addActionListener(e -> {
            PrecoFrame precoFrame = new PrecoFrame();
            precoFrame.setVisible(true);
        });

        contatosButton.addActionListener(e -> {
            ContatoFrame contatoFrame = new ContatoFrame();
            contatoFrame.setVisible(true);
        });

        // Adiciona o painel principal à janela
        add(mainPanel);
    }

    /**
     * Método auxiliar para criar e estilizar os botões do menu.
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        // Garante que a UI seja executada na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Tenta usar um Look and Feel mais moderno se disponível
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Se o Nimbus não estiver disponível, usa o padrão do sistema
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
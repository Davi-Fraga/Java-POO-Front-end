package br.com.view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela que serve como menu para a área de Gerência.
 */
public class MainFrame extends JFrame {

    public MainFrame(String username) {
        setTitle("Área de Gerência - PDV");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(0, 20));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Título ---
        JLabel titleLabel = new JLabel("Painel de Gerência", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIStyle.AZUL_TITULO);
        add(titleLabel, BorderLayout.NORTH);

        // --- Painel de Botões ---
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        mainPanel.setOpaque(false);

        // --- Criação dos Botões ---
        JButton pessoasButton = createMenuButton("Gerenciar Pessoas");
        JButton produtosButton = createMenuButton("Gerenciar Produtos");
        JButton estoqueButton = createMenuButton("Gerenciar Estoque");
        JButton custosButton = createMenuButton("Gerenciar Custos");
        JButton precosButton = createMenuButton("Gerenciar Preços");
        JButton contatosButton = createMenuButton("Gerenciar Contatos");
        JButton acessosButton = createMenuButton("Gerenciar Acessos");

        // --- Adição dos Botões ao Painel ---
        mainPanel.add(pessoasButton);
        mainPanel.add(produtosButton);
        mainPanel.add(estoqueButton);
        mainPanel.add(custosButton);
        mainPanel.add(precosButton);
        mainPanel.add(contatosButton);
        mainPanel.add(acessosButton);

        // --- Ações dos Botões ---
        pessoasButton.addActionListener(e -> new PessoaFrame().setVisible(true));
        produtosButton.addActionListener(e -> new ProdutoFrame().setVisible(true));
        estoqueButton.addActionListener(e -> new EstoqueFrame().setVisible(true));
        custosButton.addActionListener(e -> new CustoFrame().setVisible(true));
        precosButton.addActionListener(e -> new PrecoFrame().setVisible(true));
        contatosButton.addActionListener(e -> new ContatoFrame().setVisible(true));
        acessosButton.addActionListener(e -> new AcessoFrame().setVisible(true));

        // --- Wrapper Panel to center the mainPanel ---
        JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
        centerWrapperPanel.setOpaque(false);
        centerWrapperPanel.add(mainPanel);

        add(centerWrapperPanel, BorderLayout.CENTER);

        // --- Painel do Botão Sair ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton backButton = new JButton("Sair");
        backButton.setPreferredSize(new Dimension(120, 40));

        // Ação de Logout: fecha a tela atual e abre a tela de login
        backButton.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 60));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        return button;
    }
}

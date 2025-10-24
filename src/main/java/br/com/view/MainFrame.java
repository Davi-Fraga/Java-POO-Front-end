package br.com.view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela principal que serve como menu para acessar os outros cadastros.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("PDV - Sistema de Gerenciamento de Posto");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(0, 20));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Título ---
        JLabel titleLabel = new JLabel("Menu Principal", SwingConstants.CENTER);
        UIStyle.estilizarTitulo(titleLabel);
        titleLabel.setFont(UIStyle.FONTE_TITULO.deriveFont(Font.BOLD, 24f));
        add(titleLabel, BorderLayout.NORTH);

        // --- Painel de Botões ---
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        mainPanel.setOpaque(false);

        // --- Criação dos Botões ---
        JButton pessoasButton = createMenuButton("Gerenciar Pessoas", "/icons/user.png");
        JButton produtosButton = createMenuButton("Gerenciar Produtos", "/icons/product.png");
        JButton estoqueButton = createMenuButton("Gerenciar Estoque", "/icons/stock.png");
        JButton custosButton = createMenuButton("Gerenciar Custos", "/icons/costs.png");
        JButton precosButton = createMenuButton("Gerenciar Preços", "/icons/price.png");
        JButton contatosButton = createMenuButton("Gerenciar Contatos", "/icons/contact.png");

        // --- Adição dos Botões ao Painel ---
        mainPanel.add(pessoasButton);
        mainPanel.add(produtosButton);
        mainPanel.add(estoqueButton);
        mainPanel.add(custosButton);
        mainPanel.add(precosButton);
        mainPanel.add(contatosButton);

        // --- Ações dos Botões ---
        pessoasButton.addActionListener(e -> new PessoaFrame().setVisible(true));
        produtosButton.addActionListener(e -> new ProdutoFrame().setVisible(true));
        estoqueButton.addActionListener(e -> new EstoqueFrame().setVisible(true));
        custosButton.addActionListener(e -> new CustoFrame().setVisible(true));
        precosButton.addActionListener(e -> new PrecoFrame().setVisible(true));
        contatosButton.addActionListener(e -> new ContatoFrame().setVisible(true));

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text, String iconPath) {
        ImageIcon icon = null;
        try {
            // Tenta carregar o ícone e redimensioná-lo
            Image img = new ImageIcon(getClass().getResource(iconPath)).getImage();
            icon = new ImageIcon(img.getScaledInstance(28, 28, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            System.err.println("Ícone não encontrado: " + iconPath);
        }

        JButton button = new JButton(text, icon);
        UIStyle.estilizarBotaoPrimario(button);
        button.setPreferredSize(new Dimension(200, 60));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15);
        return button;
    }

    public static void main(String[] args) {
        // Inicializa o Look and Feel personalizado
        UIStyle.inicializar();

        // Garante que a UI seja executada na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

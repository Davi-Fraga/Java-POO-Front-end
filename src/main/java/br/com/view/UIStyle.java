package br.com.view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Classe utilitária para centralizar a estilização da UI da aplicação.
 */
public class UIStyle {

    // --- NOVA PALETA DE CORES ---
    public static final Color FUNDO_JANELA = new Color(0xF5, 0xF7, 0xFA);      // #F5F7FA
    public static final Color AZUL_PRINCIPAL = new Color(0x15, 0x65, 0xC0);   // #1565C0
    public static final Color AZUL_HOVER = new Color(0x19, 0x76, 0xD2);      // #1976D2
    public static final Color AZUL_TITULO = new Color(0x00, 0x4A, 0xAD);      // #004AAD
    public static final Color TEXTO_BOTAO = Color.WHITE;                     // #FFFFFF
    public static final Color TEXTO_NORMAL = new Color(0x1E, 0x1E, 0x1E);      // #1E1E1E
    public static final Color BORDA_SUTIL = new Color(224, 224, 224);
    public static final Color FUNDO_PAINEL = Color.WHITE;                 // #FFFFFF
    public static final Color ZEBRA_TABELA = new Color(249, 250, 251);    // #F9FAFB

    // Cores adicionais
    public static final Color VERDE_SUCESSO = new Color(0, 128, 0); // Verde escuro
    public static final Color LARANJA_ALERTA = new Color(255, 165, 0); // Laranja
    public static final Color VERMELHO_ERRO = new Color(220, 20, 60); // Vermelho carmesim


    // --- FONTES ---
    public static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 14);

    /**
     * Inicializa o Look and Feel FlatLaf Light e personalizações globais.
     * Deve ser chamado no início do método main.
     */
    public static void inicializar() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            // --- Personalizações globais do UIManager ---
            int borderRadius = 12;
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.arc", borderRadius);
            UIManager.put("Button.arc", borderRadius);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("Component.focusColor", AZUL_PRINCIPAL);
            UIManager.put("ProgressBar.arc", borderRadius);

            // Estilo dos Botões
            UIManager.put("Button.background", AZUL_PRINCIPAL);
            UIManager.put("Button.foreground", TEXTO_BOTAO);
            UIManager.put("Button.hoverBackground", AZUL_HOVER);
            UIManager.put("Button.focusedBackground", AZUL_HOVER.darker());
            UIManager.put("Button.font", FONTE_BOTAO);
            UIManager.put("Button.border", new EmptyBorder(8, 18, 8, 18));

            // Cores de Seleção
            UIManager.put("Table.selectionBackground", AZUL_HOVER);
            UIManager.put("Table.selectionForeground", TEXTO_BOTAO);
            UIManager.put("TextField.selectionBackground", AZUL_PRINCIPAL);
            UIManager.put("TextField.selectionForeground", TEXTO_BOTAO);
            UIManager.put("TextArea.selectionBackground", AZUL_PRINCIPAL);
            UIManager.put("TextArea.selectionForeground", TEXTO_BOTAO);

            // Fontes
            UIManager.put("defaultFont", FONTE_PADRAO);

        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Falha ao inicializar o Look and Feel FlatLaf: " + e.getMessage());
        }
    }

    /**
     * Estiliza um botão primário. A maior parte do estilo vem do UIManager.
     */
    public static void estilizarBotaoPrimario(JButton button) {
        // O estilo principal é definido globalmente no método inicializar().
    }
    
    /**
     * Estiliza um botão como secundário (não especificado no novo design).
     */
    public static void estilizarBotaoSecundario(JButton button) {
        button.setBackground(new Color(0x6c, 0x75, 0x7d)); // Cinza
        button.setForeground(Color.WHITE);
        button.setFont(FONTE_BOTAO);
        button.setFocusPainted(false);
    }

    /**
     * Estiliza uma JTable com o novo design.
     */
    public static void estilizarTabela(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(AZUL_PRINCIPAL);
        header.setForeground(TEXTO_BOTAO);
        header.setFont(FONTE_TITULO.deriveFont(Font.BOLD, 14f));
        header.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? FUNDO_PAINEL : ZEBRA_TABELA);
                }
                c.setForeground(TEXTO_NORMAL);
                ((JComponent) c).setBorder(new EmptyBorder(0, 10, 0, 10)); // Padding nas células
                return c;
            }
        });

        table.setRowHeight(32);
        table.setFont(FONTE_PADRAO);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
    }

    /**
     * Estiliza um painel com borda suave e preenchimento.
     */
    public static void estilizarPainel(JPanel panel) {
        panel.setBackground(FUNDO_PAINEL);
        panel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDA_SUTIL, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
    }

    /**
     * Estiliza um JLabel como um título de seção.
     */
    public static void estilizarTitulo(JLabel label) {
        label.setFont(FONTE_TITULO.deriveFont(22f));
        label.setForeground(AZUL_TITULO);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Estiliza um JTextField.
     */
    public static void estilizarCampoDeTexto(JTextField field) {
        field.setFont(FONTE_PADRAO);
        field.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(BORDA_SUTIL, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
    }

    /**
     * Estiliza um JComboBox.
     */
    public static void estilizarComboBox(JComboBox<?> combo) {
        combo.setFont(FONTE_PADRAO);
        combo.setBackground(Color.WHITE);
    }
}

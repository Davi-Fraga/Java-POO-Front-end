package br.com.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class BicoPanel extends JPanel {

    private final String nomeBico;
    private final String tipoCombustivel;
    private final double precoLitro;
    private final JTextField volumeField;
    private final JTextField quantiaField;
    private final ButtonGroup pagamentoGroup;
    private final JButton confirmarButton;
    private final JLabel statusLabel;

    private boolean isEditing = false; // Flag para evitar loop infinito nos listeners

    public BicoPanel(String nomeBico, String tipoCombustivel, double precoLitro, String status) {
        this.nomeBico = nomeBico;
        this.tipoCombustivel = tipoCombustivel;
        this.precoLitro = precoLitro;

        setBackground(UIStyle.FUNDO_PAINEL);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        TitledBorder titledBorder = BorderFactory.createTitledBorder(nomeBico);
        titledBorder.setTitleColor(UIStyle.AZUL_TITULO);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                new EmptyBorder(5, 10, 5, 10)
        ));

        // Status e Informações do Combustível
        statusLabel = createLabel("Status: " + status, SwingConstants.LEFT);
        statusLabel.setForeground(UIStyle.TEXTO_NORMAL);
        add(statusLabel);
        JLabel infoLabel = createLabel(tipoCombustivel + " - R$ " + String.format("%.2f", precoLitro) + "/L", SwingConstants.LEFT);
        infoLabel.setForeground(UIStyle.AZUL_PRINCIPAL);
        add(infoLabel);
        add(Box.createVerticalStrut(10));

        // Campos de Entrada (Litragem e Valor)
        volumeField = new JTextField();
        quantiaField = new JTextField();
        quantiaField.setHorizontalAlignment(JTextField.RIGHT);

        add(createInputPanel("Litragem (V)", volumeField));
        add(Box.createVerticalStrut(5));
        add(createInputPanel("Valor (R$)", quantiaField));
        add(Box.createVerticalStrut(15));

        // Opções de Pagamento
        pagamentoGroup = new ButtonGroup();
        JPanel pagamentoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pagamentoPanel.setOpaque(false);
        String[] opcoes = {"PIX", "Débito", "Crédito"};
        for (String opcao : opcoes) {
            JRadioButton rb = new JRadioButton(opcao);
            rb.setOpaque(false);
            rb.setForeground(UIStyle.TEXTO_NORMAL);
            pagamentoGroup.add(rb);
            pagamentoPanel.add(rb);
            rb.addActionListener(e -> validarCampos());
        }
        add(pagamentoPanel);
        add(Box.createVerticalStrut(20));

        // Botão de Confirmação
        confirmarButton = new JButton("Confirmar Abastecimento");
        confirmarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmarButton.setEnabled(false);
        add(confirmarButton);

        // Listeners
        addDocumentListeners();
        confirmarButton.addActionListener(e -> processarPagamento());
    }

    private JLabel createLabel(String text, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createInputPanel(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 2));
        panel.setOpaque(false);
        JLabel jlabel = new JLabel(label);
        jlabel.setForeground(UIStyle.TEXTO_NORMAL);
        panel.add(jlabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        Dimension panelSize = new Dimension(Integer.MAX_VALUE, 40);
        panel.setPreferredSize(panelSize);
        panel.setMaximumSize(panelSize);

        return panel;
    }

    private void addDocumentListeners() {
        volumeField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleVolumeChange(); }
            public void removeUpdate(DocumentEvent e) { handleVolumeChange(); }
            public void changedUpdate(DocumentEvent e) { handleVolumeChange(); }
        });

        quantiaField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleQuantiaChange(); }
            public void removeUpdate(DocumentEvent e) { handleQuantiaChange(); }
            public void changedUpdate(DocumentEvent e) { handleQuantiaChange(); }
        });
    }

    private void handleVolumeChange() {
        if (isEditing) return; // Previne loop

        SwingUtilities.invokeLater(() -> {
            try {
                String text = volumeField.getText();
                if (text == null || text.trim().isEmpty()) {
                    isEditing = true;
                    quantiaField.setText("");
                } else {
                    double volume = Double.parseDouble(text.replace(',', '.'));
                    double quantia = volume * precoLitro;
                    isEditing = true;
                    quantiaField.setText(String.format("%.2f", quantia).replace('.', ','));
                }
            } catch (NumberFormatException ex) {
                // Ignora erros de formato enquanto o usuário digita
            } finally {
                isEditing = false;
                validarCampos();
            }
        });
    }

    private void handleQuantiaChange() {
        if (isEditing) return; // Previne loop

        SwingUtilities.invokeLater(() -> {
            try {
                String text = quantiaField.getText();
                if (text == null || text.trim().isEmpty()) {
                    isEditing = true;
                    volumeField.setText("");
                } else {
                    if (precoLitro > 0) {
                        double quantia = Double.parseDouble(text.replace(',', '.'));
                        double volume = quantia / precoLitro;
                        isEditing = true;
                        volumeField.setText(String.format("%.3f", volume).replace('.', ',')); // 3 casas para litros
                    } else {
                        isEditing = true;
                        volumeField.setText("");
                    }
                }
            } catch (NumberFormatException ex) {
                // Ignora erros de formato enquanto o usuário digita
            } finally {
                isEditing = false;
                validarCampos();
            }
        });
    }

    private void validarCampos() {
        boolean isValorValido = isFieldValid(quantiaField);
        boolean isPagamentoSelected = pagamentoGroup.getSelection() != null;
        confirmarButton.setEnabled(isValorValido && isPagamentoSelected);
    }

    private boolean isFieldValid(JTextField field) {
        try {
            String text = field.getText().replace(',', '.');
            return !text.trim().isEmpty() && Double.parseDouble(text) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void processarPagamento() {
        int escolha = JOptionPane.showOptionDialog(this,
                "Deseja imprimir o Cupom Fiscal referente a esta transação?",
                "Impressão de Comprovante",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"Sim", "Não"}, "Sim");

        if (escolha == JOptionPane.YES_OPTION) {
            imprimirCupom();
        }

        resetarBico();
    }

    private void imprimirCupom() {
        try {
            String volumeText = volumeField.getText().replace(',', '.');
            String quantiaText = quantiaField.getText().replace(',', '.');

            double volume = volumeText.isEmpty() ? 0.0 : Double.parseDouble(volumeText);
            double total = quantiaText.isEmpty() ? 0.0 : Double.parseDouble(quantiaText);
            String formaPagamento = getSelectedButtonText(pagamentoGroup);

            if (total <= 0) {
                JOptionPane.showMessageDialog(this, "Não é possível gerar cupom para um abastecimento sem valor.", "Valor Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            CupomFiscal cupom = new CupomFiscal(nomeBico, tipoCombustivel, precoLitro, volume, total, formaPagamento);
            String textoCupom = cupom.gerarTextoCupom();

            JTextArea textArea = new JTextArea(textoCupom);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 500));

            JOptionPane.showMessageDialog(this, scrollPane, "Cupom Fiscal", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar cupom: valor inválido nos campos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetarBico() {
        volumeField.setText("");
        quantiaField.setText("");
        pagamentoGroup.clearSelection();
        validarCampos();
    }

    private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (java.util.Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return "Não definido";
    }
}

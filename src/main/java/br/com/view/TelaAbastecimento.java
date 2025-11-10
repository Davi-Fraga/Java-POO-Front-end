
package br.com.view;

import br.com.model.Abastecimento;
import br.com.model.Produto;
import br.com.service.AbastecimentoApiServiceMock;
import br.com.service.AbastecimentoService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TelaAbastecimento extends JFrame {

    private final List<PumpPanel> pumpPanels = new ArrayList<>();
    private final ButtonGroup paymentGroup = new ButtonGroup();
    private final AbastecimentoService abastecimentoService = new AbastecimentoApiServiceMock();
    private JButton btnSair;

    public TelaAbastecimento() {
        setTitle("ABASTECIMENTO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#eeeeee"));

        // Painel para o botão Sair
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(Color.decode("#eeeeee"));
        btnSair = new JButton("Sair");
        topPanel.add(btnSair);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.decode("#eeeeee"));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("ABASTECE-ME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        mainPanel.add(titleLabel, gbc);

        JLabel centralTitleLabel = new JLabel("Central de Abastecimento", SwingConstants.CENTER);
        centralTitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 10, 10);
        mainPanel.add(centralTitleLabel, gbc);

        JPanel pumpsContainerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pumpsContainerPanel.setBackground(Color.decode("#eeeeee"));
        PumpPanel gasolinaPanel = new PumpPanel("GASOLINA", 5.00);
        PumpPanel etanolPanel = new PumpPanel("ETANOL", 4.00);
        PumpPanel dieselPanel = new PumpPanel("DIESEL", 6.00);
        pumpPanels.add(gasolinaPanel);
        pumpPanels.add(etanolPanel);
        pumpPanels.add(dieselPanel);
        pumpsContainerPanel.add(gasolinaPanel);
        pumpsContainerPanel.add(etanolPanel);
        pumpsContainerPanel.add(dieselPanel);
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(pumpsContainerPanel, gbc);

        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Forma de Pagamento"));
        paymentPanel.setBackground(Color.decode("#eeeeee"));
        JRadioButton pixRadio = new JRadioButton("Pix");
        pixRadio.setActionCommand("Pix");
        pixRadio.setBackground(Color.decode("#eeeeee"));
        JRadioButton debitoRadio = new JRadioButton("Débito");
        debitoRadio.setActionCommand("Débito");
        debitoRadio.setBackground(Color.decode("#eeeeee"));
        JRadioButton creditoRadio = new JRadioButton("Crédito");
        creditoRadio.setActionCommand("Crédito");
        creditoRadio.setBackground(Color.decode("#eeeeee"));
        paymentGroup.add(pixRadio);
        paymentGroup.add(debitoRadio);
        paymentGroup.add(creditoRadio);
        pixRadio.setSelected(true);
        paymentPanel.add(pixRadio);
        paymentPanel.add(debitoRadio);
        paymentPanel.add(creditoRadio);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(paymentPanel, gbc);

        JButton confirmButton = new JButton("CONFIRMAR COMPRA");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.ipadx = 50;
        gbc.ipady = 10;
        mainPanel.add(confirmButton, gbc);

        confirmButton.addActionListener(e -> onConfirmCompra());

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                TelaLogin telaLogin = new TelaLogin();
                telaLogin.setVisible(true);
            }
        });

        add(mainPanel, BorderLayout.CENTER);
    }

    private void onConfirmCompra() {
        PumpPanel activePump = null;
        for (PumpPanel pump : pumpPanels) {
            if (pump.isFilled()) {
                if (activePump != null) {
                    JOptionPane.showMessageDialog(this, "Por favor, preencha apenas uma bomba por vez.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                activePump = pump;
            }
        }

        if (activePump == null) {
            JOptionPane.showMessageDialog(this, "Nenhum valor foi preenchido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ButtonModel selectedPayment = paymentGroup.getSelection();
        if (selectedPayment == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma forma de pagamento.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Produto produto = new Produto();
        produto.setNome(activePump.getFuelName());
        Abastecimento dadosDaTela = new Abastecimento(produto, activePump.getLitros(), activePump.getPrecoUnitario(), activePump.getValorTotal(), new Date());

        abastecimentoService.salvar(dadosDaTela);

        JOptionPane.showMessageDialog(this, "Venda salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        int response = JOptionPane.showConfirmDialog(this, "Deseja Imprimir o Comprovante?", "Confirmação de Impressão", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.OK_OPTION) {
            try {
                CupomFiscal cupomFiscal = new CupomFiscal(
                        "Bico 1", // Exemplo, idealmente viria do PumpPanel
                        dadosDaTela.getProduto().getNome(),
                        dadosDaTela.getPrecoLitro(),
                        dadosDaTela.getQuantidade(),
                        dadosDaTela.getTotal(),
                        selectedPayment.getActionCommand()
                );
                String textoCupom = cupomFiscal.gerarTextoCupom();

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(new CupomFiscalPrinter(textoCupom));

                if (job.printDialog()) {
                    job.print();
                }
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao imprimir: " + ex.getMessage(), "Erro de Impressão", JOptionPane.ERROR_MESSAGE);
            }
        }

        for (PumpPanel pump : pumpPanels) {
            pump.clearFields();
        }
    }

    private static class PumpPanel extends JPanel {
        private final String fuelName;
        private final double precoUnitario;
        private final JTextField litrosField = new JTextField();
        private final JTextField valorAPagarField = new JTextField();
        private final JLabel valorTotalDisplay = new JLabel("R$ 0,00");
        private final AtomicBoolean isUpdating = new AtomicBoolean(false);

        public PumpPanel(String fuelName, double precoUnitario) {
            this.fuelName = fuelName;
            this.precoUnitario = precoUnitario;
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
            setPreferredSize(new Dimension(220, 280));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel fuelLabel = new JLabel(fuelName, SwingConstants.CENTER);
            fuelLabel.setFont(new Font("Arial", Font.BOLD, 18));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            add(fuelLabel, gbc);
            JLabel precoUnitarioLabel = new JLabel(String.format("R$ %.2f / L", precoUnitario), SwingConstants.CENTER);
            precoUnitarioLabel.setFont(new Font("Arial", Font.BOLD, 16));
            precoUnitarioLabel.setForeground(Color.BLUE);
            gbc.gridy = 1;
            add(precoUnitarioLabel, gbc);
            gbc.gridwidth = 1;
            gbc.gridy = 2;
            add(new JLabel("LITROS:"), gbc);
            gbc.gridx = 1;
            add(litrosField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            add(new JLabel("PAGAR R$:"), gbc);
            gbc.gridx = 1;
            add(valorAPagarField, gbc);
            JLabel totalLabel = new JLabel("VALOR TOTAL", SwingConstants.CENTER);
            totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 5, 5, 5);
            add(totalLabel, gbc);
            valorTotalDisplay.setFont(new Font("Arial", Font.BOLD, 20));
            valorTotalDisplay.setForeground(new Color(0, 100, 0));
            gbc.gridy = 5;
            gbc.insets = new Insets(0, 5, 5, 5);
            add(valorTotalDisplay, gbc);
            setupListeners();
        }

        private void setupListeners() {
            DocumentListener listener = new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    update(e);
                }

                public void removeUpdate(DocumentEvent e) {
                    update(e);
                }

                public void changedUpdate(DocumentEvent e) {
                    update(e);
                }

                private void update(DocumentEvent e) {
                    if (isUpdating.get()) return;
                    SwingUtilities.invokeLater(() -> {
                        if (isUpdating.compareAndSet(false, true)) {
                            try {
                                if (e.getDocument() == litrosField.getDocument()) {
                                    updateFromLitros();
                                } else if (e.getDocument() == valorAPagarField.getDocument()) {
                                    updateFromValor();
                                }
                            } finally {
                                isUpdating.set(false);
                            }
                        }
                    });
                }
            };
            litrosField.getDocument().addDocumentListener(listener);
            valorAPagarField.getDocument().addDocumentListener(listener);
        }

        private void updateFromLitros() {
            String text = litrosField.getText().replace(',', '.');
            if (!text.isEmpty()) {
                try {
                    double litros = Double.parseDouble(text);
                    double valorTotal = litros * precoUnitario;
                    valorAPagarField.setText(String.format("%.2f", valorTotal));
                    valorTotalDisplay.setText(String.format("R$ %.2f", valorTotal));
                } catch (NumberFormatException ex) {
                    clearFieldsOnException();
                }
            } else {
                clearFieldsOnEmpty();
            }
        }

        private void updateFromValor() {
            String text = valorAPagarField.getText().replace(',', '.');
            if (!text.isEmpty()) {
                try {
                    double valorTotal = Double.parseDouble(text);
                    double litros = valorTotal / precoUnitario;
                    litrosField.setText(String.format("%.2f", litros));
                    valorTotalDisplay.setText(String.format("R$ %.2f", valorTotal));
                } catch (NumberFormatException ex) {
                    clearFieldsOnException();
                }
            } else {
                clearFieldsOnEmpty();
            }
        }

        private void clearFieldsOnException() {
            if (isUpdating.compareAndSet(false, true)) {
                litrosField.setText("");
                valorAPagarField.setText("");
                valorTotalDisplay.setText("R$ 0,00");
                isUpdating.set(false);
            }
        }

        private void clearFieldsOnEmpty() {
            if (isUpdating.compareAndSet(false, true)) {
                if (!litrosField.getText().isEmpty()) litrosField.setText("");
                if (!valorAPagarField.getText().isEmpty()) valorAPagarField.setText("");
                valorTotalDisplay.setText("R$ 0,00");
                isUpdating.set(false);
            }
        }

        public boolean isFilled() {
            return !litrosField.getText().trim().isEmpty() || !valorAPagarField.getText().trim().isEmpty();
        }

        public void clearFields() {
            if (isUpdating.compareAndSet(false, true)) {
                litrosField.setText("");
                valorAPagarField.setText("");
                valorTotalDisplay.setText("R$ 0,00");
                isUpdating.set(false);
            }
        }

        public String getFuelName() {
            return fuelName;
        }

        public double getPrecoUnitario() {
            return precoUnitario;
        }

        public double getLitros() {
            try {
                return Double.parseDouble(litrosField.getText().replace(',', '.'));
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }

        public double getValorTotal() {
            try {
                return Double.parseDouble(valorAPagarField.getText().replace(',', '.'));
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TelaAbastecimento().setVisible(true);
        });
    }
}

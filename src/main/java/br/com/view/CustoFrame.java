package br.com.view;

import br.com.model.Custo;
import br.com.model.enums.TipoCusto;
import br.com.service.CustoApiService;
import br.com.service.ICustoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CustoFrame extends JFrame {

    private final ICustoService custoService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField impostoField = new JTextField(10);
    private final JTextField custoVariavelField = new JTextField(10);
    private final JTextField custoFixoField = new JTextField(10);
    private final JTextField margemLucroField = new JTextField(10);
    private final JTextField dataProcessamentoField = new JTextField(10);
    private final JComboBox<TipoCusto> tipoCustoComboBox = new JComboBox<>(TipoCusto.values());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    public CustoFrame() {
        this.custoService = new CustoApiService();

        setTitle("Gerenciamento de Custos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] columnNames = {"ID", "Imposto (%)", "Custo Variável", "Custo Fixo", "Margem Lucro (%)", "Data", "Tipo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        UIStyle.estilizarTabela(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyle.BORDA_SUTIL));
        add(scrollPane, BorderLayout.CENTER);

        // Painel Sul com Formulário e Botões
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setBackground(UIStyle.FUNDO_JANELA);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 4, 15, 10));
        UIStyle.estilizarPainel(formPanel);
        idField.setEditable(false);
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(idLabel);
        formPanel.add(idField);
        JLabel impostoLabel = new JLabel("Imposto (%):");
        impostoLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(impostoLabel);
        formPanel.add(impostoField);
        JLabel custoVariavelLabel = new JLabel("Custo Variável:");
        custoVariavelLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(custoVariavelLabel);
        formPanel.add(custoVariavelField);
        JLabel custoFixoLabel = new JLabel("Custo Fixo:");
        custoFixoLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(custoFixoLabel);
        formPanel.add(custoFixoField);
        JLabel margemLucroLabel = new JLabel("Margem Lucro (%):");
        margemLucroLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(margemLucroLabel);
        formPanel.add(margemLucroField);
        JLabel dataProcessamentoLabel = new JLabel("Data Processamento (yyyy-MM-dd):");
        dataProcessamentoLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(dataProcessamentoLabel);
        formPanel.add(dataProcessamentoField);
        JLabel tipoCustoLabel = new JLabel("Tipo de Custo:");
        tipoCustoLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(tipoCustoLabel);
        formPanel.add(tipoCustoComboBox);

        UIStyle.estilizarCampoDeTexto(idField);
        UIStyle.estilizarCampoDeTexto(impostoField);
        UIStyle.estilizarCampoDeTexto(custoVariavelField);
        UIStyle.estilizarCampoDeTexto(custoFixoField);
        UIStyle.estilizarCampoDeTexto(margemLucroField);
        UIStyle.estilizarCampoDeTexto(dataProcessamentoField);
        UIStyle.estilizarComboBox(tipoCustoComboBox);
        southPanel.add(formPanel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIStyle.FUNDO_JANELA);
        JButton novoButton = new JButton("Novo");
        JButton salvarButton = new JButton("Salvar");
        JButton deletarButton = new JButton("Deletar");

        UIStyle.estilizarBotaoSecundario(novoButton);
        UIStyle.estilizarBotaoPrimario(salvarButton);
        UIStyle.estilizarBotaoSecundario(deletarButton);

        buttonPanel.add(novoButton);
        buttonPanel.add(deletarButton);
        buttonPanel.add(salvarButton);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                preencherFormularioComLinhaSelecionada();
            }
        });

        novoButton.addActionListener(e -> limparFormulario());
        salvarButton.addActionListener(e -> salvarCusto());
        deletarButton.addActionListener(e -> deletarCusto());

        atualizarTabela();
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        impostoField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        custoVariavelField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        custoFixoField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        margemLucroField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        dataProcessamentoField.setText(tableModel.getValueAt(selectedRow, 5).toString());

        Object tipoCustoObj = tableModel.getValueAt(selectedRow, 6);
        if (tipoCustoObj instanceof TipoCusto) {
            tipoCustoComboBox.setSelectedItem(tipoCustoObj);
        }
    }

    private void limparFormulario() {
        idField.setText("");
        impostoField.setText("");
        custoVariavelField.setText("");
        custoFixoField.setText("");
        margemLucroField.setText("");
        dataProcessamentoField.setText("");
        tipoCustoComboBox.setSelectedIndex(0);
        table.clearSelection();
    }

    private void atualizarTabela() {
        try {
            List<Custo> custos = custoService.listarCustos();
            tableModel.setRowCount(0); // Limpa a tabela
            for (Custo c : custos) {
                tableModel.addRow(new Object[]{
                        c.id(),
                        c.imposto(),
                        c.custoVariavel(),
                        c.custoFixo(),
                        c.margemLucro(),
                        c.dataProcessamento().format(dateFormatter),
                        c.tipoCusto()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar custos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarCusto() {
        try {
            BigDecimal imposto = new BigDecimal(impostoField.getText().replace(',', '.'));
            BigDecimal custoVariavel = new BigDecimal(custoVariavelField.getText().replace(',', '.'));
            BigDecimal custoFixo = new BigDecimal(custoFixoField.getText().replace(',', '.'));
            BigDecimal margemLucro = new BigDecimal(margemLucroField.getText().replace(',', '.'));
            LocalDate data = LocalDate.parse(dataProcessamentoField.getText(), dateFormatter);
            TipoCusto tipoCusto = (TipoCusto) tipoCustoComboBox.getSelectedItem();

            Custo custo = new Custo(null, imposto, custoVariavel, custoFixo, margemLucro, data, tipoCusto);

            String idText = idField.getText();
            Custo custoSalvo;
            if (idText.isEmpty()) { // Criar novo
                custoSalvo = custoService.criarCusto(custo);
                JOptionPane.showMessageDialog(this, "Custo criado com sucesso! ID: " + custoSalvo.id());
            } else { // Atualizar existente
                Long id = Long.parseLong(idText);
                // Para records, passamos um novo objeto com o ID nulo, pois o ID é gerenciado pelo backend na atualização.
                Custo custoParaAtualizar = new Custo(null, imposto, custoVariavel, custoFixo, margemLucro, data, tipoCusto);
                custoSalvo = custoService.atualizarCusto(id, custoParaAtualizar);
                JOptionPane.showMessageDialog(this, "Custo atualizado com sucesso! ID: " + custoSalvo.id());
            }

            limparFormulario();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico. Use '.' como separador decimal.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use yyyy-MM-dd.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar custo: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarCusto() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um custo para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este custo?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Long id = Long.parseLong(idText);
            custoService.deletarCusto(id);
            JOptionPane.showMessageDialog(this, "Custo deletado com sucesso!");
            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar custo: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustoFrame frame = new CustoFrame();
            frame.setVisible(true);
        });
    }
}

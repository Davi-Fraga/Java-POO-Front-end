package br.com.view;

import br.com.model.Custo;
import br.com.service.CustoApiServiceMock;
import br.com.service.ICustoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    public CustoFrame() {
        this.custoService = new CustoApiServiceMock();

        setTitle("Cadastro de Custos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabela
        String[] columnNames = {"ID", "Imposto (%)", "Custo Variável", "Custo Fixo", "Margem Lucro (%)", "Data"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        idField.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Imposto (%):"));
        formPanel.add(impostoField);
        formPanel.add(new JLabel("Custo Variável:"));
        formPanel.add(custoVariavelField);
        formPanel.add(new JLabel("Custo Fixo:"));
        formPanel.add(custoFixoField);
        formPanel.add(new JLabel("Margem Lucro (%):"));
        formPanel.add(margemLucroField);
        formPanel.add(new JLabel("Data Processamento (yyyy-MM-dd):"));
        formPanel.add(dataProcessamentoField);

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        JButton novoButton = new JButton("Novo");
        JButton salvarButton = new JButton("Salvar");
        JButton deletarButton = new JButton("Deletar");
        JButton limparButton = new JButton("Limpar");

        buttonPanel.add(novoButton);
        buttonPanel.add(salvarButton);
        buttonPanel.add(deletarButton);
        buttonPanel.add(limparButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                preencherFormularioComLinhaSelecionada();
            }
        });

        novoButton.addActionListener(e -> limparFormulario());
        limparButton.addActionListener(e -> limparFormulario());
        salvarButton.addActionListener(e -> salvarCusto());
        deletarButton.addActionListener(e -> deletarCusto());

        // Carregar dados iniciais
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
    }

    private void limparFormulario() {
        idField.setText("");
        impostoField.setText("");
        custoVariavelField.setText("");
        custoFixoField.setText("");
        margemLucroField.setText("");
        dataProcessamentoField.setText("");
        table.clearSelection();
    }

    private void atualizarTabela() {
        try {
            List<Custo> custos = custoService.listarCustos();
            tableModel.setRowCount(0); // Limpa a tabela
            for (Custo c : custos) {
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getImposto(),
                        c.getCustoVariavel(),
                        c.getCustoFixo(),
                        c.getMargemLucro(),
                        c.getDataProcessamento().format(dateFormatter)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar custos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarCusto() {
        try {
            Double imposto = Double.parseDouble(impostoField.getText());
            Double custoVariavel = Double.parseDouble(custoVariavelField.getText());
            Double custoFixo = Double.parseDouble(custoFixoField.getText());
            Double margemLucro = Double.parseDouble(margemLucroField.getText());
            LocalDate data = LocalDate.parse(dataProcessamentoField.getText(), dateFormatter);

            Custo custo = new Custo(null, imposto, custoVariavel, custoFixo, margemLucro, data);

            String idText = idField.getText();
            if (idText.isEmpty()) { // Criar novo
                custoService.criarCusto(custo);
                JOptionPane.showMessageDialog(this, "Custo criado com sucesso!");
            } else { // Atualizar existente
                Long id = Long.parseLong(idText);
                custo.setId(id);
                custoService.atualizarCusto(id, custo);
                JOptionPane.showMessageDialog(this, "Custo atualizado com sucesso!");
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
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Usa o padrão
            }

            CustoFrame frame = new CustoFrame();
            frame.setVisible(true);
        });
    }
}
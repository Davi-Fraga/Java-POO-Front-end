package br.com.view;

import br.com.model.Estoque;
import br.com.model.enums.TipoEstoque;
import br.com.service.IEstoqueService;
import br.com.service.EstoqueApiService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EstoqueFrame extends JFrame {

    private final IEstoqueService estoqueService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField quantidadeField = new JTextField(10);
    private final JTextField localTanqueField = new JTextField(20);
    private final JTextField localEnderecoField = new JTextField(20);
    private final JTextField loteFabricacaoField = new JTextField(15);
    private final JTextField dataValidadeField = new JTextField(10);
    private final JComboBox<TipoEstoque> tipoEstoqueComboBox = new JComboBox<>(TipoEstoque.values());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    public EstoqueFrame() {
        this.estoqueService = new EstoqueApiService();

        setTitle("Gerenciamento de Estoque");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] columnNames = {"ID", "Quantidade", "Local/Tanque", "Endereço", "Lote", "Data Validade", "Tipo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        UIStyle.estilizarTabela(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyle.BORDA_SUTIL));
        add(scrollPane, BorderLayout.CENTER);

        // Painel Sul com Formulário e Botões
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setOpaque(false);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 4, 15, 10));
        UIStyle.estilizarPainel(formPanel);
        idField.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Quantidade:"));
        formPanel.add(quantidadeField);
        formPanel.add(new JLabel("Local/Tanque:"));
        formPanel.add(localTanqueField);
        formPanel.add(new JLabel("Endereço:"));
        formPanel.add(localEnderecoField);
        formPanel.add(new JLabel("Lote Fabricação:"));
        formPanel.add(loteFabricacaoField);
        formPanel.add(new JLabel("Data Validade (yyyy-MM-dd):"));
        formPanel.add(dataValidadeField);
        formPanel.add(new JLabel("Tipo de Estoque:"));
        formPanel.add(tipoEstoqueComboBox);

        UIStyle.estilizarCampoDeTexto(idField);
        UIStyle.estilizarCampoDeTexto(quantidadeField);
        UIStyle.estilizarCampoDeTexto(localTanqueField);
        UIStyle.estilizarCampoDeTexto(localEnderecoField);
        UIStyle.estilizarCampoDeTexto(loteFabricacaoField);
        UIStyle.estilizarCampoDeTexto(dataValidadeField);
        UIStyle.estilizarComboBox(tipoEstoqueComboBox);
        southPanel.add(formPanel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        JButton novoButton = new JButton("Novo");
        JButton salvarButton = new JButton("Salvar");
        JButton deletarButton = new JButton("Deletar");

        UIStyle.estilizarBotaoPrimario(salvarButton);
        UIStyle.estilizarBotaoSecundario(novoButton);
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
        salvarButton.addActionListener(e -> salvarEstoque());
        deletarButton.addActionListener(e -> deletarEstoque());

        atualizarTabela();
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        quantidadeField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        localTanqueField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        localEnderecoField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        loteFabricacaoField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        dataValidadeField.setText(tableModel.getValueAt(selectedRow, 5).toString());

        Object tipoEstoqueObj = tableModel.getValueAt(selectedRow, 6);
        if (tipoEstoqueObj instanceof TipoEstoque) {
            tipoEstoqueComboBox.setSelectedItem(tipoEstoqueObj);
        }
    }

    private void limparFormulario() {
        idField.setText("");
        quantidadeField.setText("");
        localTanqueField.setText("");
        localEnderecoField.setText("");
        loteFabricacaoField.setText("");
        dataValidadeField.setText("");
        tipoEstoqueComboBox.setSelectedIndex(0);
        table.clearSelection();
    }

    private void atualizarTabela() {
        try {
            List<Estoque> estoques = estoqueService.listarEstoques();
            tableModel.setRowCount(0); // Limpa a tabela
            for (Estoque e : estoques) {
                tableModel.addRow(new Object[]{
                        e.getId(),
                        e.getQuantidade(),
                        e.getLocalTanque(),
                        e.getLocalEndereco(),
                        e.getLoteFabricacao(),
                        e.getDataValidade().format(dateFormatter),
                        e.getTipoEstoque()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar estoques: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarEstoque() {
        try {
            BigDecimal quantidade = new BigDecimal(quantidadeField.getText().replace(',', '.'));
            String localTanque = localTanqueField.getText();
            String localEndereco = localEnderecoField.getText();
            String lote = loteFabricacaoField.getText();
            LocalDate dataValidade = LocalDate.parse(dataValidadeField.getText(), dateFormatter);
            TipoEstoque tipoEstoque = (TipoEstoque) tipoEstoqueComboBox.getSelectedItem();

            Estoque estoque = new Estoque(null, quantidade, localTanque, localEndereco, lote, dataValidade, tipoEstoque);

            String idText = idField.getText();
            if (idText.isEmpty()) { // Criar novo
                estoqueService.criarEstoque(estoque);
                JOptionPane.showMessageDialog(this, "Estoque criado com sucesso!");
            } else { // Atualizar existente
                Long id = Long.parseLong(idText);
                estoque.setId(id);
                estoqueService.atualizarEstoque(id, estoque);
                JOptionPane.showMessageDialog(this, "Estoque atualizado com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico (Quantidade). Use '.' como separador decimal.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use yyyy-MM-dd.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar estoque: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarEstoque() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um item do estoque para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este item do estoque?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Long id = Long.parseLong(idText);
            estoqueService.deletarEstoque(id);
            JOptionPane.showMessageDialog(this, "Item do estoque deletado com sucesso!");
            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar item do estoque: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EstoqueFrame frame = new EstoqueFrame();
            frame.setVisible(true);
        });
    }
}

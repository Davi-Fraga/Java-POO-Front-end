package br.com.view;

import br.com.model.Preco;
import br.com.service.IPrecoService;
import br.com.service.PrecoApiServiceMock;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrecoFrame extends JFrame {

    private final IPrecoService precoService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField valorField = new JTextField(10);
    private final JTextField dataHoraField = new JTextField(20);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PrecoFrame() {
        this.precoService = new PrecoApiServiceMock();

        setTitle("Gerenciamento de Preços");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] columnNames = {"ID", "Valor (R$)", "Data/Hora Alteração"};
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
        dataHoraField.setEditable(false);

        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(idLabel);
        formPanel.add(idField);
        JLabel valorLabel = new JLabel("Valor (R$):");
        valorLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(valorLabel);
        formPanel.add(valorField);
        JLabel dataHoraLabel = new JLabel("Última Alteração:");
        dataHoraLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(dataHoraLabel);
        formPanel.add(dataHoraField);

        UIStyle.estilizarCampoDeTexto(idField);
        UIStyle.estilizarCampoDeTexto(valorField);
        UIStyle.estilizarCampoDeTexto(dataHoraField);
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
        salvarButton.addActionListener(e -> salvarPreco());
        deletarButton.addActionListener(e -> deletarPreco());

        atualizarTabela();
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        valorField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        dataHoraField.setText(tableModel.getValueAt(selectedRow, 2).toString());
    }

    private void limparFormulario() {
        idField.setText("");
        valorField.setText("");
        dataHoraField.setText("");
        table.clearSelection();
    }

    private void atualizarTabela() {
        try {
            List<Preco> precos = precoService.listarPrecos();
            tableModel.setRowCount(0); // Limpa a tabela
            for (Preco p : precos) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getValor(),
                        p.getDataHoraAlteracao().format(dateTimeFormatter)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar preços: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarPreco() {
        try {
            BigDecimal valor = new BigDecimal(valorField.getText());
            Preco preco = new Preco(null, valor, null); // Data/Hora será definida pelo serviço mock

            String idText = idField.getText();
            if (idText.isEmpty()) { // Criar novo
                precoService.criarPreco(preco);
                JOptionPane.showMessageDialog(this, "Preço criado com sucesso!");
            } else { // Atualizar existente
                Long id = Long.parseLong(idText);
                preco.setId(id);
                precoService.atualizarPreco(id, preco);
                JOptionPane.showMessageDialog(this, "Preço atualizado com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico para o Valor. Use '.' como separador decimal.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar preço: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarPreco() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um preço para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este preço?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Long id = Long.parseLong(idText);
            precoService.deletarPreco(id);
            JOptionPane.showMessageDialog(this, "Preço deletado com sucesso!");
            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar preço: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PrecoFrame frame = new PrecoFrame();
            frame.setVisible(true);
        });
    }
}

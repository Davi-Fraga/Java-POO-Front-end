package br.com.view;

import br.com.model.Produto;
import br.com.service.IProdutoService;
import br.com.service.ProdutoApiServiceMock;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoFrame extends JFrame {

    private final IProdutoService produtoService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField nomeField = new JTextField(20);
    private final JTextField referenciaField = new JTextField(20);
    private final JTextField fornecedorField = new JTextField(15);
    private final JTextField marcaField = new JTextField(15);
    private final JTextField categoriaField = new JTextField(15);

    public ProdutoFrame() {
        this.produtoService = new ProdutoApiServiceMock();

        setTitle("Cadastro de Produtos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só esta janela
        setLocationRelativeTo(null);

        // Tabela
        String[] columnNames = {"ID", "Nome", "Referência", "Fornecedor", "Marca", "Categoria"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        idField.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Referência:"));
        formPanel.add(referenciaField);
        formPanel.add(new JLabel("Fornecedor:"));
        formPanel.add(fornecedorField);
        formPanel.add(new JLabel("Marca:"));
        formPanel.add(marcaField);
        formPanel.add(new JLabel("Categoria:"));
        formPanel.add(categoriaField);

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
        salvarButton.addActionListener(e -> salvarProduto());
        deletarButton.addActionListener(e -> deletarProduto());

        // Carregar dados iniciais
        atualizarTabela();
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        nomeField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        referenciaField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        fornecedorField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        marcaField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        categoriaField.setText(tableModel.getValueAt(selectedRow, 5).toString());
    }

    private void limparFormulario() {
        idField.setText("");
        nomeField.setText("");
        referenciaField.setText("");
        fornecedorField.setText("");
        marcaField.setText("");
        categoriaField.setText("");
        table.clearSelection();
    }

    private void atualizarTabela() {
        try {
            List<Produto> produtos = produtoService.listarProdutos();
            tableModel.setRowCount(0); // Limpa a tabela
            for (Produto p : produtos) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getReferencia(),
                        p.getFornecedor(),
                        p.getMarca(),
                        p.getCategoria()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarProduto() {
        try {
            String nome = nomeField.getText();
            String referencia = referenciaField.getText();
            String fornecedor = fornecedorField.getText();
            String marca = marcaField.getText();
            String categoria = categoriaField.getText();

            Produto produto = new Produto(null, nome, referencia, fornecedor, marca, categoria);

            String idText = idField.getText();
            if (idText.isEmpty()) { // Criar novo
                produtoService.criarProduto(produto);
                JOptionPane.showMessageDialog(this, "Produto criado com sucesso!");
            } else { // Atualizar existente
                Long id = Long.parseLong(idText);
                produto.setId(id);
                produtoService.atualizarProduto(id, produto);
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarProduto() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este produto?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Long id = Long.parseLong(idText);
            produtoService.deletarProduto(id);
            JOptionPane.showMessageDialog(this, "Produto deletado com sucesso!");
            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar produto: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
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

            ProdutoFrame frame = new ProdutoFrame();
            frame.setVisible(true);
        });
    }
}
package br.com.view;

import br.com.model.Produto;
import br.com.model.enums.TipoProduto;
import br.com.service.ProdutoService;
import br.com.util.AsyncTaskExecutor;
import br.com.util.ValidationUtil;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ProdutoFrame extends JFrame {

    private final ProdutoService produtoService;
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField idField = new JTextField(5);
    private final JTextField nomeField = new JTextField(20);
    private final JTextField descricaoField = new JTextField(30);
    private final JTextField precoField = new JTextField(10);
    private final JComboBox<TipoProduto> tipoProdutoComboBox = new JComboBox<>(TipoProduto.values());
    private final JTextField marcaField = new JTextField(20);
    private final JTextField fornecedorField = new JTextField(20);
    private final JTextField referenciaField = new JTextField(20);
    private final JTextField categoriaField = new JTextField(20);
    private final JTextField createdAtField = new JTextField(20);
    private final JTextField updatedAtField = new JTextField(20);

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Border defaultBorder = new JTextField().getBorder();

    public ProdutoFrame() {
        this.produtoService = new ProdutoService();

        setTitle("Gerenciamento de Produtos");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"ID", "Nome", "Descrição", "Preço (R$)", "Tipo", "Marca", "Fornecedor", "Referência", "Categoria", "Criado Em", "Atualizado Em"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UIStyle.estilizarTabela(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyle.BORDA_SUTIL));
        add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridLayout(0, 4, 15, 10));
        UIStyle.estilizarPainel(formPanel);

        idField.setEditable(false);
        createdAtField.setEditable(false);
        updatedAtField.setEditable(false);

        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(descricaoField);
        formPanel.add(new JLabel("Preço (R$):"));
        formPanel.add(precoField);
        formPanel.add(new JLabel("Tipo:"));
        formPanel.add(tipoProdutoComboBox);
        formPanel.add(new JLabel("Marca:"));
        formPanel.add(marcaField);
        formPanel.add(new JLabel("Fornecedor:"));
        formPanel.add(fornecedorField);
        formPanel.add(new JLabel("Referência:"));
        formPanel.add(referenciaField);
        formPanel.add(new JLabel("Categoria:"));
        formPanel.add(categoriaField);
        formPanel.add(new JLabel("Criado Em:"));
        formPanel.add(createdAtField);
        formPanel.add(new JLabel("Atualizado Em:"));
        formPanel.add(updatedAtField);

        Arrays.asList(idField, nomeField, descricaoField, precoField, marcaField, fornecedorField, referenciaField, categoriaField, createdAtField, updatedAtField)
                .forEach(UIStyle::estilizarCampoDeTexto);
        UIStyle.estilizarComboBox(tipoProdutoComboBox);

        southPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton novoButton = new JButton("Novo");
        JButton salvarButton = new JButton("Salvar");
        JButton deletarButton = new JButton("Deletar");
        JButton editarButton = new JButton("Editar");

        UIStyle.estilizarBotaoPrimario(salvarButton);
        UIStyle.estilizarBotaoSecundario(novoButton);
        UIStyle.estilizarBotaoSecundario(deletarButton);
        UIStyle.estilizarBotaoSecundario(editarButton);

        buttonPanel.add(novoButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(deletarButton);
        buttonPanel.add(salvarButton);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                preencherFormularioComLinhaSelecionada();
            }
        });

        novoButton.addActionListener(e -> limparFormulario());
        salvarButton.addActionListener(e -> salvarProduto());
        deletarButton.addActionListener(e -> deletarProduto());
        editarButton.addActionListener(e -> editarProduto());

        atualizarTabela();
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        idField.setText(safeToString(tableModel.getValueAt(selectedRow, 0)));
        nomeField.setText(safeToString(tableModel.getValueAt(selectedRow, 1)));
        descricaoField.setText(safeToString(tableModel.getValueAt(selectedRow, 2)));
        precoField.setText(safeToString(tableModel.getValueAt(selectedRow, 3)));
        tipoProdutoComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
        marcaField.setText(safeToString(tableModel.getValueAt(selectedRow, 5)));
        fornecedorField.setText(safeToString(tableModel.getValueAt(selectedRow, 6)));
        referenciaField.setText(safeToString(tableModel.getValueAt(selectedRow, 7)));
        categoriaField.setText(safeToString(tableModel.getValueAt(selectedRow, 8)));
        createdAtField.setText(safeToString(tableModel.getValueAt(selectedRow, 9)));
        updatedAtField.setText(safeToString(tableModel.getValueAt(selectedRow, 10)));
    }

    private void limparFormulario() {
        idField.setText("");
        nomeField.setText("");
        descricaoField.setText("");
        precoField.setText("");
        tipoProdutoComboBox.setSelectedIndex(0);
        marcaField.setText("");
        fornecedorField.setText("");
        referenciaField.setText("");
        categoriaField.setText("");
        createdAtField.setText("");
        updatedAtField.setText("");
        table.clearSelection();
        resetBorders();
    }

    private void atualizarTabela() {
        AsyncTaskExecutor.execute(
                this,
                produtoService::listarTodos,
                produtos -> {
                    tableModel.setRowCount(0);
                    for (Produto p : produtos) {
                        tableModel.addRow(new Object[]{
                                p.getId(),
                                p.getNome(),
                                p.getDescricao(),
                                p.getPreco(),
                                p.getTipoProduto(),
                                p.getMarca(),
                                p.getFornecedor(),
                                p.getReferencia(),
                                p.getCategoria(),
                                p.getCreatedAt() != null ? p.getCreatedAt().format(dateTimeFormatter) : "",
                                p.getUpdatedAt() != null ? p.getUpdatedAt().format(dateTimeFormatter) : ""
                        });
                    }
                },
                erro -> {
                    JOptionPane.showMessageDialog(this, "Erro ao buscar produtos: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace();
                }
        );
    }

    private void salvarProduto() {
        resetBorders();

        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        String precoStr = precoField.getText();
        TipoProduto tipoProduto = (TipoProduto) tipoProdutoComboBox.getSelectedItem();
        String marca = marcaField.getText();
        String fornecedor = fornecedorField.getText();
        String referencia = referenciaField.getText();
        String categoria = categoriaField.getText();

        try {
            ValidationUtil.validarCampoObrigatorio(nome, "Nome");
            ValidationUtil.validarCampoObrigatorio(descricao, "Descrição");
            ValidationUtil.validarValorMonetario(precoStr, "Preço");
            ValidationUtil.validarCampoObrigatorio(marca, "Marca");
            ValidationUtil.validarCampoObrigatorio(fornecedor, "Fornecedor");

            BigDecimal preco = new BigDecimal(precoStr.replace(',', '.'));

            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setTipoProduto(tipoProduto);
            produto.setMarca(marca);
            produto.setFornecedor(fornecedor);
            produto.setReferencia(referencia);
            produto.setCategoria(categoria);

            String idText = idField.getText();
            if (!idText.isEmpty()) {
                produto.setId(Long.parseLong(idText));
            }

            AsyncTaskExecutor.execute(
                    this,
                    () -> {
                        if (produto.getId() == null) {
                            return produtoService.criar(produto);
                        } else {
                            return produtoService.atualizar(produto.getId(), produto);
                        }
                    },
                    resultado -> {
                        JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
                        limparFormulario();
                        atualizarTabela();
                    },
                    erro -> {
                        JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + erro.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
                        erro.printStackTrace();
                    }
            );

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            highlightField(e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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

        Long id = Long.parseLong(idText);

        AsyncTaskExecutor.execute(
                this,
                () -> produtoService.deletar(id),
                sucesso -> {
                    JOptionPane.showMessageDialog(this, "Produto deletado com sucesso!");
                    limparFormulario();
                    atualizarTabela();
                },
                erro -> {
                    JOptionPane.showMessageDialog(this, "Erro ao deletar produto: " + erro.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace();
                }
        );
    }

    private void editarProduto() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Edite os campos e clique em 'Salvar' para aplicar as alterações.", "Editar Produto", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void highlightField(String errorMessage) {
        if (errorMessage.contains("Nome")) nomeField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("Descrição")) descricaoField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("Preço")) precoField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("Marca")) marcaField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("Fornecedor")) fornecedorField.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    private void resetBorders() {
        nomeField.setBorder(defaultBorder);
        descricaoField.setBorder(defaultBorder);
        precoField.setBorder(defaultBorder);
        marcaField.setBorder(defaultBorder);
        fornecedorField.setBorder(defaultBorder);
    }

    private String safeToString(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            ProdutoFrame frame = new ProdutoFrame();
            frame.setVisible(true);
        });
    }
}

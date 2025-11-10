package br.com.view;

import br.com.model.Produto;
import br.com.service.ProdutoService;
import br.com.util.AsyncTaskExecutor; // Importar AsyncTaskExecutor
import br.com.util.ValidationUtil; // Importar ValidationUtil

import javax.swing.*;
import javax.swing.border.Border; // Importar Border
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ProdutoFrame extends JFrame {

    private final ProdutoService produtoService;
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField idField = new JTextField(5);
    private final JTextField nomeField = new JTextField(20);
    private final JTextField descricaoField = new JTextField(30);
    private final JTextField precoField = new JTextField(10);
    private final JTextField createdAtField = new JTextField(20);
    private final JTextField updatedAtField = new JTextField(20);

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Borda padrão para resetar
    private final Border defaultBorder = new JTextField().getBorder();

    public ProdutoFrame() {
        this.produtoService = new ProdutoService();

        setTitle("Gerenciamento de Produtos");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Tabela ---
        String[] columnNames = {"ID", "Nome", "Descrição", "Preço (R$)", "Criado Em", "Atualizado Em"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células da tabela não editáveis
            }
        };
        table = new JTable(tableModel);
        UIStyle.estilizarTabela(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyle.BORDA_SUTIL));
        add(scrollPane, BorderLayout.CENTER);

        // --- Painel Sul com Formulário e Botões ---
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setOpaque(false);

        // --- Painel de Formulário ---
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
        formPanel.add(new JLabel("Criado Em:"));
        formPanel.add(createdAtField);
        formPanel.add(new JLabel("Atualizado Em:"));
        formPanel.add(updatedAtField);

        UIStyle.estilizarCampoDeTexto(idField);
        UIStyle.estilizarCampoDeTexto(nomeField);
        UIStyle.estilizarCampoDeTexto(descricaoField);
        UIStyle.estilizarCampoDeTexto(precoField);
        UIStyle.estilizarCampoDeTexto(createdAtField);
        UIStyle.estilizarCampoDeTexto(updatedAtField);
        southPanel.add(formPanel, BorderLayout.CENTER);

        // --- Painel de Botões ---
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

        // Listeners
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

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        nomeField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        descricaoField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        precoField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        createdAtField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        updatedAtField.setText(tableModel.getValueAt(selectedRow, 5).toString());
    }

    private void limparFormulario() {
        idField.setText("");
        nomeField.setText("");
        descricaoField.setText("");
        precoField.setText("");
        createdAtField.setText("");
        updatedAtField.setText("");
        table.clearSelection();
        resetBorders(); // Resetar bordas
    }

    private void atualizarTabela() {
        AsyncTaskExecutor.execute(
                this, // Componente pai para o dialog de carregamento
                () -> produtoService.listarTodos(), // Tarefa em background
                produtos -> { // Callback de sucesso
                    tableModel.setRowCount(0); // Limpa a tabela
                    for (Produto p : produtos) {
                        tableModel.addRow(new Object[]{
                                p.getId(),
                                p.getNome(),
                                p.getDescricao(),
                                p.getPreco(),
                                p.getCreatedAt() != null ? p.getCreatedAt().format(dateTimeFormatter) : "",
                                p.getUpdatedAt() != null ? p.getUpdatedAt().format(dateTimeFormatter) : ""
                        });
                    }
                },
                erro -> { // Callback de erro
                    JOptionPane.showMessageDialog(this, "Erro ao buscar produtos: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace();
                }
        );
    }

    private void salvarProduto() {
        resetBorders(); // Resetar bordas antes de validar

        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        String precoStr = precoField.getText();

        try {
            ValidationUtil.validarCampoObrigatorio(nome, "Nome");
            ValidationUtil.validarCampoObrigatorio(descricao, "Descrição");
            ValidationUtil.validarValorMonetario(precoStr, "Preço");

            BigDecimal preco = new BigDecimal(precoStr.replace(',', '.'));

            Produto produto = new Produto(null, nome, descricao, preco, null, null); // ID, createdAt, updatedAt serão definidos pelo backend

            String idText = idField.getText();
            if (!idText.isEmpty()) { // Se o ID não estiver vazio, é uma atualização
                Long id = Long.parseLong(idText);
                produto.setId(id); // Garante que o ID esteja no objeto para a atualização
            }

            AsyncTaskExecutor.execute(
                    this,
                    () -> {
                        if (idText.isEmpty()) {
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
            highlightField(e.getMessage()); // Tenta destacar o campo com erro
        } catch (Exception e) { // Este catch agora captura NumberFormatException e outras exceções
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
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
                () -> {
                    return produtoService.deletar(id);
                },
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

    // Métodos auxiliares para destaque de campos
    private void highlightField(String errorMessage) {
        if (errorMessage.contains("Nome")) nomeField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("Descrição")) descricaoField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("Preço")) precoField.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    private void resetBorders() {
        nomeField.setBorder(defaultBorder);
        descricaoField.setBorder(defaultBorder);
        precoField.setBorder(defaultBorder);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            ProdutoFrame frame = new ProdutoFrame();
            frame.setVisible(true);
        });
    }
}
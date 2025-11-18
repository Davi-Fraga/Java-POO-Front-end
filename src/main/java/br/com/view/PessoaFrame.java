package br.com.view;

import br.com.model.Pessoa;
import br.com.model.enums.TipoPessoa;
import br.com.service.PessoaService;
import br.com.util.AsyncTaskExecutor;
import br.com.util.PaginatedResponse;
import br.com.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PessoaFrame extends JFrame {

    private final PessoaService pessoaService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField nomeField = new JTextField(20);
    private final JTextField cpfCnpjField = new JTextField(15);
    private final JTextField ctpsField = new JTextField(10);
    private final JTextField dataNascimentoField = new JTextField(10);
    private final JComboBox<TipoPessoa> tipoPessoaComboBox = new JComboBox<>(TipoPessoa.values());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int currentPage = 0;
    private final int pageSize = 10;
    private final JLabel pageInfoLabel = new JLabel("Página 1 de 1");
    private final JButton prevPageButton = new JButton("Anterior");
    private final JButton nextPageButton = new JButton("Próxima");

    private final JTextField searchCpfCnpjField = new JTextField(15);
    private final JButton searchButton = new JButton("Buscar CPF/CNPJ");
    private final JButton clearSearchButton = new JButton("Limpar Busca");

    private final Border defaultBorder = new JTextField().getBorder();

    public PessoaFrame() {
        this.pessoaService = new PessoaService();

        setTitle("Gerenciamento de Pessoas");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] columnNames = {"ID", "Nome", "CPF/CNPJ", "CTPS", "Nascimento", "Tipo"};
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

        // Painel Norte para Busca
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        UIStyle.estilizarPainel(northPanel);
        northPanel.add(new JLabel("Buscar por CPF/CNPJ:"));
        northPanel.add(searchCpfCnpjField);
        northPanel.add(searchButton);
        northPanel.add(clearSearchButton);
        UIStyle.estilizarCampoDeTexto(searchCpfCnpjField);
        UIStyle.estilizarBotaoPrimario(searchButton);
        UIStyle.estilizarBotaoSecundario(clearSearchButton);
        add(northPanel, BorderLayout.NORTH);

        // Painel Sul com Formulário e Botões
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setOpaque(false);

        // Painel de Formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 4, 15, 10));
        UIStyle.estilizarPainel(formPanel);

        idField.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nome Completo:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("CPF/CNPJ:"));
        formPanel.add(cpfCnpjField);
        formPanel.add(new JLabel("Nº CTPS:"));
        formPanel.add(ctpsField);
        formPanel.add(new JLabel("Data Nascimento (dd/MM/yyyy):"));
        formPanel.add(dataNascimentoField);
        formPanel.add(new JLabel("Tipo Pessoa:"));
        formPanel.add(tipoPessoaComboBox);

        UIStyle.estilizarCampoDeTexto(idField);
        UIStyle.estilizarCampoDeTexto(nomeField);
        UIStyle.estilizarCampoDeTexto(cpfCnpjField);
        UIStyle.estilizarCampoDeTexto(ctpsField);
        UIStyle.estilizarCampoDeTexto(dataNascimentoField);
        UIStyle.estilizarComboBox(tipoPessoaComboBox);
        southPanel.add(formPanel, BorderLayout.CENTER);

        // Painel de Botões e Paginação
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

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
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Painel de Paginação
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        paginationPanel.setOpaque(false);
        paginationPanel.add(prevPageButton);
        paginationPanel.add(pageInfoLabel);
        paginationPanel.add(nextPageButton);
        UIStyle.estilizarBotaoSecundario(prevPageButton);
        UIStyle.estilizarBotaoSecundario(nextPageButton);
        bottomPanel.add(paginationPanel, BorderLayout.WEST);

        southPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                preencherFormularioComLinhaSelecionada();
            }
        });

        novoButton.addActionListener(e -> limparFormulario());
        salvarButton.addActionListener(e -> salvarPessoa());
        deletarButton.addActionListener(e -> deletarPessoa());
        editarButton.addActionListener(e -> editarPessoa());

        prevPageButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                atualizarTabela();
            }
        });
        nextPageButton.addActionListener(e -> {
            currentPage++;
            atualizarTabela();
        });

        searchButton.addActionListener(e -> buscarPessoaPorCpfCnpj());
        clearSearchButton.addActionListener(e -> {
            searchCpfCnpjField.setText("");
            atualizarTabela();
        });

        atualizarTabela();
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        // Variável para reutilização
        Object value;

        // ID (Coluna 0)
        value = tableModel.getValueAt(selectedRow, 0);
        idField.setText(value != null ? value.toString() : "");

        // Nome (Coluna 1)
        value = tableModel.getValueAt(selectedRow, 1);
        nomeField.setText(value != null ? value.toString() : "");

        // CPF/CNPJ (Coluna 2)
        value = tableModel.getValueAt(selectedRow, 2);
        cpfCnpjField.setText(value != null ? value.toString() : "");

        // CTPS (Coluna 3)
        value = tableModel.getValueAt(selectedRow, 3);
        ctpsField.setText(value != null ? value.toString() : "");

        // Data de Nascimento (Coluna 4)
        value = tableModel.getValueAt(selectedRow, 4);
        if (value instanceof LocalDate) {
            dataNascimentoField.setText(((LocalDate) value).format(dateFormatter));
        } else if (value != null) {
            dataNascimentoField.setText(value.toString());
        } else {
            dataNascimentoField.setText("");
        }

        // Tipo Pessoa (Coluna 5)
        value = tableModel.getValueAt(selectedRow, 5);
        if (value instanceof TipoPessoa) {
            tipoPessoaComboBox.setSelectedItem(value);
        } else if (value != null) {
            try {
                tipoPessoaComboBox.setSelectedItem(TipoPessoa.valueOf(value.toString()));
            } catch (IllegalArgumentException ex) {
                System.err.println("TipoPessoa inválido na tabela: " + value);
                tipoPessoaComboBox.setSelectedIndex(-1); // Reseta a seleção se for inválido
            }
        } else {
            tipoPessoaComboBox.setSelectedIndex(-1); // Reseta a seleção se for nulo
        }
    }

    private void limparFormulario() {
        idField.setText("");
        nomeField.setText("");
        cpfCnpjField.setText("");
        ctpsField.setText("");
        dataNascimentoField.setText("");
        tipoPessoaComboBox.setSelectedIndex(0);
        table.clearSelection();
        resetBorders();
    }

    private void atualizarTabela() {
        AsyncTaskExecutor.execute(
                this,
                () -> {
                    try {
                        String searchDoc = searchCpfCnpjField.getText().trim();
                        if (!searchDoc.isEmpty()) {
                            return pessoaService.buscarPorCpfCnpj(searchDoc);
                        } else {
                            PaginatedResponse<Pessoa> response = pessoaService.listarComPaginacao(currentPage, pageSize);
                            SwingUtilities.invokeLater(() -> {
                                pageInfoLabel.setText("Página " + (response.getPage() + 1) + " de " + response.getTotalPages());
                                prevPageButton.setEnabled(!response.isFirst());
                                nextPageButton.setEnabled(!response.isLast());
                            });
                            return response.getContent();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                pessoas -> {
                    tableModel.setRowCount(0);
                    for (Pessoa p : pessoas) {
                        tableModel.addRow(new Object[]{
                                p.getId(),
                                p.getNome(),
                                p.getCpfCnpj(),
                                p.getCpfCnpj(), // Usando CPF/CNPJ temporariamente para CTPS
                                p.getDataNascimento(),
                                p.getTipoPessoa()
                        });
                    }
                },
                erro -> {
                    JOptionPane.showMessageDialog(this, "Erro ao buscar pessoas: " + erro.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace();
                }
        );
    }

    private void buscarPessoaPorCpfCnpj() {
        String documento = searchCpfCnpjField.getText().trim();
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um CPF/CNPJ para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        currentPage = 0;
        atualizarTabela();
    }

    private void salvarPessoa() {
        // 1. Coleta dos dados da interface gráfica
        String nomeCompleto = nomeField.getText().trim();
        String cpfCnpj = cpfCnpjField.getText().trim();
        String dataNascimentoStr = dataNascimentoField.getText().trim();
        TipoPessoa tipoPessoa = (TipoPessoa) tipoPessoaComboBox.getSelectedItem();

        // 2. Validação dos campos obrigatórios
        if (nomeCompleto.isEmpty() || cpfCnpj.isEmpty() || dataNascimentoStr.isEmpty() || tipoPessoa == null) {
            JOptionPane.showMessageDialog(this, "Todos os campos obrigatórios (Nome, CPF/CNPJ, Data de Nascimento, Tipo) devem ser preenchidos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 3. Conversão e construção do objeto Pessoa
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(nomeCompleto);
            pessoa.setCpfCnpj(cpfCnpj);
            pessoa.setTipoPessoa(tipoPessoa);

            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, dateFormatter);
            pessoa.setDataNascimento(dataNascimento);

            String idText = idField.getText();
            if (idText != null && !idText.isEmpty()) {
                pessoa.setId(Long.parseLong(idText));
            }

            // 4. Chamada assíncrona ao serviço para criar ou atualizar
            AsyncTaskExecutor.execute(
                    this,
                    () -> {
                        try {
                            if (pessoa.getId() == null) {
                                return pessoaService.criar(pessoa);
                            } else {
                                return pessoaService.atualizar(pessoa);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    },
                    resultado -> {
                        JOptionPane.showMessageDialog(this, "Pessoa salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        limparFormulario();
                        atualizarTabela();
                    },
                    erro -> JOptionPane.showMessageDialog(this, "Erro ao salvar pessoa: " + erro.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE)
            );

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de Data de Nascimento inválido. Use o formato DD/MM/AAAA.", "Erro de Conversão", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O ID fornecido é inválido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarPessoa() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar esta pessoa?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Long id = Long.parseLong(idText);

        AsyncTaskExecutor.execute(
                this,
                () -> {
                    try {
                        return pessoaService.deletar(id);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                sucesso -> {
                    JOptionPane.showMessageDialog(this, "Pessoa deletada com sucesso!");
                    limparFormulario();
                    atualizarTabela();
                },
                erro -> {
                    String errorMessage = "Erro ao deletar pessoa: " + erro.getMessage();
                    if (erro.getCause() instanceof NumberFormatException) {
                        errorMessage = "Erro de formato numérico para o ID. Por favor, insira um número válido.";
                    }
                    JOptionPane.showMessageDialog(this, errorMessage, "Erro de API", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace();
                }
        );
    }

    private void editarPessoa() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Edite os campos e clique em 'Salvar' para aplicar as alterações.", "Editar Pessoa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void highlightField(String errorMessage) {
        if (errorMessage.contains("Nome")) nomeField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("CPF/CNPJ")) cpfCnpjField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("Data de Nascimento")) dataNascimentoField.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    private void resetBorders() {
        nomeField.setBorder(defaultBorder);
        cpfCnpjField.setBorder(defaultBorder);
        dataNascimentoField.setBorder(defaultBorder);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            PessoaFrame frame = new PessoaFrame();
            frame.setVisible(true);
        });
    }
}
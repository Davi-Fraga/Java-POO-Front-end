package br.com.view;

import br.com.model.Pessoa;
import br.com.model.enums.TipoPessoa;
import br.com.service.PessoaService;
import br.com.util.AsyncTaskExecutor;
import br.com.util.PaginatedResponse;
import br.com.util.ValidationUtil; // Importar ValidationUtil

import javax.swing.*;
import javax.swing.border.Border; // Importar Border
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
    private final JTextField ctpsField = new JTextField(10); // Este campo não tem correspondência direta no modelo Pessoa
    private final JTextField dataNascimentoField = new JTextField(10);
    private final JComboBox<TipoPessoa> tipoPessoaComboBox = new JComboBox<>(TipoPessoa.values());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    // Campos para Paginação
    private int currentPage = 0;
    private final int pageSize = 10; // Tamanho fixo da página
    private JLabel pageInfoLabel = new JLabel("Página 1 de 1");
    private JButton prevPageButton = new JButton("Anterior");
    private JButton nextPageButton = new JButton("Próxima");

    // Campo para Busca
    private JTextField searchCpfCnpjField = new JTextField(15);
    private JButton searchButton = new JButton("Buscar CPF/CNPJ");
    private JButton clearSearchButton = new JButton("Limpar Busca");

    // Borda padrão para resetar
    private final Border defaultBorder = new JTextField().getBorder();


    public PessoaFrame() {
        this.pessoaService = new PessoaService();

        setTitle("Gerenciamento de Pessoas");
        setSize(1000, 700); // Aumentar o tamanho para acomodar novos elementos
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Tabela ---
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

        // --- Painel Norte para Busca ---
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


        // --- Painel Sul com Formulário e Botões ---
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setOpaque(false);

        // --- Painel de Formulário ---
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
        formPanel.add(new JLabel("Data Nascimento (yyyy-MM-dd):"));
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

        // --- Painel de Botões e Paginação ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton novoButton = new JButton("Novo");
        JButton salvarButton = new JButton("Salvar");
        JButton deletarButton = new JButton("Deletar");
        JButton editarButton = new JButton("Editar"); // Adicionado botão Editar

        UIStyle.estilizarBotaoPrimario(salvarButton);
        UIStyle.estilizarBotaoSecundario(novoButton);
        UIStyle.estilizarBotaoSecundario(deletarButton);
        UIStyle.estilizarBotaoSecundario(editarButton);

        buttonPanel.add(novoButton);
        buttonPanel.add(editarButton); // Adicionado botão Editar
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
        editarButton.addActionListener(e -> editarPessoa()); // Listener para o botão Editar

        // Listeners de Paginação
        prevPageButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                atualizarTabela();
            }
        });
        nextPageButton.addActionListener(e -> {
            currentPage++; // A lógica para verificar se há próxima página será no atualizarTabela
            atualizarTabela();
        });

        // Listeners de Busca
        searchButton.addActionListener(e -> buscarPessoaPorCpfCnpj());
        clearSearchButton.addActionListener(e -> {
            searchCpfCnpjField.setText("");
            atualizarTabela(); // Recarrega a tabela sem filtro
        });


        atualizarTabela(); // Carrega a tabela inicialmente
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        nomeField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        cpfCnpjField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        ctpsField.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "");
        dataNascimentoField.setText(tableModel.getValueAt(selectedRow, 4).toString());

        Object tipoPessoaObj = tableModel.getValueAt(selectedRow, 5);
        if (tipoPessoaObj instanceof TipoPessoa) {
            tipoPessoaComboBox.setSelectedItem(tipoPessoaObj);
        } else if (tipoPessoaObj != null) { // Tenta converter String para TipoPessoa
            try {
                tipoPessoaComboBox.setSelectedItem(TipoPessoa.valueOf(tipoPessoaObj.toString()));
            } catch (IllegalArgumentException ex) {
                System.err.println("TipoPessoa inválido na tabela: " + tipoPessoaObj);
            }
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
        resetBorders(); // Resetar bordas após limpar
    }

    private void atualizarTabela() {
        AsyncTaskExecutor.execute(
                this,
                () -> {
                    String searchDoc = searchCpfCnpjField.getText().trim();
                    if (!searchDoc.isEmpty()) {
                        return pessoaService.buscarPorCpfCnpj(searchDoc);
                    } else {
                        // Chamar listarComPaginacao e obter o conteúdo
                        PaginatedResponse<Pessoa> response = pessoaService.listarComPaginacao(currentPage, pageSize);
                        SwingUtilities.invokeLater(() -> {
                            pageInfoLabel.setText("Página " + (response.getPage() + 1) + " de " + response.getTotalPages());
                            prevPageButton.setEnabled(!response.isFirst());
                            nextPageButton.setEnabled(!response.isLast());
                        });
                        return response.getContent();
                    }
                },
                pessoas -> {
                    tableModel.setRowCount(0); // Limpa a tabela
                    for (Pessoa p : pessoas) {
                        tableModel.addRow(new Object[]{
                                p.getId(),
                                p.getNome(),
                                p.getCpfCnpj(),
                                // p.getNumeroCtps(), // REMOVIDO: Não existe no modelo Pessoa
                                // Como o campo CTPS existe na UI, vamos preencher com o CPF/CNPJ ou deixar vazio se não houver um campo específico para CTPS no modelo
                                p.getCpfCnpj(), // Usando CPF/CNPJ temporariamente para preencher a coluna CTPS na tabela
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
        currentPage = 0; // Resetar paginação ao buscar
        atualizarTabela(); // O método atualizarTabela já lida com a lógica de busca
    }

    private void salvarPessoa() {
        resetBorders(); // Resetar bordas antes de validar

        String nome = nomeField.getText();
        String cpfCnpj = cpfCnpjField.getText();
        String dataNascimentoStr = dataNascimentoField.getText();
        TipoPessoa tipoPessoa = (TipoPessoa) tipoPessoaComboBox.getSelectedItem();

        try {
            ValidationUtil.validarCampoObrigatorio(nome, "Nome");
            ValidationUtil.validarCampoObrigatorio(cpfCnpj, "CPF/CNPJ");
            if (tipoPessoa == TipoPessoa.FISICA) {
                ValidationUtil.validarCPF(cpfCnpj);
            } else if (tipoPessoa == TipoPessoa.JURIDICA) {
                ValidationUtil.validarCNPJ(cpfCnpj);
            }
            ValidationUtil.validarCampoObrigatorio(dataNascimentoStr, "Data de Nascimento");
            ValidationUtil.validarData(dataNascimentoStr, "Data de Nascimento", dateFormatter);

            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, dateFormatter);

            Pessoa pessoa = new Pessoa(null, nome, cpfCnpj, dataNascimento, tipoPessoa);

            String idText = idField.getText();
            if (!idText.isEmpty()) { // Se o ID não estiver vazio, é uma atualização
                Long id = Long.parseLong(idText); // Pode lançar NumberFormatException
                pessoa.setId(id); // Definir o ID para a atualização
            }

            AsyncTaskExecutor.execute(
                    this,
                    () -> {
                        if (idText.isEmpty()) {
                            return pessoaService.criar(pessoa);
                        } else {
                            return pessoaService.atualizar(pessoa);
                        }
                    },
                    resultado -> {
                        JOptionPane.showMessageDialog(this, "Pessoa salva com sucesso!");
                        limparFormulario();
                        atualizarTabela();
                    },
                    erro -> {
                        JOptionPane.showMessageDialog(this, "Erro ao salvar pessoa: " + erro.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
                        erro.printStackTrace();
                    }
            );

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            highlightField(e.getMessage()); // Tenta destacar o campo com erro
        } catch (Exception e) { // Este catch agora captura NumberFormatException e outras exceções
            String errorMessage = "Erro ao salvar pessoa: " + e.getMessage();
            if (e.getCause() instanceof NumberFormatException) { // Verifica se a causa original foi NumberFormatException
                errorMessage = "Erro de formato numérico para o ID. Por favor, insira um número válido.";
            }
            JOptionPane.showMessageDialog(this, errorMessage, "Erro de API", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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

        Long id = Long.parseLong(idText); // Pode lançar NumberFormatException

        AsyncTaskExecutor.execute(
                this,
                () -> {
                    return pessoaService.deletar(id);
                },
                sucesso -> {
                    JOptionPane.showMessageDialog(this, "Pessoa deletada com sucesso!");
                    limparFormulario();
                    atualizarTabela();
                },
                erro -> {
                    String errorMessage = "Erro ao deletar pessoa: " + erro.getMessage();
                    if (erro.getCause() instanceof NumberFormatException) { // Verifica se a causa original foi NumberFormatException
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

    // Métodos auxiliares para destaque de campos
    private void highlightField(String errorMessage) {
        if (errorMessage.contains("Nome")) nomeField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("CPF/CNPJ")) cpfCnpjField.setBorder(BorderFactory.createLineBorder(Color.RED));
        else if (errorMessage.contains("Data de Nascimento")) dataNascimentoField.setBorder(BorderFactory.createLineBorder(Color.RED));
        // Adicione mais condições conforme necessário
    }

    private void resetBorders() {
        nomeField.setBorder(defaultBorder);
        cpfCnpjField.setBorder(defaultBorder);
        dataNascimentoField.setBorder(defaultBorder);
        // Resetar bordas de outros campos se houver
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
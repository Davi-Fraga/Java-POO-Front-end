package br.com.view;

import br.com.model.Contato;
import br.com.model.Pessoa; // Importar a classe Pessoa
import br.com.model.enums.TipoContato;
import br.com.service.ContatoService;
import br.com.service.PessoaService; // Importar PessoaService
import br.com.util.AsyncTaskExecutor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects; // Para Objects.equals

public class ContatoFrame extends JFrame {

    private final ContatoService contatoService;
    private final PessoaService pessoaService; // NOVO: Serviço para buscar Pessoas
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField telefoneField = new JTextField(15);
    private final JTextField emailField = new JTextField(20);
    private final JTextField enderecoField = new JTextField(20);
    private final JComboBox<TipoContato> tipoContatoComboBox = new JComboBox<>(TipoContato.values());
    private final JComboBox<Pessoa> pessoaComboBox = new JComboBox<>(); // NOVO: ComboBox para Pessoas
    private List<Pessoa> pessoas; // NOVO: Lista de pessoas carregadas

    public ContatoFrame() {
        this.contatoService = new ContatoService();
        this.pessoaService = new PessoaService(); // Inicializar PessoaService

        setTitle("Gerenciamento de Contatos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] columnNames = {"ID", "Telefone", "Email", "Endereço", "Tipo", "Pessoa"};
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

        // Painel Sul com Formulário e Botões
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setOpaque(false);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 4, 15, 10));
        UIStyle.estilizarPainel(formPanel);
        idField.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(telefoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Endereço:"));
        formPanel.add(enderecoField);
        formPanel.add(new JLabel("Tipo de Contato:"));
        formPanel.add(tipoContatoComboBox);
        formPanel.add(new JLabel("Pessoa:")); // NOVO: Label para Pessoa
        formPanel.add(pessoaComboBox); // NOVO: ComboBox para Pessoa

        UIStyle.estilizarCampoDeTexto(idField);
        UIStyle.estilizarCampoDeTexto(telefoneField);
        UIStyle.estilizarCampoDeTexto(emailField);
        UIStyle.estilizarCampoDeTexto(enderecoField);
        UIStyle.estilizarComboBox(tipoContatoComboBox);
        UIStyle.estilizarComboBox(pessoaComboBox); // Estilizar o novo ComboBox
        southPanel.add(formPanel, BorderLayout.CENTER);

        // Configurar ListCellRenderer para pessoaComboBox
        pessoaComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Pessoa) {
                    Pessoa pessoa = (Pessoa) value;
                    setText(pessoa.getNome()); // Exibe o nome da pessoa
                } else if (value == null) {
                    setText("-- Selecione uma Pessoa --"); // Texto padrão para item nulo
                }
                return this;
            }
        });

        // Painel de botões
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
        salvarButton.addActionListener(e -> salvarContato());
        deletarButton.addActionListener(e -> deletarContato());
        editarButton.addActionListener(e -> editarContato());

        carregarPessoas(); // Carregar pessoas ao iniciar o frame
        atualizarTabela();
    }

    // NOVO MÉTODO: Carregar Pessoas para o ComboBox
    private void carregarPessoas() {
        AsyncTaskExecutor.execute(
                this,
                () -> {
                    try {
                        return pessoaService.listarTodos();
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao listar pessoas: " + e.getMessage(), e);
                    }
                },
                pessoasCarregadas -> {
                    this.pessoas = pessoasCarregadas; // Armazena a lista de pessoas
                    pessoaComboBox.removeAllItems();
                    pessoaComboBox.addItem(null); // Adiciona um item nulo para "selecione"

                    // LOG AQUI: verificar IDs das pessoas vindas da API
                    for (Pessoa p : pessoasCarregadas) {
                        System.out.println("DEBUG Pessoa carregada: id=" + p.getId() + ", nome=" + p.getNome());
                        pessoaComboBox.addItem(p);
                    }
                },
                erro -> {
                    JOptionPane.showMessageDialog(this, "Erro ao carregar pessoas: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace();
                }
        );
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        telefoneField.setText(tableModel.getValueAt(selectedRow, 1) != null ? tableModel.getValueAt(selectedRow, 1).toString() : "");
        emailField.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
        enderecoField.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "");

        Object tipoContatoObj = tableModel.getValueAt(selectedRow, 4);
        if (tipoContatoObj instanceof TipoContato) {
            tipoContatoComboBox.setSelectedItem(tipoContatoObj);
        } else if (tipoContatoObj != null) {
            try {
                tipoContatoComboBox.setSelectedItem(TipoContato.valueOf(tipoContatoObj.toString()));
            } catch (IllegalArgumentException ex) {
                System.err.println("TipoContato inválido na tabela: " + tipoContatoObj);
            }
        }

        // NOVO: Preencher o ComboBox de Pessoa
        Object pessoaIdObj = tableModel.getValueAt(selectedRow, 5);
        Long tempPessoaIdNaTabela;
        if (pessoaIdObj instanceof Long) {
            tempPessoaIdNaTabela = (Long) pessoaIdObj;
        } else if (pessoaIdObj != null) {
            try {
                tempPessoaIdNaTabela = Long.parseLong(pessoaIdObj.toString());
            } catch (NumberFormatException e) {
                System.err.println("ID da Pessoa na tabela não é um número válido: " + pessoaIdObj);
                tempPessoaIdNaTabela = null;
            }
        } else {
            tempPessoaIdNaTabela = null;
        }
        final Long finalPessoaIdNaTabela = tempPessoaIdNaTabela;

        final List<Pessoa> finalPessoas = this.pessoas;

        if (finalPessoaIdNaTabela != null && finalPessoas != null) {
            finalPessoas.stream()
                    .filter(p -> Objects.equals(p.getId(), finalPessoaIdNaTabela))
                    .findFirst()
                    .ifPresentOrElse(
                            pessoaComboBox::setSelectedItem,
                            () -> pessoaComboBox.setSelectedItem(null)
                    );
        } else {
            pessoaComboBox.setSelectedItem(null);
        }
    }

    private void limparFormulario() {
        idField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        enderecoField.setText("");
        tipoContatoComboBox.setSelectedIndex(0);
        pessoaComboBox.setSelectedItem(null);
        table.clearSelection();
    }

    private void atualizarTabela() {
        final List<Pessoa> finalPessoas = this.pessoas;

        AsyncTaskExecutor.execute(
                this,
                () -> {
                    try {
                        return contatoService.listarTodos();
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao listar contatos: " + e.getMessage(), e);
                    }
                },
                contatos -> {
                    tableModel.setRowCount(0);
                    for (Contato c : contatos) {
                        String nomePessoa = "N/A";
                        Long pessoaId = c.getPessoaId();
                        if (pessoaId != null && finalPessoas != null) {
                            nomePessoa = finalPessoas.stream()
                                    .filter(p -> Objects.equals(p.getId(), pessoaId))
                                    .map(Pessoa::getNome)
                                    .findFirst()
                                    .orElse("ID " + pessoaId);
                        }

                        tableModel.addRow(new Object[]{
                                c.getId(),
                                c.getTelefone(),
                                c.getEmail(),
                                c.getEndereco(),
                                c.getTipoContato(),
                                pessoaId // Armazena o ID da pessoa na coluna 5
                        });
                    }
                },
                erro -> {
                    JOptionPane.showMessageDialog(this, "Erro ao buscar contatos: " + erro.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace();
                }
        );
    }

    private void salvarContato() {
        try {
            String telefone = telefoneField.getText();
            String email = emailField.getText();
            String endereco = enderecoField.getText();
            TipoContato tipoContato = (TipoContato) tipoContatoComboBox.getSelectedItem();
            Pessoa pessoaSelecionada = (Pessoa) pessoaComboBox.getSelectedItem();

            // 1) Validação de e-mail
            if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                JOptionPane.showMessageDialog(this,
                        "Digite um e-mail válido (ex: teste@gmail.com).",
                        "E-mail inválido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2) Validação para pessoa selecionada
            if (pessoaSelecionada == null) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, selecione uma Pessoa.",
                        "Erro de Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Long pessoaId = pessoaSelecionada.getId();

            // 3) Criação do contato com pessoaId
            Contato contato = new Contato(null, tipoContato, telefone, email, endereco, pessoaId);

            // DEBUGS IMPORTANTES
            System.out.println("DEBUG ContatoFrame pessoaSelecionada ID = " + pessoaId);
            System.out.println("DEBUG ContatoFrame Contato = " + contato);
            System.out.println("DEBUG ContatoFrame getPessoaId() = " + contato.getPessoaId());

            String idText = idField.getText();
            if (!idText.isEmpty()) {
                Long id = Long.parseLong(idText);
                contato.setId(id);
            }

            AsyncTaskExecutor.execute(
                    this,
                    () -> {
                        if (idText.isEmpty()) {
                            return contatoService.criar(contato);
                        } else {
                            return contatoService.atualizar(contato);
                        }
                    },
                    resultado -> {
                        JOptionPane.showMessageDialog(this, "Contato salvo com sucesso!");
                        limparFormulario();
                        atualizarTabela();
                    },
                    erro -> {
                        JOptionPane.showMessageDialog(this, "Erro ao salvar contato: " + erro.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
                        erro.printStackTrace();
                    }
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao processar dados do formulário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletarContato() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um contato para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este contato?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Long id = Long.parseLong(idText);

        AsyncTaskExecutor.execute(
                this,
                () -> {
                    try {
                        return contatoService.deletar(id);
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao deletar contato: " + e.getMessage(), e);
                    }
                },
                sucesso -> {
                    JOptionPane.showMessageDialog(this, "Contato deletado com sucesso!");
                    limparFormulario();
                    atualizarTabela();
                },
                erro -> {
                    JOptionPane.showMessageDialog(this, "Erro ao deletar contato: " + erro.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
                    erro.printStackTrace();
                }
        );
    }

    private void editarContato() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um contato na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Edite os campos e clique em 'Salvar' para aplicar as alterações.", "Editar Contato", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            new ContatoFrame().setVisible(true);
        });
    }
}

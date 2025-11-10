package br.com.view;

import br.com.model.Contato;
import br.com.model.enums.TipoContato;
import br.com.service.ContatoService;
import br.com.util.AsyncTaskExecutor; // Importar AsyncTaskExecutor

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContatoFrame extends JFrame {

    private final ContatoService contatoService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField telefoneField = new JTextField(15);
    private final JTextField emailField = new JTextField(20);
    private final JTextField enderecoField = new JTextField(20);
    private final JComboBox<TipoContato> tipoContatoComboBox = new JComboBox<>(TipoContato.values());

    public ContatoFrame() {
        this.contatoService = new ContatoService();

        setTitle("Gerenciamento de Contatos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] columnNames = {"ID", "Telefone", "Email", "Endereço", "Tipo"};
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

        UIStyle.estilizarCampoDeTexto(idField);
        UIStyle.estilizarCampoDeTexto(telefoneField);
        UIStyle.estilizarCampoDeTexto(emailField);
        UIStyle.estilizarCampoDeTexto(enderecoField);
        UIStyle.estilizarComboBox(tipoContatoComboBox);
        southPanel.add(formPanel, BorderLayout.CENTER);

        // Painel de botões
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
        editarButton.addActionListener(e -> editarContato()); // Listener para o botão Editar

        atualizarTabela();
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
        } else if (tipoContatoObj != null) { // Tenta converter String para TipoContato
            try {
                tipoContatoComboBox.setSelectedItem(TipoContato.valueOf(tipoContatoObj.toString()));
            } catch (IllegalArgumentException ex) {
                System.err.println("TipoContato inválido na tabela: " + tipoContatoObj);
            }
        }
    }

    private void limparFormulario() {
        idField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        enderecoField.setText("");
        tipoContatoComboBox.setSelectedIndex(0);
        table.clearSelection();
    }

    private void atualizarTabela() {
        AsyncTaskExecutor.execute(
            this, // Componente pai para o dialog de carregamento
            () -> contatoService.listarTodos(), // Tarefa em background
            contatos -> { // Callback de sucesso
                tableModel.setRowCount(0); // Limpa a tabela
                for (Contato c : contatos) {
                    tableModel.addRow(new Object[]{
                            c.getId(),
                            c.getTelefone(),
                            c.getEmail(),
                            c.getEndereco(),
                            c.getTipoContato()
                    });
                }
            },
            erro -> { // Callback de erro
                JOptionPane.showMessageDialog(this, "Erro ao buscar contatos: " + erro.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
                erro.printStackTrace(); // Para depuração
            }
        );
    }

    private void salvarContato() {
        try {
            String telefone = telefoneField.getText();
            String email = emailField.getText();
            String endereco = enderecoField.getText();
            TipoContato tipoContato = (TipoContato) tipoContatoComboBox.getSelectedItem();

            Contato contato = new Contato(null, tipoContato, telefone, email, endereco);

            String idText = idField.getText();
            if (!idText.isEmpty()) { // Se o ID não estiver vazio, é uma atualização
                Long id = Long.parseLong(idText);
                contato.setId(id); // Definir o ID para a atualização
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
                return contatoService.deletar(id);
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
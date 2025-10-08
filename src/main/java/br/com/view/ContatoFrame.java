package br.com.view;

import br.com.model.Contato;
import br.com.service.ContatoApiServiceMock;
import br.com.service.IContatoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContatoFrame extends JFrame {

    private final IContatoService contatoService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField telefoneField = new JTextField(15);
    private final JTextField emailField = new JTextField(25);
    private final JTextField enderecoField = new JTextField(30);

    public ContatoFrame() {
        this.contatoService = new ContatoApiServiceMock();

        setTitle("Cadastro de Contatos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabela
        String[] columnNames = {"ID", "Telefone", "E-mail", "Endereço"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        idField.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(telefoneField);
        formPanel.add(new JLabel("E-mail:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Endereço:"));
        formPanel.add(enderecoField);

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
        salvarButton.addActionListener(e -> salvarContato());
        deletarButton.addActionListener(e -> deletarContato());

        // Carregar dados iniciais
        atualizarTabela();
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        telefoneField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        enderecoField.setText(tableModel.getValueAt(selectedRow, 3).toString());
    }

    private void limparFormulario() {
        idField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        enderecoField.setText("");
        table.clearSelection();
    }

    private void atualizarTabela() {
        try {
            List<Contato> contatos = contatoService.listarContatos();
            tableModel.setRowCount(0); // Limpa a tabela
            for (Contato c : contatos) {
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getTelefone(),
                        c.getEmail(),
                        c.getEndereco()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar contatos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarContato() {
        try {
            String telefone = telefoneField.getText();
            String email = emailField.getText();
            String endereco = enderecoField.getText();

            if (email.isEmpty() || telefone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Telefone e E-mail são obrigatórios.", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Contato contato = new Contato(null, telefone, email, endereco);

            String idText = idField.getText();
            if (idText.isEmpty()) { // Criar novo
                contatoService.criarContato(contato);
                JOptionPane.showMessageDialog(this, "Contato criado com sucesso!");
            } else { // Atualizar existente
                Long id = Long.parseLong(idText);
                contato.setId(id);
                contatoService.atualizarContato(id, contato);
                JOptionPane.showMessageDialog(this, "Contato atualizado com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar contato: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
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

        try {
            Long id = Long.parseLong(idText);
            contatoService.deletarContato(id);
            JOptionPane.showMessageDialog(this, "Contato deletado com sucesso!");
            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar contato: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
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

            ContatoFrame frame = new ContatoFrame();
            frame.setVisible(true);
        });
    }
}
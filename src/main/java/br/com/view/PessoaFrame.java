package br.com.view;

import br.com.model.Pessoa;
import br.com.model.enums.TipoPessoa;
import br.com.service.IPessoaService;
import br.com.service.PessoaApiService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PessoaFrame extends JFrame {

    private final IPessoaService pessoaService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField nomeField = new JTextField(20);
    private final JTextField cpfCnpjField = new JTextField(15);
    private final JTextField ctpsField = new JTextField(10);
    private final JTextField dataNascimentoField = new JTextField(10);
    private final JComboBox<TipoPessoa> tipoPessoaComboBox = new JComboBox<>(TipoPessoa.values());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    public PessoaFrame() {
        this.pessoaService = new PessoaApiService();

        setTitle("Gerenciamento de Pessoas");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Tabela ---
        String[] columnNames = {"ID", "Nome", "CPF/CNPJ", "CTPS", "Nascimento", "Tipo"};
        tableModel = new DefaultTableModel(columnNames, 0);
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

        // --- Painel de Botões ---
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
        salvarButton.addActionListener(e -> salvarPessoa());
        deletarButton.addActionListener(e -> deletarPessoa());

        atualizarTabela();
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
    }

    private void atualizarTabela() {
        try {
            List<Pessoa> pessoas = pessoaService.listarPessoas();
            tableModel.setRowCount(0); // Limpa a tabela
            for (Pessoa p : pessoas) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getNomeCompleto(),
                        p.getCpfCnpj(),
                        p.getNumeroCtps(),
                        p.getDataNascimento(),
                        p.getTipoPessoa()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar pessoas: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Ajuda a depurar no console
        }
    }

    private void salvarPessoa() {
        try {
            String nome = nomeField.getText();
            String cpfCnpj = cpfCnpjField.getText();
            Long ctps = ctpsField.getText().isEmpty() ? null : Long.parseLong(ctpsField.getText());
            LocalDate dataNascimento;
            try {
                dataNascimento = LocalDate.parse(dataNascimentoField.getText(), dateFormatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido. Use yyyy-MM-dd.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TipoPessoa tipoPessoa = (TipoPessoa) tipoPessoaComboBox.getSelectedItem();

            Pessoa pessoa = new Pessoa(null, nome, cpfCnpj, ctps, dataNascimento, tipoPessoa);

            String idText = idField.getText();
            if (idText.isEmpty()) { // Criar nova pessoa
                pessoaService.criarPessoa(pessoa);
                JOptionPane.showMessageDialog(this, "Pessoa criada com sucesso!");
            } else { // Atualizar pessoa existente
                Long id = Long.parseLong(idText);
                pessoa.setId(id);
                pessoaService.atualizarPessoa(id, pessoa);
                JOptionPane.showMessageDialog(this, "Pessoa atualizada com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico (CTPS).", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar pessoa: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Ajuda a depurar no console
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

        try {
            Long id = Long.parseLong(idText);
            pessoaService.deletarPessoa(id);
            JOptionPane.showMessageDialog(this, "Pessoa deletada com sucesso!");
            limparFormulario();
            atualizarTabela();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar pessoa: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Ajuda a depurar no console
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Garante que o Look and Feel seja aplicado antes de criar a janela
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

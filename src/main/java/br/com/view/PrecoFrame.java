package br.com.view;

import br.com.model.Preco;
import br.com.model.enums.TipoPreco; // Importar o enum TipoPreco
import br.com.service.PrecoService;
import br.com.service.IPrecoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrecoFrame extends JFrame {

    private final PrecoService precoService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField valorField = new JTextField(10);
    // Campos de data e hora agora serão apenas para exibição, não para entrada direta
    private final JTextField dataAlteracaoField = new JTextField(10);
    private final JTextField horaAlteracaoField = new JTextField(10);
    private final JComboBox<TipoPreco> tipoPrecoComboBox = new JComboBox<>(TipoPreco.values()); // Novo JComboBox

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // HH:mm:ss

    public PrecoFrame() {
        this.precoService = new PrecoService();

        setTitle("Gerenciamento de Preços");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        // Colunas atualizadas para refletir o novo modelo
        String[] columnNames = {"ID", "Valor (R$)", "Data Alteração", "Hora Alteração", "Tipo"};
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
        dataAlteracaoField.setEditable(false); // Apenas para exibição
        horaAlteracaoField.setEditable(false); // Apenas para exibição

        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(idLabel);
        formPanel.add(idField);

        JLabel valorLabel = new JLabel("Valor (R$):");
        valorLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(valorLabel);
        formPanel.add(valorField);

        JLabel dataAlteracaoLabel = new JLabel("Data Alteração:");
        dataAlteracaoLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(dataAlteracaoLabel);
        formPanel.add(dataAlteracaoField);

        JLabel horaAlteracaoLabel = new JLabel("Hora Alteração:");
        horaAlteracaoLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(horaAlteracaoLabel);
        formPanel.add(horaAlteracaoField);

        JLabel tipoPrecoLabel = new JLabel("Tipo de Preço:"); // Novo label
        tipoPrecoLabel.setForeground(UIStyle.TEXTO_NORMAL);
        formPanel.add(tipoPrecoLabel);
        formPanel.add(tipoPrecoComboBox); // Novo campo

        UIStyle.estilizarCampoDeTexto(idField);
        UIStyle.estilizarCampoDeTexto(valorField);
        UIStyle.estilizarCampoDeTexto(dataAlteracaoField);
        UIStyle.estilizarCampoDeTexto(horaAlteracaoField);
        UIStyle.estilizarComboBox(tipoPrecoComboBox); // Estilizar o novo JComboBox
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
        dataAlteracaoField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        horaAlteracaoField.setText(tableModel.getValueAt(selectedRow, 3).toString());

        Object tipoPrecoObj = tableModel.getValueAt(selectedRow, 4);
        if (tipoPrecoObj instanceof TipoPreco) {
            tipoPrecoComboBox.setSelectedItem(tipoPrecoObj);
        } else if (tipoPrecoObj != null) {
            try {
                tipoPrecoComboBox.setSelectedItem(TipoPreco.valueOf(tipoPrecoObj.toString()));
            } catch (IllegalArgumentException ex) {
                System.err.println("TipoPreco inválido na tabela: " + tipoPrecoObj);
            }
        }
    }

    private void limparFormulario() {
        idField.setText("");
        valorField.setText("");
        dataAlteracaoField.setText("");
        horaAlteracaoField.setText("");
        tipoPrecoComboBox.setSelectedIndex(0); // Limpa a seleção do JComboBox
        table.clearSelection();
    }

    private void atualizarTabela() {
        try {
            List<Preco> precos = precoService.listarTodos();
            tableModel.setRowCount(0);
            for (Preco p : precos) {
                String dataFormatada = (p.getDataAlteracao() != null) ?
                                        p.getDataAlteracao().format(dateFormatter) :
                                        "N/A";
                String horaFormatada = (p.getHoraAlteracao() != null) ?
                                        p.getHoraAlteracao().format(timeFormatter) :
                                        "N/A";
                String tipoPrecoDisplay = (p.getTipoPreco() != null) ?
                                            p.getTipoPreco().toString() :
                                            "N/A";

                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getValor(),
                        dataFormatada,
                        horaFormatada,
                        tipoPrecoDisplay // Adiciona o tipo de preço
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar preços: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void salvarPreco() {
        try {
            BigDecimal valor = new BigDecimal(valorField.getText().replace(",", "."));

            // Obter data e hora atuais
            LocalDate dataAtual = LocalDate.now();
            LocalTime horaAtual = LocalTime.now();
            // Obter tipo de preço selecionado
            TipoPreco tipoPrecoSelecionado = (TipoPreco) tipoPrecoComboBox.getSelectedItem();

            // Criar objeto Preco com os novos campos
            Preco preco = new Preco(null, valor, dataAtual, horaAtual, tipoPrecoSelecionado);

            String idText = idField.getText();
            if (idText.isEmpty()) { // Criar novo
                precoService.criar(preco);
                JOptionPane.showMessageDialog(this, "Preço criado com sucesso!");
            } else { // Atualizar existente
                Long id = Long.parseLong(idText);
                preco.setId(id);
                precoService.atualizar(preco);
                JOptionPane.showMessageDialog(this, "Preço atualizado com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico para o Valor. Use '.' como separador decimal.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar preço: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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
            precoService.deletar(id);
            JOptionPane.showMessageDialog(this, "Preço deletado com sucesso!");
            limparFormulario();
            atualizarTabela();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar preço: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            PrecoFrame frame = new PrecoFrame();
            frame.setVisible(true);
        });
    }
}

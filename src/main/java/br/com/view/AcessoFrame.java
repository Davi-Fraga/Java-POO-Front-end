package br.com.view;

import br.com.model.Acesso;
import br.com.model.PerfilAcesso;
import br.com.service.AcessoService;
import br.com.service.PerfilAcessoService;
import br.com.util.AsyncTaskExecutor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AcessoFrame extends JFrame {

    private final PerfilAcessoService perfilAcessoService = new PerfilAcessoService();
    private final AcessoService acessoService = new AcessoService();
    private DefaultTableModel perfilTableModel;
    private DefaultTableModel logTableModel;

    public AcessoFrame() {
        setTitle("Painel de Controle de Acesso");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIStyle.FONTE_PADRAO);

        tabbedPane.addTab("Gerenciamento de Perfis", createPerfisPanel());
        tabbedPane.addTab("Logs de Acesso", createLogsPanel());

        add(tabbedPane);
    }

    private JPanel createPerfisPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.FUNDO_JANELA);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] perfilColunas = {"ID", "Nome do Perfil", "Status", "Permissões"};
        perfilTableModel = new DefaultTableModel(perfilColunas, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable perfilTable = new JTable(perfilTableModel);
        UIStyle.estilizarTabela(perfilTable);
        perfilTable.getColumnModel().getColumn(2).setCellRenderer(new StatusCellRenderer());
        atualizarTabelaPerfis();
        panel.add(new JScrollPane(perfilTable), BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoesPanel.setOpaque(false);
        JButton adicionarButton = new JButton("Adicionar Perfil");
        JButton removerButton = new JButton("Remover Perfil");
        JButton statusButton = new JButton("Alternar Status");
        JButton permissoesButton = new JButton("Editar Permissões");

        botoesPanel.add(adicionarButton);
        botoesPanel.add(removerButton);
        botoesPanel.add(statusButton);
        botoesPanel.add(permissoesButton);
        panel.add(botoesPanel, BorderLayout.SOUTH);

        adicionarButton.addActionListener(e -> adicionarNovoPerfil());
        removerButton.addActionListener(e -> removerPerfilSelecionado(perfilTable));
        statusButton.addActionListener(e -> alternarStatusSelecionado(perfilTable));
        permissoesButton.addActionListener(e -> editarPermissoesSelecionado(perfilTable));

        return panel;
    }

    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.FUNDO_JANELA);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] logColunas = {"ID", "Usuário/Perfil", "Local de Login", "Data/Hora", "Status"};
        logTableModel = new DefaultTableModel(logColunas, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable logTable = new JTable(logTableModel);
        UIStyle.estilizarTabela(logTable);
        logTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        atualizarTabelaLogs();
        panel.add(new JScrollPane(logTable), BorderLayout.CENTER);

        return panel;
    }

    private void atualizarTabelaPerfis() {
        AsyncTaskExecutor.execute(
                this,
                () -> {
                    try {
                        return perfilAcessoService.listarPerfis();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                perfis -> {
                    perfilTableModel.setRowCount(0);
                    for (PerfilAcesso perfil : perfis) {
                        String permissoesStr = perfil.getPermissoes().entrySet().stream()
                                .filter(Map.Entry::getValue)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.joining(", "));
                        perfilTableModel.addRow(new Object[]{perfil.getIdPerfil(), perfil.getNomePerfil(), perfil.getStatus(), permissoesStr});
                    }
                },
                this::handleApiError
        );
    }

    private void atualizarTabelaLogs() {
        AsyncTaskExecutor.execute(
                this,
                () -> {
                    try {
                        return acessoService.listarTodos();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                logs -> {
                    logTableModel.setRowCount(0);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    for (Acesso log : logs) {
                        LocalDateTime dataHora = log.getDataHora();
                        String dataFormatada = (dataHora != null) ? dataHora.format(formatter) : "Data Indisponível";
                        logTableModel.addRow(new Object[]{log.getIdAcesso(), log.getUsuarioOuPerfil(), log.getLocalLogin(), dataFormatada, log.getStatus()});
                    }
                },
                this::handleApiError
        );
    }

    private void adicionarNovoPerfil() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do novo perfil:");
        if (nome != null && !nome.trim().isEmpty()) {
            AsyncTaskExecutor.execute(
                    this,
                    () -> {
                        try {
                            return perfilAcessoService.adicionarPerfil(nome);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    },
                    perfil -> atualizarTabelaPerfis(),
                    this::handleApiError
            );
        }
    }

    private void removerPerfilSelecionado(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idPerfil = (int) perfilTableModel.getValueAt(selectedRow, 0);
            AsyncTaskExecutor.execute(
                    this,
                    () -> {
                        try {
                            perfilAcessoService.removerPerfil(idPerfil);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    },
                    result -> atualizarTabelaPerfis(),
                    this::handleApiError
            );
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um perfil.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void alternarStatusSelecionado(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idPerfil = (int) perfilTableModel.getValueAt(selectedRow, 0);
            AsyncTaskExecutor.execute(
                    this,
                    () -> {
                        try {
                            perfilAcessoService.alternarStatus(idPerfil);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    },
                    result -> atualizarTabelaPerfis(),
                    this::handleApiError
            );
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um perfil.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarPermissoesSelecionado(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idPerfil = (int) perfilTableModel.getValueAt(selectedRow, 0);
            String area = JOptionPane.showInputDialog(this, "Digite a área de permissão (ex: gerencia, estoque, vendas):");
            if (area != null && !area.trim().isEmpty()) {
                int result = JOptionPane.showConfirmDialog(this, "Conceder acesso a '" + area + "'?", "Confirmar Permissão", JOptionPane.YES_NO_OPTION);
                boolean podeAcessar = (result == JOptionPane.YES_OPTION);
                AsyncTaskExecutor.execute(
                        this,
                        () -> {
                            try {
                                perfilAcessoService.modificarPermissao(idPerfil, area.toLowerCase(), podeAcessar);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            return null;
                        },
                        res -> atualizarTabelaPerfis(),
                        this::handleApiError
                );
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um perfil.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleApiError(Throwable e) {
        JOptionPane.showMessageDialog(this, "Erro na comunicação com a API: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) value;

            if (status == null) {
                c.setForeground(UIStyle.TEXTO_NORMAL);
                setText("");
            } else if (!isSelected) {
                switch (status) {
                    case "Sucesso":
                    case "Ativo":
                        c.setForeground(UIStyle.VERDE_SUCESSO);
                        break;
                    case "Falha":
                        c.setForeground(UIStyle.LARANJA_ALERTA);
                        break;
                    case "Bloqueado":
                        c.setForeground(UIStyle.VERMELHO_ERRO);
                        break;
                    default:
                        c.setForeground(UIStyle.TEXTO_NORMAL);
                        break;
                }
            }
            setHorizontalAlignment(JLabel.CENTER);
            return c;
        }
    }
}

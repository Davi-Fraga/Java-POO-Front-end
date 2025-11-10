package br.com.view;

import br.com.dao.UsuarioDAO;
import br.com.model.Usuario;
import br.com.util.Sessao;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de login que serve como ponto de entrada para o sistema.
 */
public class TelaLogin extends JFrame {

    private final UsuarioDAO usuarioDAO;

    public TelaLogin() {
        this.usuarioDAO = new UsuarioDAO();

        setTitle("Login - Sistema de Gerenciamento");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UIStyle.FUNDO_JANELA);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 25, 10, 25);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Logotipo ---
        JLabel logoLabel = new JLabel("POSTO", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 80));
        logoLabel.setForeground(UIStyle.AZUL_PRINCIPAL);
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 25, 30, 25);
        add(logoLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 25, 10, 25);

        // --- Campo Usuário ---
        JLabel userLabel = new JLabel("Usuário:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 1;
        add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 2;
        add(userField, gbc);

        // --- Campo Senha ---
        JLabel passLabel = new JLabel("Senha:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 3;
        add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 4;
        add(passField, gbc);

        // --- Botão de Login ---
        JButton loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(UIStyle.AZUL_PRINCIPAL);
        loginButton.setForeground(UIStyle.TEXTO_BOTAO);
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 25, 20, 25);
        gbc.ipady = 10; // Aumenta a altura do botão
        add(loginButton, gbc);

        // --- Ação do Botão ---
        loginButton.addActionListener(e -> {
            String login = userField.getText();
            String senha = new String(passField.getPassword());

            if ("teste2".equals(login) && "123".equals(senha)) {
                // Ação: Instanciar e exibir a nova tela de abastecimento
                TelaAbastecimento tela = new TelaAbastecimento();
                tela.setVisible(true);
                // Ação: Ocultar ou fechar a tela de login atual
                dispose();
            } else {
                Usuario usuarioAutenticado = usuarioDAO.autenticar(login, senha);

                if (usuarioAutenticado != null) {
                    // Acesso concedido. Fecha a tela de login IMEDIATAMENTE.
                    dispose();

                    Sessao.setUsuarioLogado(usuarioAutenticado);
                    String perfil = usuarioAutenticado.getPerfilAcesso();

                    if ("GERENCIA".equals(perfil)) {
                        // Roteamento EXCLUSIVO para a área de Gerência
                        MainFrame mainFrame = new MainFrame(usuarioAutenticado.getLogin());
                        mainFrame.setVisible(true);
                    } else {
                        // Tratamento de erro para perfis não mapeados
                        JOptionPane.showMessageDialog(null, "Erro de Acesso: Perfil não autorizado.", "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Login falhou
                    JOptionPane.showMessageDialog(this, "Login ou senha inválidos.", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(() -> {
            TelaLogin frame = new TelaLogin();
            frame.setVisible(true);
        });
    }
}
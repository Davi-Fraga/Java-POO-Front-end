package br.com.util;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncTaskExecutor<T> extends SwingWorker<T, Void> {

    private final Supplier<T> backgroundTask;
    private final Consumer<T> successCallback;
    private final Consumer<Throwable> errorCallback;
    private final JDialog loadingDialog;
    private final Component parentComponent;

    public AsyncTaskExecutor(Component parentComponent, Supplier<T> backgroundTask, Consumer<T> successCallback, Consumer<Throwable> errorCallback) {
        this.parentComponent = parentComponent;
        this.backgroundTask = backgroundTask;
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;

        // Setup loading dialog
        // Tenta encontrar a janela pai para o dialog, se não, usa null
        Frame ownerFrame = null;
        if (parentComponent instanceof Frame) {
            ownerFrame = (Frame) parentComponent;
        } else if (parentComponent != null) {
            ownerFrame = (Frame) SwingUtilities.getWindowAncestor(parentComponent);
        }

        loadingDialog = new JDialog(ownerFrame, "Carregando...", Dialog.ModalityType.APPLICATION_MODAL);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Impede o fechamento
        loadingDialog.setSize(250, 100);
        loadingDialog.setResizable(false);
        loadingDialog.setLocationRelativeTo(parentComponent); // Centraliza em relação ao componente pai

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Por favor, aguarde..."), BorderLayout.NORTH);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.CENTER);
        loadingDialog.add(panel);
    }

    @Override
    protected T doInBackground() throws Exception {
        // Mostrar o dialog de carregamento na EDT antes de iniciar a tarefa
        SwingUtilities.invokeLater(() -> {
            if (!loadingDialog.isVisible()) {
                loadingDialog.setVisible(true);
            }
        });
        return backgroundTask.get();
    }

    @Override
    protected void done() {
        // Esconder e descartar o dialog de carregamento na EDT
        SwingUtilities.invokeLater(() -> {
            loadingDialog.setVisible(false);
            loadingDialog.dispose();
        });

        try {
            T result = get(); // Obtém o resultado de doInBackground
            successCallback.accept(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            errorCallback.accept(e);
        } catch (ExecutionException e) {
            errorCallback.accept(e.getCause()); // Obtém a exceção real da tarefa em segundo plano
        } catch (Exception e) { // Captura quaisquer outras exceções inesperadas
            errorCallback.accept(e);
        }
    }

    public static <T> void execute(Component parentComponent, Supplier<T> backgroundTask, Consumer<T> successCallback, Consumer<Throwable> errorCallback) {
        new AsyncTaskExecutor<>(parentComponent, backgroundTask, successCallback, errorCallback).execute();
    }
}

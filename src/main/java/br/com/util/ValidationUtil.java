package br.com.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    // Regex para CPF (apenas formato, não valida dígitos verificadores)
    private static final String CPF_REGEX = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$";
    // Regex para CNPJ (apenas formato, não valida dígitos verificadores)
    private static final String CNPJ_REGEX = "^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$";
    // Regex para email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    /**
     * Valida se um campo obrigatório não está vazio ou nulo.
     * @param valor O valor do campo.
     * @param nomeCampo O nome do campo para a mensagem de erro.
     * @return true se válido, false caso contrário.
     * @throws IllegalArgumentException se o campo for inválido.
     */
    public static boolean validarCampoObrigatorio(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " é obrigatório.");
        }
        return true;
    }

    /**
     * Valida o formato de um CPF.
     * @param cpf O CPF a ser validado.
     * @return true se válido, false caso contrário.
     * @throws IllegalArgumentException se o CPF for inválido.
     */
    public static boolean validarCPF(String cpf) {
        if (cpf == null || !cpf.matches(CPF_REGEX)) {
            throw new IllegalArgumentException("CPF inválido. Formato esperado: XXX.XXX.XXX-XX");
        }
        // Adicionar lógica de validação de dígitos verificadores se necessário
        return true;
    }

    /**
     * Valida o formato de um CNPJ.
     * @param cnpj O CNPJ a ser validado.
     * @return true se válido, false caso contrário.
     * @throws IllegalArgumentException se o CNPJ for inválido.
     */
    public static boolean validarCNPJ(String cnpj) {
        if (cnpj == null || !cnpj.matches(CNPJ_REGEX)) {
            throw new IllegalArgumentException("CNPJ inválido. Formato esperado: XX.XXX.XXX/XXXX-XX");
        }
        // Adicionar lógica de validação de dígitos verificadores se necessário
        return true;
    }

    /**
     * Valida o formato de um email.
     * @param email O email a ser validado.
     * @return true se válido, false caso contrário.
     * @throws IllegalArgumentException se o email for inválido.
     */
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Email pode ser opcional, se não for obrigatório, retorna true se vazio
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
        return true;
    }

    /**
     * Valida se um valor pode ser convertido para BigDecimal e é positivo.
     * @param valor O valor monetário como String.
     * @param nomeCampo O nome do campo para a mensagem de erro.
     * @return true se válido, false caso contrário.
     * @throws IllegalArgumentException se o valor for inválido.
     */
    public static boolean validarValorMonetario(String valor, String nomeCampo) {
        validarCampoObrigatorio(valor, nomeCampo);
        try {
            BigDecimal bd = new BigDecimal(valor.replace(",", "."));
            if (bd.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(nomeCampo + " não pode ser negativo.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nomeCampo + " inválido. Use apenas números e '.' ou ',' como separador decimal.");
        }
        return true;
    }

    /**
     * Valida se uma string pode ser convertida para LocalDate usando um formato específico.
     * @param data A data como String.
     * @param nomeCampo O nome do campo para a mensagem de erro.
     * @param formatter O DateTimeFormatter a ser usado.
     * @return true se válido, false caso contrário.
     * @throws IllegalArgumentException se a data for inválida.
     */
    public static boolean validarData(String data, String nomeCampo, DateTimeFormatter formatter) {
        validarCampoObrigatorio(data, nomeCampo);
        try {
            LocalDate.parse(data, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(nomeCampo + " inválida. Formato esperado: " + formatter.toString().replace("yyyy", "aaaa").replace("MM", "mm").replace("dd", "dd"));
        }
        return true;
    }
}

package br.com.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpConnectionUtil {

    public static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 1. Habilita o módulo para lidar com os tipos de data e hora do Java 8
        objectMapper.registerModule(new JavaTimeModule());

        // 2. Configuração para não falhar se o JSON tiver campos extras
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 3. Garante que enums sejam serializados como texto simples (ex: "EMAIL")
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        // 4. Força a serialização de datas/horas para o formato de texto padrão ISO
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
    }

    /**
     * GET que desserializa a resposta para um único objeto.
     */
    public static <T> T sendGetRequest(String endpoint, Class<T> responseType) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        String jsonResponse = readResponse(connection);

        // Debug opcional para objeto único
        System.out.println("========================================================");
        System.out.println("DEBUG JSON RECEBIDO (" + responseType.getSimpleName() + "): " + jsonResponse);
        System.out.println("========================================================");

        return objectMapper.readValue(jsonResponse, responseType);
    }

    /**
     * GET que desserializa a resposta para uma lista de objetos.
     */
    public static <T> List<T> sendGetListRequest(String endpoint, Class<T> elementType) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        String jsonResponse = readResponse(connection);

        // LOG IMPORTANTE: ver o JSON bruto recebido para listas (Pessoa, Contato, etc.)
        System.out.println("========================================================");
        System.out.println("DEBUG JSON RECEBIDO (LISTA " + elementType.getSimpleName() + "): " + jsonResponse);
        System.out.println("========================================================");

        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, elementType);

        return objectMapper.readValue(jsonResponse, listType);
    }

    /**
     * GET que retorna a resposta bruta como String.
     */
    public static String sendGetRawResponse(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        return readResponse(connection);
    }

    /**
     * POST com corpo JSON.
     */
    public static String sendPostRequest(String endpoint, Object requestBody) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = objectMapper.writeValueAsString(requestBody);

        // DEBUG: ver o JSON enviado
        System.out.println("========================================================");
        System.out.println("DEBUG JSON ENVIADO (BODY): " + jsonInputString);
        System.out.println("========================================================");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return readResponse(connection);
    }

    /**
     * PUT com corpo JSON.
     */
    public static String sendPutRequest(String endpoint, Object requestBody) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = objectMapper.writeValueAsString(requestBody);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return readResponse(connection);
    }

    /**
     * PATCH com corpo JSON (via POST + X-HTTP-Method-Override).
     */
    public static String sendPatchRequest(String endpoint, Object requestBody) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = objectMapper.writeValueAsString(requestBody);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return readResponse(connection);
    }

    /**
     * DELETE simples.
     */
    public static String sendDeleteRequest(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");

        return readResponse(connection);
    }

    /**
     * Lê a resposta (ou erro) da conexão HTTP.
     */
    private static String readResponse(HttpURLConnection connection) throws Exception {
        int responseCode = connection.getResponseCode();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                (responseCode >= 200 && responseCode < 300)
                        ? connection.getInputStream()
                        : connection.getErrorStream()
        ))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            if (responseCode >= 200 && responseCode < 300) {
                return response.toString();
            } else {
                throw new RuntimeException(
                        "Falha na requisição HTTP. Código: " + responseCode +
                                ", Mensagem: " + response
                );
            }
        }
    }
}

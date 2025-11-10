package br.com.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        // Configura o ObjectMapper para lidar com tipos de data e hora do Java 8
        objectMapper.registerModule(new JavaTimeModule());
        // Opcional: Configurar para falhar em propriedades desconhecidas
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Envia uma requisição GET e desserializa a resposta JSON para um único objeto do tipo especificado.
     *
     * @param endpoint O endpoint da API (ex: "/precos/1").
     * @param responseType A classe do tipo de objeto esperado na resposta.
     * @param <T> O tipo do objeto esperado.
     * @return Um objeto do tipo T.
     * @throws Exception Se ocorrer um erro na requisição ou desserialização.
     */
    public static <T> T sendGetRequest(String endpoint, Class<T> responseType) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        String jsonResponse = readResponse(connection);
        return objectMapper.readValue(jsonResponse, responseType);
    }

    /**
     * Envia uma requisição GET e desserializa a resposta JSON para uma lista de objetos do tipo especificado.
     *
     * @param endpoint O endpoint da API (ex: "/precos").
     * @param elementType A classe do tipo de elemento esperado na lista.
     * @param <T> O tipo dos elementos na lista.
     * @return Uma lista de objetos do tipo T.
     * @throws Exception Se ocorrer um erro na requisição ou desserialização.
     */
    public static <T> List<T> sendGetListRequest(String endpoint, Class<T> elementType) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        String jsonResponse = readResponse(connection);
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
        return objectMapper.readValue(jsonResponse, listType);
    }

    /**
     * Envia uma requisição GET e retorna a resposta JSON bruta como String.
     * Útil para desserialização de tipos genéricos complexos fora desta classe.
     *
     * @param endpoint O endpoint da API.
     * @return A resposta JSON bruta como String.
     * @throws Exception Se ocorrer um erro na requisição.
     */
    public static String sendGetRawResponse(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        return readResponse(connection);
    }

    /**
     * Envia uma requisição POST com um corpo de requisição em JSON e retorna a resposta como String.
     *
     * @param endpoint O endpoint da API.
     * @param requestBody O objeto Java a ser serializado para JSON e enviado no corpo da requisição.
     * @return A resposta da API como String.
     * @throws Exception Se ocorrer um erro na requisição ou serialização.
     */
    public static String sendPostRequest(String endpoint, Object requestBody) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
     * Envia uma requisição PUT com um corpo de requisição em JSON e retorna a resposta como String.
     *
     * @param endpoint O endpoint da API.
     * @param requestBody O objeto Java a ser serializado para JSON e enviado no corpo da requisição.
     * @return A resposta da API como String.
     * @throws Exception Se ocorrer um erro na requisição ou serialização.
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
     * Envia uma requisição PATCH com um corpo de requisição em JSON e retorna a resposta como String.
     * Nota: HttpURLConnection não suporta PATCH diretamente. Usa POST com X-HTTP-Method-Override.
     *
     * @param endpoint O endpoint da API.
     * @param requestBody O objeto Java a ser serializado para JSON e enviado no corpo da requisição.
     * @return A resposta da API como String.
     * @throws Exception Se ocorrer um erro na requisição ou serialização.
     */
    public static String sendPatchRequest(String endpoint, Object requestBody) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        connection.setRequestMethod("POST"); // O método real enviado será POST
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
     * Envia uma requisição DELETE e retorna a resposta como String.
     *
     * @param endpoint O endpoint da API.
     * @return A resposta da API como String.
     * @throws Exception Se ocorrer um erro na requisição.
     */
    public static String sendDeleteRequest(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json"); // Pode ser útil para algumas APIs

        return readResponse(connection);
    }

    /**
     * Lê a resposta de uma conexão HTTP.
     *
     * @param connection A conexão HttpURLConnection.
     * @return A resposta do servidor como String.
     * @throws Exception Se ocorrer um erro na leitura da resposta ou se o código de resposta indicar falha.
     */
    private static String readResponse(HttpURLConnection connection) throws Exception {
        int responseCode = connection.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) { // Sucesso (2xx)
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            // Para erros, também é útil ler a mensagem de erro do servidor
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String errorLine;
            StringBuilder errorResponse = new StringBuilder();
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            throw new RuntimeException("Falha na requisição HTTP. Código: " + responseCode + ", Mensagem: " + errorResponse.toString());
        }
    }
}

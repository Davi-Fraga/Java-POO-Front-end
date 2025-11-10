package br.com.service;

import br.com.model.PerfilAcesso;
import br.com.util.HttpConnectionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Map;

public class PerfilAcessoService {

    private static final String ENDPOINT = "/perfil-acessos";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PerfilAcessoService() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public List<PerfilAcesso> listarPerfis() throws Exception {
        // A URL é a URL base do endpoint, sem parâmetros.
        // O HttpConnectionUtil é chamado com a URL e o tipo de retorno esperado.
        return HttpConnectionUtil.sendGetListRequest(ENDPOINT, PerfilAcesso.class);
    }

    public PerfilAcesso adicionarPerfil(String nome) throws Exception {
        Map<String, String> requestBody = Map.of("nomePerfil", nome);
        String jsonResponse = HttpConnectionUtil.sendPostRequest(ENDPOINT, requestBody);
        return objectMapper.readValue(jsonResponse, PerfilAcesso.class);
    }

    public void removerPerfil(int id) throws Exception {
        HttpConnectionUtil.sendDeleteRequest(ENDPOINT + "/" + id);
    }

    public void alternarStatus(int id) throws Exception {
        // O backend deve ter um endpoint específico para isso, assumindo PATCH
        HttpConnectionUtil.sendPatchRequest(ENDPOINT + "/" + id + "/status", null);
    }

    public void modificarPermissao(int id, String area, boolean podeAcessar) throws Exception {
        Map<String, Object> requestBody = Map.of("area", area, "permitido", podeAcessar);
        HttpConnectionUtil.sendPatchRequest(ENDPOINT + "/" + id + "/permissoes", requestBody);
    }
}

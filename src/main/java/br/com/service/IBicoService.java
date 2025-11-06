package br.com.service;

import br.com.model.Bico;

import java.util.List;

public interface IBicoService {
    List<Bico> listarBicos() throws Exception;
}